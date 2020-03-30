package de.maibornwolff.iotshowcase;

import org.json.JSONObject;

public class Message {
    private String sessionID;
    private String deviceID;
    private double deviceCoordinateX;
    private double deviceCoordinateY;
    private double deviceCoordinateZ;
    private long sendingTimestamp;

    public Message(String sessionID, String deviceID, double deviceCoordinateX, double deviceCoordinateY, double deviceCoordinateZ, long sendingTimestamp) {
        this.sessionID = sessionID;
        this.deviceID = deviceID;
        this.deviceCoordinateX = deviceCoordinateX;
        this.deviceCoordinateY = deviceCoordinateY;
        this.deviceCoordinateZ = deviceCoordinateZ;
        this.sendingTimestamp = sendingTimestamp;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public double getDeviceCoordinateX() {
        return deviceCoordinateX;
    }

    public double getDeviceCoordinateY() {
        return deviceCoordinateY;
    }

    public double getDeviceCoordinateZ() {
        return deviceCoordinateZ;
    }

    public long getSendingTimestamp() {
        return sendingTimestamp;
    }
}
