package de.maibornwolff.iotshowcase;

public class DeviceDocument {
    private String sessionID;
    private String deviceID;
    private double deviceCoordinateX;
    private double deviceCoordinateY;
    private double deviceCoordinateZ;
    private String timestamp;

    public DeviceDocument(){
    }

    public String getDeviceID() {
        return deviceID;
    }
}
