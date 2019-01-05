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
public class SystemEvent extends Event {

    protected boolean bluetooth_info_updated;
    protected boolean func_status_updated;
    protected boolean speaker_settings_updated;
    protected boolean name_text_updated;
    protected boolean tag_updated;
    protected boolean location_info_updated;
    protected boolean stereo_pair_info_updated;

    /**
     * Returns whether or not Bluetooth info has changed. If so, pull
     * renewed info using /system/getBluetoothInfo
     */
    public boolean getBluetoothInfoUpdated() {
        return bluetooth_info_updated;
    }

    /**
     * Returns whether or not System overall info has changed. If so,
     * pull renewed info using /system/getFuncStatus
     */
    public boolean getFuncStatusUpdated() {
        return func_status_updated;
    }

    /**
     * Reserved 
     */
    public boolean getSpeakerSettingsUpdated() {
        return speaker_settings_updated;
    }

    /**
     * Returns whether or not name text info has changed. If so, pull
     * renewed info using /system/getNameText
     */
    public boolean getNameTextUpdated() {
        return name_text_updated;
    }

    /**
     * Reserved
     */
    public boolean getTagUpdated() {
        return tag_updated;
    }

    /**
     * Returns whether or not Location info has changed. If so, pull
     * renewed info using /system/getLocationInfo 
     */
    public boolean getLocationInfoUpdated() {
        return location_info_updated;
    }

    /**
     * Reserved
     */
    public boolean getStereoPairInfoUpdated() {
        return stereo_pair_info_updated;
    }
}
