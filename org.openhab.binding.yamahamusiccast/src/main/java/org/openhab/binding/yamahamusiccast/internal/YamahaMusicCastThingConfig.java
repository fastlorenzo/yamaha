package org.openhab.binding.yamahamusiccast.internal;

public class YamahaMusicCastThingConfig {

    /**
     * RefreshInterval in seconds.
     */
    private int refresh = 10;

    private String host = "yamaha";

    public String getHost() {
        return host;
    }

    public int getRefreshInterval() {
        return refresh;
    }

}
