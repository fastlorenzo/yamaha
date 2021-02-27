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
package org.openhab.binding.yamahamusiccast.internal.api.model;

/**
 * DeviceInfo
 *
 * @author Dries Decock - Initial contribution
 */

public class DeviceInfo extends Response {

    public final static String url = "/YamahaExtendedControl/v2/system/getDeviceInfo";

    protected String modelName;

    protected String destination;

    protected String deviceId;

    protected float systemVersion;

    protected float apiVersion;

    protected String netmoduleVersion;

    public static String getUrl() {
        return url;
    }

    public String getModelName() {
        return modelName;
    }

    public String getDestination() {
        return destination;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public float getSystemVersion() {
        return systemVersion;
    }

    public float getApiVersion() {
        return apiVersion;
    }

    public String getNetmoduleVersion() {
        return netmoduleVersion;
    }
}
