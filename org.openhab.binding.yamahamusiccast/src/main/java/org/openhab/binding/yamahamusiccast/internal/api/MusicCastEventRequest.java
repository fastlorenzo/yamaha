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
import org.openhab.binding.yamahamusiccast.internal.api.model.Response;
import org.openhab.binding.yamahamusiccast.internal.api.model.SubscribeEvent;
import static org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants.EVENTS_DEFAULT_PORT;
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
public class MusicCastEventRequest<T> extends MusicCastRequest<T> {

    /**
     * 
     * @param host IP address of the target device
     */
    public MusicCastEventRequest(Gson gson, HttpClient httpClient, String host) {
        super(gson, httpClient, host, 80);
    }

    /**
     * For retrieving basic information of a Device
     *
     * @return SubscribeEvent
     * @throws MusicCastException
     */
    public SubscribeEvent subscribeToEvents() throws MusicCastException {
        setResultType((Class<T>) SubscribeEvent.class);
        SubscribeEvent subscribeEvent;
        setPath(SubscribeEvent.url);
        setHeaders("X-AppName", SubscribeEvent.appName);
        setHeaders("X-AppPort", EVENTS_DEFAULT_PORT);
        subscribeEvent = execute();
        return subscribeEvent;
    }
}