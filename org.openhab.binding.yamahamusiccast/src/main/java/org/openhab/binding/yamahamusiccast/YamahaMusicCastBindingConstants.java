/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link YamahaMusicCastBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Frank Zimmer - Initial contribution
 * @author Dries Decock
 */
public class YamahaMusicCastBindingConstants {

    public static final String BINDING_ID = "yamahamusiccast";

    // List all Thing Type UIDs, related to the YamahaMusicCast Binding
    public static final ThingTypeUID THING_TYPE_SPEAKER = new ThingTypeUID(BINDING_ID, "speaker");

    // List of all Channel ids
    public static final String CHANNEL_POWER = "power";
    public static final String CHANNEL_VOLUME = "volume";
    public static final String CHANNEL_MUTE = "mute";
    public static final String CHANNEL_INPUT = "input";
    public static final String CHANNEL_ALBUM_ART = "albumAart";

    // The supported thing types
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_SPEAKER);

    // Configuration constants
    public static class Configs {
        public static final String CONFIG_HOST_NAME = "host";
        public static final String CONFIG_ZONE = "zone";
    }

    // Default UDP listening events  port
    public static final int EVENTS_DEFAULT_PORT = 41100;

    // List of properties
    public static final String DESC_DEVICE_TYPE = "MediaRenderer";
    public static final String DESC_MANUFACTURER = "Yamaha Corporation";
    public static final String DESC_FRIENDLY_NAME = "friendlyName";
    public static final String DESC_MODEL_NAME = "modelName";
    public static final String DESC_SERIAL_NUMBER = "serialNumber";
    public static final String DESC_IP_ADDRESS = "ipAddress";

    /**
     * The names of this enum are part of the protocols!
     * Receivers have different capabilities, some have 2 zones, some up to 4.
     * Every receiver has a "Main_Zone".
     */
    public enum Zone {
        main,
        zone2,
        zone3,
        zone4
    }

    /**
     * All possible inputs for this device
     */
    public enum Input {
        cd,
        tuner,
        multi_ch,
        phono,
        hdmi1,
        hdmi2,
        hdmi3,
        hdmi4,
        hdmi5,
        hdmi6,
        hdmi7,
        hdmi8,
        hdmi,
        av1,
        av2,
        av3,
        av4,
        av5,
        av6,
        av7,
        v_aux,
        aux1,
        aux2,
        aux,
        audio1,
        audio2,
        audio3,
        audio4,
        audio_cd,
        audio,
        optical1,
        optical2,
        optical,
        coaxial1,
        coaxial2,
        coaxial,
        digital1,
        digital2,
        digital,
        line1,
        line2,
        line3,
        line_cd,
        analog,
        tv,
        bd_dvd,
        usb_dac,
        usb,
        bluetooth,
        server,
        net_radio,
        rhapsody,
        napster,
        pandora,
        siriusxm,
        spotify,
        juke,
        airplay,
        radiko,
        qobuz,
        mc_link,
        main_sync,
        none
    }

}
