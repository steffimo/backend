package de.maibornwolff.iotshowcase;

public class PlayerScore implements Comparable{
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

    @Override
    public int compareTo(Object o) {
        PlayerScore score  = (PlayerScore) o;
        if (this.getEnergy() > score.getEnergy()) {
            return -1;
        } else if (getEnergy() < score.getEnergy()) {
            return 1;
        }
        return 0;
    }
}
