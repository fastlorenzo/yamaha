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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
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
import org.openhab.binding.yamahamusiccast.internal.api.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The {@link YamahaMusicCastHandler} is responsibhe type org.eclipse.jdt.annotation.NonNullByDefault cannot be
 * resolved. le for handling commands, which are
 * sent to one of the channels.
 *
 * @author Frank Zimmer - Initial contribution
 */
public class YamahaMusicCastHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(YamahaMusicCastHandler.class);
    private String host;
    private static String ON = "on";
    private static String OFF = "standby";

    private HttpClient httpClient;
    private Gson gson;
    private Status state;
    private @Nullable ScheduledFuture<?> refreshJob;
    private @Nullable YamahaMusicCastThingConfig config;

    public YamahaMusicCastHandler(Thing thing) {
        super(thing);
        host = (String) getConfig().get("host");
        this.httpClient = new HttpClient();
        this.httpClient.setFollowRedirects(false);
        this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        // .registerTypeAdapter(Status.class, new TidyLowerCaseStringDeserializer()).create();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling command = {} for channel = {}", command, channelUID);
        if (command == REFRESH) {
            refresh(channelUID);
        } else {
            if (channelUID.getId().equals(POWER)) {
                if (command.equals(OnOffType.ON)) {
                    postPowerState(ON);
                } else if (command.equals(OnOffType.OFF)) {
                    postPowerState(OFF);
                }
            }
            if (channelUID.getId().equals(VOLUME)) {
                postVolumeState(command.toString());
            }
        }
    }

    private void getUpdate() {
        logger.info("Trying to connect to " + host);
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
            MusicCastRequest<Status> statusRequest = newRequest(Status.class);
            statusRequest.setPath("/YamahaExtendedControl/v1/main/getStatus");
            logger.info("Requesting ....");
            state = statusRequest.execute();
            if (state != null) {
                logger.info("Result is " + state.toString());
            }
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

        String urlString = "http://" + host + "/YamahaExtendedControl/v1/main/setPower?power=" + state;
        logger.debug(urlString);
        try {

            // Create HTTP GET request
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            logger.debug(response.toString());

            logger.debug("YAMAHA: " + responseCode + "; " + connection.getResponseMessage());
            // Process response
            if (responseCode == 200) {
                if (state.equals(ON)) {
                    updateState(POWER, OnOffType.ON);
                } else if (state.equals(OFF)) {
                    updateState(POWER, OnOffType.OFF);
                    updateStatus(ThingStatus.ONLINE);
                }
            }
            logger.debug("Update of {} : {} to {}", this.getThing().getUID(), POWER, state);

        } catch (Exception e) {
            logger.warn("Unable to post state: " + e.getMessage(), e);
            updateStatus(ThingStatus.OFFLINE);
        }

    }

    private void postVolumeState(String volume) {

        String urlString = "http://" + host + "/YamahaExtendedControl/v1/main/setVolume?volume=" + volume;
        logger.debug(urlString);
        try {

            // Create HTTP GET request
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            logger.debug("YAMAHA: " + responseCode);
            // Process response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            logger.debug(response.toString());

            updateState(VOLUME, new PercentType(volume));

            logger.debug("Update of {} : {} to {}", this.getThing().getUID(), POWER, volume);

            updateStatus(ThingStatus.ONLINE);

        } catch (Exception e) {
            logger.warn("Unable to post state: " + e.getMessage(), e);
            updateStatus(ThingStatus.OFFLINE);
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
}
