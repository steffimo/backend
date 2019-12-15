package de.maibornwolff.iotshowcase;

public class SmartphoneData {
    private double deviceCoordinateX;
    private double deviceCoordinateY;
    private double deviceCoordinateZ;
    private String timestamp;

    public void setDeviceCoordinateX(double deviceCoordinateX) {
        this.deviceCoordinateX = deviceCoordinateX;
    }

    public void setDeviceCoordinateY(double deviceCoordinateY) {
        this.deviceCoordinateY = deviceCoordinateY;
    }

    public void setDeviceCoordinateZ(double deviceCoordinateZ) {
        this.deviceCoordinateZ = deviceCoordinateZ;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
