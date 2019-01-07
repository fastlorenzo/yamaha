/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.internal.api.model.events;

import com.google.gson.annotations.SerializedName;

/**
 * Returns CD related information 
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class CdEvent {

    @SerializedName("device_status")
    protected String deviceStatus;
    @SerializedName("play_time")
    protected Integer playTime;
    @SerializedName("play_info_updated")
    protected Boolean playInfoUpdated;

    /*
     * Returns CD device status Values: "open" / "close" / "ready" / "not_ready" 
     */
    public String getDeviceStatus() {
        return deviceStatus;
    }

    /*
     * Returns current playback time (unit in second).  Value Range: -59999 ~ 59999 
     */
    public Integer getPlayTime() {
        return playTime;
    }

    /*
     * Returns whether or not playback info has changed. If so, pull renewed info using /cd/getPlayInfo 
     */
    public Boolean getPlayInfoUpdated() {
        return playInfoUpdated;
    }
}
