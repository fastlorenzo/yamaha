package org.openhab.binding.yamahamusiccast.internal.api.model;

public class PlayInfo extends Response {
    public static final String url = "/YamahaExtendedControl/v2/netusb/getPlayInfo";

    protected String albumart_url;

    protected String album;

    protected String track;

    public String getAlbumart_url() {
        return albumart_url;
    }

    public String getAlbum() {
        return album;
    }

    public String getTrack() {
        return track;
    }

}
