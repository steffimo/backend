package de.maibornwolff.iotshowcase;

import java.util.ArrayList;

public class DeviceDocument {
    private String sessionID;
    private String deviceID;
    private ArrayList<SmartphoneData> smartphoneData;

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setSmartphoneData(ArrayList<SmartphoneData> smartphoneData) {
        this.smartphoneData = smartphoneData;
    }

    public String getDeviceID() {
        return deviceID;
    }


}
