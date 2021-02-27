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
    protected Integer maxVolume;
    protected String input;
    protected boolean distributionEnable;
    protected String soundProgram;
    protected boolean clearVoice;
    protected Integer subwooferVolume;
    protected String linkControl;
    protected String linkAudioDelay;
    protected String linkAudioQuality;
    protected Integer disableFlags;

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

    public Integer getMaxVolume() {
        return maxVolume;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isDistributionEnable() {
        return distributionEnable;
    }

    public String getSoundProgram() {
        return soundProgram;
    }

    public boolean isClearVoice() {
        return clearVoice;
    }

    public Integer getSubwooferVolume() {
        return subwooferVolume;
    }

    public String getLinkControl() {
        return linkControl;
    }

    public String getLinkAudioDelay() {
        return linkAudioDelay;
    }

    public String getLinkAudioQuality() {
        return linkAudioQuality;
    }

    public Integer getDisableFlags() {
        return disableFlags;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getUrl() {
        url = YamahaMusicCastBindingConstants.ROOT_PATH + "/" + zone + path;
        return url;
    }
}
