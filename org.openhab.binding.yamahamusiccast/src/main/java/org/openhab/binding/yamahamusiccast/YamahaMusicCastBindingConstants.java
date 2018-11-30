/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
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
 */
public class YamahaMusicCastBindingConstants {

    public static final String BINDING_ID = "yamahamusiccast";

    public static final ThingTypeUID BRIDGE_THING_TYPE = new ThingTypeUID(BINDING_ID, "speaker");
    public static final ThingTypeUID ZONE_THING_TYPE = new ThingTypeUID(BINDING_ID, "zone");

    public static final Set<ThingTypeUID> BRIDGE_THING_TYPES_UIDS = Collections.singleton(BRIDGE_THING_TYPE);
    public static final Set<ThingTypeUID> ZONE_THING_TYPES_UIDS = Collections.singleton(ZONE_THING_TYPE);

    // For the backwards compatibility...
    public final static ThingTypeUID THING_TYPE_SPEAKER = BRIDGE_THING_TYPE;

    // List of all Channel ids
    public final static String POWER = "power";
    public final static String VOLUME = "volume";
    public final static String MUTE = "mute";
    public final static String INPUT = "input";
    public final static String MODEL_NAME = "model_name";
    public final static String ALBUM_ART = "album_art";

    // Configuration constants
    public static class Configs {
        public static final String CONFIG_HOST_NAME = "host";
        public static final String CONFIG_ZONE = "zone";
    }

    /**
     * The names of this enum are part of the protocols!
     * Receivers have different capabilities, some have 2 zones, some up to 4.
     * Every receiver has a "Main_Zone".
     */
    public enum Zone {
        Main_Zone,
        Zone_2,
        Zone_3,
        Zone_4
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

    public static final String UPNP_TYPE = "MediaRenderer";
    public static final String UPNP_MANUFACTURER = "YAMAHA";

}
