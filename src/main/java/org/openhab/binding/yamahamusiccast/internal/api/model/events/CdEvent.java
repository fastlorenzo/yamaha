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
package org.openhab.binding.yamahamusiccast.internal.api.model.events;

/**
 * Returns CD related information
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class CdEvent {

    protected String deviceStatus;
    protected Integer playTime;
    protected Boolean playInfoUpdated;

    /*
     * Returns CD device status Values: "open" / "close" / "ready" / "not_ready"
     */
    public String getDeviceStatus() {
        return deviceStatus;
    }

    /*
     * Returns current playback time (unit in second). Value Range: -59999 ~ 59999
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
