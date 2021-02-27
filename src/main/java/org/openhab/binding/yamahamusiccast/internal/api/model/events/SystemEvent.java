/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.internal.api.model.events;

/**
 * Returns System related information
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class SystemEvent {

    protected Boolean bluetoothInfoUpdated;
    protected Boolean funcStatusUpdated;
    protected Boolean speakerSettingsUpdated;
    protected Boolean nameTextUpdated;
    protected Boolean tagUpdated;
    protected Boolean locationInfoUpdated;
    protected Boolean stereoPairInfoUpdated;

    /**
     * Returns whether or not Bluetooth info has changed. If so, pull
     * renewed info using /system/getBluetoothInfo
     */
    public Boolean getBluetoothInfoUpdated() {
        return bluetoothInfoUpdated;
    }

    /**
     * Returns whether or not System overall info has changed. If so,
     * pull renewed info using /system/getFuncStatus
     */
    public Boolean getFuncStatusUpdated() {
        return funcStatusUpdated;
    }

    /**
     * Reserved
     */
    public Boolean getSpeakerSettingsUpdated() {
        return speakerSettingsUpdated;
    }

    /**
     * Returns whether or not name text info has changed. If so, pull
     * renewed info using /system/getNameText
     */
    public Boolean getNameTextUpdated() {
        return nameTextUpdated;
    }

    /**
     * Reserved
     */
    public Boolean getTagUpdated() {
        return tagUpdated;
    }

    /**
     * Returns whether or not Location info has changed. If so, pull
     * renewed info using /system/getLocationInfo
     */
    public Boolean getLocationInfoUpdated() {
        return locationInfoUpdated;
    }

    /**
     * Reserved
     */
    public Boolean getStereoPairInfoUpdated() {
        return stereoPairInfoUpdated;
    }
}
