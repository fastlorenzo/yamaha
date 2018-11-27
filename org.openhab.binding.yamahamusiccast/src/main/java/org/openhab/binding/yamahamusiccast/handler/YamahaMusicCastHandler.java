/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.handler;

import static org.eclipse.smarthome.core.thing.ThingStatus.OFFLINE;
import static org.eclipse.smarthome.core.thing.ThingStatus.ONLINE;
import static org.eclipse.smarthome.core.thing.ThingStatusDetail.*;
import static org.eclipse.smarthome.core.types.RefreshType.REFRESH;
import static org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.RawType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ThingStatusInfoBuilder;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.yamahamusiccast.internal.YamahaMusicCastThingConfig;
import org.openhab.binding.yamahamusiccast.internal.api.MusicCastException;
import org.openhab.binding.yamahamusiccast.internal.api.MusicCastRequest;
import org.openhab.binding.yamahamusiccast.internal.api.model.DeviceInfo;
import org.openhab.binding.yamahamusiccast.internal.api.model.PlayInfo;
import org.openhab.binding.yamahamusiccast.internal.api.model.Response;
import org.openhab.binding.yamahamusiccast.internal.api.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The {@link YamahaMusicCastHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Frank Zimmer - Initial contribution
 * @author Dries Decock - Adding extra channels and refresh from speaker
 */
public class YamahaMusicCastHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(YamahaMusicCastHandler.class);
    private String host;
    private static String ON = "on";
    private static String OFF = "standby";

    private HttpClient httpClient;
    private Gson gson;
    private DeviceInfo info;
    private Status state;
    private PlayInfo playInfo;
    private @Nullable ScheduledFuture<?> refreshJob;
    private @Nullable YamahaMusicCastThingConfig config;

    public YamahaMusicCastHandler(Thing thing) {
        super(thing);
        host = (String) getConfig().get("host");
        this.httpClient = new HttpClient();
        this.httpClient.setFollowRedirects(false);
        this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        state = null;
        info = null;
        playInfo = null;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling command = {} for channel = {}", command, channelUID);
        if (command == REFRESH) {
            refresh(channelUID);
        } else {
            switch (channelUID.getId()) {
                case POWER:
                    if (command.equals(OnOffType.ON)) {
                        postPowerState(ON);
                    } else if (command.equals(OnOffType.OFF)) {
                        postPowerState(OFF);
                    }
                    break;
                case MUTE:
                    if (command.equals(OnOffType.ON)) {
                        postMuteState("true");
                    } else if (command.equals(OnOffType.OFF)) {
                        postMuteState("false");
                    }
                    break;
                case INPUT:
                    postInput(command.toString());
                case VOLUME:
                    postVolumeState(command.toString());
                    break;
            }
        }
    }

    private void setValue(String url, String key, String value) throws MusicCastException {
        if (!httpClient.isStarted()) {
            try {
                logger.info("Connecting to host.");
                httpClient.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.info("Failed to start HTTP client");
                e.printStackTrace();
            }
        }
        MusicCastRequest<Response> setRequest = newRequest(Response.class);
        setRequest.setPath(url);
        setRequest.setQueryParameter(key, value);
        logger.info("Requesting ....");
        Response response = setRequest.execute();
        if (response != null) {
            logger.info("Result is " + response.toString());
        }
    }

    private void getUpdate() {
        try {
            if (!httpClient.isStarted()) {
                try {
                    logger.info("Connecting to host.");
                    httpClient.start();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.info("Failed to start HTTP client");
                    e.printStackTrace();
                }
            }
            if (info == null) {
                MusicCastRequest<DeviceInfo> infoRequest = newRequest(DeviceInfo.class);
                infoRequest.setPath(DeviceInfo.url);
                info = infoRequest.execute();
            }
            MusicCastRequest<Status> statusRequest = newRequest(Status.class);
            statusRequest.setPath(Status.url);
            logger.info("Requesting ....");
            state = statusRequest.execute();
            if (state != null) {
                logger.info("Result is " + state.toString());
            }
            MusicCastRequest<PlayInfo> playInfoRequest = newRequest(PlayInfo.class);
            playInfoRequest.setPath(PlayInfo.url);
            playInfo = playInfoRequest.execute();

        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info(e.toString());
        }
    }

    private void refresh(ChannelUID channelUID) {
        logger.info("Refresh Channel " + channelUID.getAsString());
        getUpdate();
        logger.info("Refreshed everything.");
        String channelID = channelUID.getId();
        State result = null;
        if (state != null) {
            switch (channelID) {
                case VOLUME:
                    result = new PercentType(state.getVolume() * 100 / state.getMax_volume());
                    break;
                case POWER:
                    result = OnOffType.from(state.getPower());
                    break;
                case MUTE:
                    result = OnOffType.from(state.isMute());
                    break;
                case INPUT:
                    result = StringType.valueOf(state.getInput());
                    break;
                case MODEL_NAME:
                    result = StringType.valueOf(info.getModel_name());
                    break;
                case ALBUM_ART:
                    String urlString = "http://" + host + playInfo.getAlbumart_url();
                    URL url;
                    try {
                        url = new URL(urlString);
                        result = new RawType(readImage(url).toByteArray(), "image/jpeg");
                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
            }
        }
        if (result != null) {
            updateState(channelID, result);
        }
    }

    @Override
    public void initialize() {
        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.
        config = getConfig().as(YamahaMusicCastThingConfig.class);
        updateStatus(ThingStatus.ONLINE);

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work
        // as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }

    private void postPowerState(String state) {
        try {
            setValue("/YamahaExtendedControl/v2/main/setPower", "power", state);
            refresh();
        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void postInput(String input) {
        try {
            setValue("/YamahaExtendedControl/v2/main/setInput", "input", input);
            refresh();
        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void postVolumeState(String volume) {
        try {
            setValue("/YamahaExtendedControl/v2/main/setVolume", "volume", volume);
            refresh();
        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void postMuteState(String mute) {
        try {
            setValue("/YamahaExtendedControl/v2/main/setMute", "enable", mute);
            refresh();
        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void updateStatus(ThingStatus status, ThingStatusDetail statusDetail, @Nullable String description) {
        if (status == ONLINE || (status == OFFLINE && statusDetail == COMMUNICATION_ERROR)) {
            scheduleRefreshJob();
        } else if (status == OFFLINE && statusDetail == CONFIGURATION_ERROR) {
            cancelRefreshJob();
        }
        // mgb: update the status only if it's changed
        ThingStatusInfo statusInfo = ThingStatusInfoBuilder.create(status, statusDetail).withDescription(description)
                .build();
        if (!statusInfo.equals(getThing().getStatusInfo())) {
            super.updateStatus(status, statusDetail, description);
        }
    }

    private void refresh() throws MusicCastException {
        logger.debug("Refreshing the MusicCast speaker {}", getThing().getUID());
        getUpdate();
        for (Channel channel : getThing().getChannels()) {
            ChannelUID channelUID = channel.getUID();
            refresh(channelUID);
        }
    }

    private void run() {
        try {
            logger.trace("Executing refresh job");
            refresh();
            updateStatus(ONLINE);
        } catch (Exception e) {
            logger.warn("Unhandled exception while refreshing the Yamaha MusicCast Speaker {} - {}",
                    getThing().getUID(), e.getMessage());
            updateStatus(OFFLINE, COMMUNICATION_ERROR, e.getMessage());
        }
    }

    // Private API

    private void scheduleRefreshJob() {
        synchronized (this) {
            if (refreshJob == null) {
                logger.debug("Scheduling refresh job every {}s", 1);
                refreshJob = scheduler.scheduleWithFixedDelay(this::run, 0, config.getRefreshInterval(),
                        TimeUnit.SECONDS);
            }
        }
    }

    private void cancelRefreshJob() {
        synchronized (this) {
            if (refreshJob != null) {
                logger.debug("Cancelling refresh job");
                refreshJob.cancel(true);
                refreshJob = null;
            }
        }
    }

    private <T> MusicCastRequest<T> newRequest(Class<T> responseType) {
        return new MusicCastRequest<T>(responseType, gson, httpClient, host, 80);
    }

    private static ByteArrayOutputStream readImage(URL url) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        java.net.URI uri = url.toURI();
        InputStream is = null;
        try {
            is = url.openStream();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace();
            // Perform any other exception handling that's appropriate.
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return baos;
    }
}
