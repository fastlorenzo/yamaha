package org.openhab.binding.yamahamusiccast.internal.api.model;

public class DeviceInfo extends Response {

    public final static String url = "/YamahaExtendedControl/v2/system/getDeviceInfo";

    protected String model_name;

    protected String destination;

    protected String device_id;

    protected float system_version;

    protected float api_version;

    protected String netmodule_version;

    public static String getUrl() {
        return url;
    }

    public String getModel_name() {
        return model_name;
    }

    public String getDestination() {
        return destination;
    }

    public String getDevice_id() {
        return device_id;
    }

    public float getSystem_version() {
        return system_version;
    }

    public float getApi_version() {
        return api_version;
    }

    public String getNetmodule_version() {
        return netmodule_version;
    }

}
