package de.maibornwolff.iotshowcase;

public class PlayerScore {
    private Double energy;
    private String deviceID;
    private String sessionID;

    public PlayerScore(Double energy, String deviceID, String sessionID) {
        this.energy = energy;
        this.deviceID = deviceID;
        this.sessionID = sessionID;
    }

    public Double getEnergy() {
        return energy;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getSessionID() {
        return sessionID;
    }
}
