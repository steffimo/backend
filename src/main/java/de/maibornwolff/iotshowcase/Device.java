package de.maibornwolff.iotshowcase;

public class Device {
    private String deviceID;
    private String sharedAccessKey;

    public Device(String deviceID, String sharedAccessKey) {
        this.deviceID = deviceID;
        this.sharedAccessKey = sharedAccessKey;
    }

    public String getDeviceID() {
        return deviceID;
    }
}