package de.maibornwolff.iotshowcase;

public class PlayerScore {
    private Double energy;
    private String deviceId;
    private String sessionId;
    //private int sendingTimestamp;

    public PlayerScore(Double energy, String deviceId, String sessionId, int sendingTimestamp) {
        this.energy = energy;
        this.deviceId = deviceId;
        this.sessionId = sessionId;
        //this.sendingTimestamp = sendingTimestamp;
    }
}
