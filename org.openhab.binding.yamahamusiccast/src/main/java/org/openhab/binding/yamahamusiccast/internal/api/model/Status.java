package org.openhab.binding.yamahamusiccast.internal.api.model;

public class Status extends Response {
    public final static String url = "/YamahaExtendedControl/v2/main/getStatus";

    public String getPower() {
        return power;
    }

    public Integer getVolume() {
        return volume;
    }

    public boolean isMute() {
        return mute;
    }

    public Integer getMax_volume() {
        return max_volume;
    }

    public String getInput() {
        return input;
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

}
