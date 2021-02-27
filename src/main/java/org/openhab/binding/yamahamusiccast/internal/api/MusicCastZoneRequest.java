/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.yamahamusiccast.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants;
import org.openhab.binding.yamahamusiccast.internal.api.model.Response;
import org.openhab.binding.yamahamusiccast.internal.api.model.Status;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

import com.google.gson.Gson;

/**
 * The {@link MusicCastRequest} encapsulates a request sent by the {@link YamahaMusicCast}.
 *
 * @author Hector Rodriguez Medina - Initial contribution
 *
 * @param <T> The response type expected as a result of the request's execution
 */
@NonNullByDefault
public class MusicCastZoneRequest<T> extends MusicCastRequest<T> {

    /**
     *
     * @param host IP address of the target device
     */
    public MusicCastZoneRequest(Gson gson, HttpClient httpClient, String host) {
        super(gson, httpClient, host, 80);
    }

    /**
     * For retrieving basic information of a Device
     *
     * @return Status
     * @throws MusicCastException
     */
    public @Nullable Status getStatus(String zone) throws MusicCastException {
        setResultType((Class<T>) Status.class);
        String url = YamahaMusicCastBindingConstants.ROOT_PATH + "/" + zone + Status.path;
        Status status;
        setPath(url);
        status = execute();
        return status;
    }

    public @Nullable Response setPower(String zone, Command command) throws MusicCastException {
        setResultType((Class<T>) Response.class);
        String url = YamahaMusicCastBindingConstants.ROOT_PATH + "/" + zone
                + YamahaMusicCastBindingConstants.ZONE_SET_POWER_METHOD;
        String power = "";
        Response response;
        setPath(url);
        clearQueryParameter();

        if (command == OnOffType.ON) {
            power = YamahaMusicCastBindingConstants.ZONE_POWER_ON;
        } else if (command == OnOffType.OFF) {
            power = YamahaMusicCastBindingConstants.ZONE_POWER_STANDBY;
        }

        setQueryParameter(YamahaMusicCastBindingConstants.SET_POWER_PARAMETER, power);
        response = execute();
        return response;
    }

    public @Nullable Response setVolume(String zone, Command command, Integer maxVolume) throws MusicCastException {
        setResultType((Class<T>) Response.class);
        String url = YamahaMusicCastBindingConstants.ROOT_PATH + "/" + zone
                + YamahaMusicCastBindingConstants.ZONE_SET_VOLUME_METHOD;
        Integer adjustedVolume;
        String volume;
        if (command == IncreaseDecreaseType.INCREASE) {
            volume = "up";
        } else if (command == IncreaseDecreaseType.DECREASE) {
            volume = "down";
        } else {
            adjustedVolume = Integer.valueOf(command.toString()) * maxVolume / 100;
            volume = adjustedVolume.toString();
        }
        Response response;
        setPath(url);
        clearQueryParameter();
        setQueryParameter(YamahaMusicCastBindingConstants.SET_VOLUME_PARAMETER, volume);
        response = execute();
        return response;
    }

    public @Nullable Response setMute(String zone, Command command) throws MusicCastException {
        setResultType((Class<T>) Response.class);
        String url = YamahaMusicCastBindingConstants.ROOT_PATH + "/" + zone
                + YamahaMusicCastBindingConstants.ZONE_SET_MUTE_METHOD;
        boolean mute = false;
        Response response;
        setPath(url);
        clearQueryParameter();

        if (command == OnOffType.ON) {
            mute = true;
        } else if (command == OnOffType.OFF) {
            mute = false;
        }

        setQueryParameter(YamahaMusicCastBindingConstants.SET_MUTE_PARAMETER, mute);
        response = execute();
        return response;
    }

    public @Nullable Response setInput(String zone, Command command) throws MusicCastException {
        setResultType((Class<T>) Response.class);
        String url = YamahaMusicCastBindingConstants.ROOT_PATH + "/" + zone
                + YamahaMusicCastBindingConstants.ZONE_SET_INPUT_METHOD;
        String input = command.toString();
        Response response;
        setPath(url);
        clearQueryParameter();
        setQueryParameter(YamahaMusicCastBindingConstants.SET_INPUT_PARAMETER, input);
        response = execute();
        return response;
    }
}
