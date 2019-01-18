/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.yamahamusiccast.internal.api.model;

import org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants;

/**
 * Status
 *
 * @author Dries Decock - Initial contribution
 */
public class Status extends Response {
    protected String power;
    protected Integer volume;
    protected boolean mute;
    protected Integer max_volume;
    protected String input;
    protected boolean distribution_enable;
    protected String sound_program;
    protected boolean clear_voice;
    protected Integer subwoofer_volume;
    protected String link_control;
    protected String link_audio_delay;
    protected String link_audio_quality;
    protected Integer disable_flags;

    public String zone = "main";
    public final static String path = "/getStatus";
    public String url;
    
    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public Integer getMax_volume() {
        return max_volume;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isDistribution_enable() {
        return distribution_enable;
    }

    public String getSound_program() {
        return sound_program;
    }

    public boolean isClear_voice() {
        return clear_voice;
    }

    public Integer getSubwoofer_volume() {
        return subwoofer_volume;
    }

    public String getLink_control() {
        return link_control;
    }

    public String getLink_audio_delay() {
        return link_audio_delay;
    }

    public String getLink_audio_quality() {
        return link_audio_quality;
    }

    public Integer getDisable_flags() {
        return disable_flags;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getUrl() {
        url = YamahaMusicCastBindingConstants.ROOT_PATH + "/" + zone + path;
        return url;
    }
}