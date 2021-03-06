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
import org.openhab.binding.yamahamusiccast.internal.api.model.PlayInfo;
import org.openhab.binding.yamahamusiccast.internal.api.model.Response;
import org.openhab.core.library.types.NextPreviousType;
import org.openhab.core.library.types.PlayPauseType;
import org.openhab.core.library.types.StopMoveType;
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
public class MusicCastNetUSBRequest<T> extends MusicCastRequest<T> {

    /**
     *
     * @param host IP address of the target device
     */
    public MusicCastNetUSBRequest(Gson gson, HttpClient httpClient, String host) {
        super(gson, httpClient, host, 80);
    }

    /**
     * For retrieving basic information of a Device
     *
     * @return PlayInfo
     * @throws MusicCastException
     */
    public @Nullable PlayInfo getPlayInfo() throws MusicCastException {
        setResultType((Class<T>) PlayInfo.class);
        PlayInfo playInfo;
        setPath(PlayInfo.url);
        playInfo = execute();
        return playInfo;
    }

    /**
     * For controlling playback status
     *
     * @param playback Playback status ("play" / "stop" / "pause" / "play_pause"
     *            / "previous" / "next" / "fast_reverse_start" / "fast_reverse_end" /
     *            "fast_forward_start" / "fast_forward_end")
     * @return Response code
     * @throws MusicCastException
     */
    public @Nullable Response setPlayback(Command command) throws MusicCastException {
        setResultType((Class<T>) Response.class);
        String playback = "";
        Response response;
        setPath(YamahaMusicCastBindingConstants.NETWORK_USB_SET_PLAYBACK_PATH);
        clearQueryParameter();

        if (command == PlayPauseType.PLAY) {
            playback = YamahaMusicCastBindingConstants.PLAYBACK_PLAY;
        } else if (command == PlayPauseType.PAUSE) {
            playback = YamahaMusicCastBindingConstants.PLAYBACK_PAUSE;
        } else if (command == StopMoveType.STOP) {
            playback = YamahaMusicCastBindingConstants.PLAYBACK_STOP;
        } else if (command == NextPreviousType.NEXT) {
            playback = YamahaMusicCastBindingConstants.PLAYBACK_NEXT;
        } else if (command == NextPreviousType.PREVIOUS) {
            playback = YamahaMusicCastBindingConstants.PLAYBACK_PREVIOUS;
        }

        /*
         * case RewindFastforwardType.FASTFORWARD:
         * playback = ;
         * break;
         * case RewindFastforwardType.REWIND:
         * playback = ;
         * break;
         */

        setQueryParameter(YamahaMusicCastBindingConstants.PLAYBACK_PARAMETER, playback);
        response = execute();
        return response;
    }
}
