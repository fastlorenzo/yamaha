/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
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
                //e.printStackTrace();
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
                    //e.printStackTrace();
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
            //e.printStackTrace();
            logger.debug(e.toString());
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
                    String urlString = "http://" + host;
                    if (playInfo.getAlbumart_url().isEmpty()) {
                        urlString += ":49154/Icons/120x120.jpg";
                    } else {
                        urlString += playInfo.getAlbumart_url();
                    }
                    URL url;
                    logger.debug("Getting image from " + urlString);
                    try {
                        url = new URL(urlString);
                        result = new RawType(readImage(url).toByteArray(), "image/jpeg");
                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                        //e1.printStackTrace();
                        logger.debug(e1.toString());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                        logger.debug(e.toString());
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
            //e.printStackTrace();
            logger.debug(e.toString());
        }

    }

    private void postInput(String input) {
        try {
            setValue("/YamahaExtendedControl/v2/main/setInput", "input", input);
            refresh();
        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            logger.debug(e.toString());
        }
    }

    private void postVolumeState(String volume) {
        try {
            setValue("/YamahaExtendedControl/v2/main/setVolume", "volume", volume);
            refresh();
        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            logger.debug(e.toString());
        }
    }

    private void postMuteState(String mute) {
        try {
            setValue("/YamahaExtendedControl/v2/main/setMute", "enable", mute);
            refresh();
        } catch (MusicCastException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            logger.debug(e.toString());
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
            //System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            //e.printStackTrace();
            //logger.debug(e.toString());
            baos.reset();
            baos.write(new String(
                    "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`")
                            .getBytes());
            // Perform any other exception handling that's appropriate.
            return baos;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return baos;
    }
}
