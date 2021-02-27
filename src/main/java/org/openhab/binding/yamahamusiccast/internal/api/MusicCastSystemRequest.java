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
import org.openhab.binding.yamahamusiccast.internal.api.model.DeviceInfo;

import com.google.gson.Gson;

/**
 * The {@link MusicCastRequest} encapsulates a request sent by the {@link YamahaMusicCast}.
 *
 * @author Hector Rodriguez Medina - Initial contribution
 *
 * @param <T> The response type expected as a result of the request's execution
 */
@NonNullByDefault
public class MusicCastSystemRequest<T> extends MusicCastRequest<T> {

    /**
     *
     * @param host IP address of the target device
     */
    public MusicCastSystemRequest(Gson gson, HttpClient httpClient, String host) {
        super(gson, httpClient, host, 80);
    }

    /**
     * For retrieving basic information of a Device
     *
     * @return DeviceInfo
     * @throws MusicCastException
     */
    public @Nullable DeviceInfo getDeviceInfo() throws MusicCastException {
        setResultType((Class<T>) DeviceInfo.class);
        DeviceInfo info;
        setPath(DeviceInfo.url);
        info = execute();
        return info;
    }
}
