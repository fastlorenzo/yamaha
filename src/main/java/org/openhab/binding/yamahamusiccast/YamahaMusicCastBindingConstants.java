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

import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link YamahaMusicCastBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Frank Zimmer - Initial contribution
 * @author Dries Decock
 * @author Hector Rodriguez Medina - Updates for event receiver
 */
public class YamahaMusicCastBindingConstants {

    public static final String BINDING_ID = "yamahamusiccast";

    // List all Thing Type UIDs, related to the YamahaMusicCast Binding
    public static final ThingTypeUID THING_TYPE_SPEAKER = new ThingTypeUID(BINDING_ID, "speaker");

    // List of all Channel ids
    public static final String CHANNEL_ZONE = "zone";
    public static final String CHANNEL_POWER = "power";
    public static final String CHANNEL_VOLUME = "volume";
    public static final String CHANNEL_MUTE = "mute";
    public static final String CHANNEL_INPUT = "input";
    public static final String CHANNEL_PLAYBACK = "playback";
    public static final String CHANNEL_ALBUM_ART = "albumArt";
    public static final String CHANNEL_ARTIST = "artist";
    public static final String CHANNEL_ALBUM = "album";
    public static final String CHANNEL_TRACK = "track";
    public static final String CHANNEL_ALBUMART_URL = "albumArtUrl";
    public static final String CHANNEL_PLAY_URI = "playuri";

    // The supported thing types
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_SPEAKER);

    // Configuration constants
    public static class Configs {
        public static final String CONFIG_HOST_NAME = "host";
        public static final String CONFIG_ZONE = "zone";
        public static final String CONFIG_UDN = "udn";
    }

    // Default UDP listening events port
    public static final int EVENTS_DEFAULT_PORT = 41100;

    // List of properties
    public static final String DESC_DEVICE_TYPE = "MediaRenderer";
    public static final String DESC_MANUFACTURER = "Yamaha Corporation";
    public static final String DESC_FRIENDLY_NAME = "friendlyName";
    public static final String DESC_MODEL_NAME = "modelName";
    public static final String DESC_SERIAL_NUMBER = "serialNumber";
    public static final String DESC_IP_ADDRESS = "ipAddress";

    // API
    public static final String ROOT_PATH = "/YamahaExtendedControl/v2";

    public static final String SET_POWER_PARAMETER = "power";
    public static final String ZONE_SET_POWER_METHOD = "/setPower";
    public static final String ZONE_POWER_ON = "on";
    public static final String ZONE_POWER_STANDBY = "standby";
    public static final String ZONE_POWER_TOGGLE = "toggle";

    public static final String SET_SLEEP_PARAMETER = "sleep";
    public static final String ZONE_SET_SLEEP_METHOD = "/setSleep";
    public static final int ZONE_SLEEP_0 = 0;
    public static final int ZONE_SLEEP_30 = 30;
    public static final int ZONE_SLEEP_60 = 60;
    public static final int ZONE_SLEEP_90 = 90;
    public static final int ZONE_SLEEP_120 = 120;

    public static final String SET_VOLUME_PARAMETER = "volume";
    public static final String ZONE_SET_VOLUME_METHOD = "/setVolume";
    public static final String SET_MUTE_PARAMETER = "enable";
    public static final String ZONE_SET_MUTE_METHOD = "/setMute";

    public static final String SET_INPUT_PARAMETER = "input";
    public static final String ZONE_SET_INPUT_METHOD = "/setInput";
    public static final String SET_SOUND_PROGRAM_PARAMETER = "program";
    public static final String ZONE_SET_SOUND_PROGRAM_METHOD = "/setSoundProgram";
    public static final String ZONE_PREPARE_INPUT_CHANGE_METHOD = "/prepareInputChange";

    public static final String NETWORK_USB_SET_PLAYBACK_PATH = ROOT_PATH + "/netusb/setPlayback";
    public static final String PLAYBACK_PARAMETER = "playback";
    public static final String PLAYBACK_PLAY = "play";
    public static final String PLAYBACK_STOP = "stop";
    public static final String PLAYBACK_PAUSE = "pause";
    public static final String PLAYBACK_PLAY_PAUSE = "play_pause";
    public static final String PLAYBACK_PREVIOUS = "previous";
    public static final String PLAYBACK_NEXT = "next";
    public static final String PLAYBACK_FAST_REVERSE_START = "fast_reverse_start";
    public static final String PLAYBACK_FAST_REVERSE_END = "fast_reverse_end";
    public static final String PLAYBACK_FAST_FORWARD_START = "fast_forward_start";
    public static final String PLAYBACK_FAST_FORWARD_END = "fast_forward_end";

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
