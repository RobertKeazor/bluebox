package com.mac.bluebox.model;

/**
 * Created by anyer on 6/27/15.
 */
public class DeviceModel {
    private final String name;
    private final String mac;

    public DeviceModel(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }
}
