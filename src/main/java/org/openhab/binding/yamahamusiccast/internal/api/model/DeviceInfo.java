/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
