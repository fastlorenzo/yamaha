/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.yamahamusiccast.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.NextPreviousType;
import org.eclipse.smarthome.core.library.types.PlayPauseType;
import org.eclipse.smarthome.core.library.types.StopMoveType;
import org.eclipse.smarthome.core.library.types.RewindFastforwardType;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants;
import org.openhab.binding.yamahamusiccast.internal.api.model.Response;
import org.openhab.binding.yamahamusiccast.internal.api.model.PlayInfo;
import com.google.gson.Gson;
import org.eclipse.jetty.client.HttpClient;

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
    public PlayInfo getPlayInfo() throws MusicCastException {
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
     * / "previous" / "next" / "fast_reverse_start" / "fast_reverse_end" /
     * "fast_forward_start" / "fast_forward_end")
     * @return Response code
     * @throws MusicCastException
     */
    public Response setPlayback(Command command) throws MusicCastException {
        setResultType((Class<T>) Response.class);
        String playback = "";
        Response response;
        setPath(YamahaMusicCastBindingConstants.NETWORK_USB_SET_PLAYBACK_PATH);
        clearQueryParameter();

        if (command == PlayPauseType.PLAY) {
            playback = YamahaMusicCastBindingConstants.PLAYBACK_PLAY_PAUSE;
        } else if (command == PlayPauseType.PAUSE) {
                playback = YamahaMusicCastBindingConstants.PLAYBACK_PLAY_PAUSE;
        } else if (command == StopMoveType.STOP) {
                playback = YamahaMusicCastBindingConstants.PLAYBACK_STOP;
        } else if (command == NextPreviousType.NEXT) {
                playback = YamahaMusicCastBindingConstants.PLAYBACK_NEXT;
        } else if (command == NextPreviousType.PREVIOUS) {
                playback = YamahaMusicCastBindingConstants.PLAYBACK_PREVIOUS;
        }

/*             case RewindFastforwardType.FASTFORWARD:
                playback = ;
                break;
            case RewindFastforwardType.REWIND:
                playback = ;
                break;
 */

        setQueryParameter(YamahaMusicCastBindingConstants.PLAYBACK_PARAMETER, playback);
        response = execute();
        return response;
    }
}