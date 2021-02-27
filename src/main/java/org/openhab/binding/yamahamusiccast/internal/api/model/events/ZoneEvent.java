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
 * Returns information related to Zone
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class ZoneEvent {

    protected String power;
    protected String input;
    protected Integer volume;
    protected Boolean mute;
    protected Boolean statusUpdated;
    protected Boolean signalInfoUpdated;

    /*
     * Returns power status Values: "on" / "standby"
     */
    public String getPower() {
        return power;
    }

    /*
     * Returns current Input ID Values: Input IDs gotten via /system/getFeature
     */
    public String getInput() {
        return input;
    }

    /*
     * Returns volume value Values: Value range calculated by minimum/maximum/step
     * values gotten via /system/getFeatures
     */
    public Integer getVolume() {
        return volume;
    }

    /*
     * Returns mute status
     */
    public Boolean getMute() {
        return mute;
    }

    /*
     * Returns whether or not other info has changed than main zone power/input/volume/mute status.
     * If so, pull renewed info using /main/getStatus
     */
    public Boolean getStatusUpdated() {
        return statusUpdated;
    }

    /*
     * Returns whether or not signal info has changed. If so, pull renewed info using
     * /main/getSignalInfo
     */
    public Boolean getSignalInfoUpdated() {
        return signalInfoUpdated;
    }
}
