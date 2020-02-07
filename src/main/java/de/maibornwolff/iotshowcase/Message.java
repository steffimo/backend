package de.maibornwolff.iotshowcase;

import org.json.JSONObject;

public class Message {
   private String sessionID;

    private   String deviceID;
   private double deviceCoordinateX;
   private double deviceCoordinateY;
  private   double deviceCoordinateZ;
    private long sendingTimestamp;
    private boolean isValid = true;

    public Message(String message){
        JSONObject msg = new JSONObject(message);
        String sessionID = msg.getString("sessionID");
        String deviceID = msg.getString("deviceID");
        double deviceCoordinateX = msg.getDouble("deviceCoordinateX");
        double deviceCoordinateY = msg.getDouble("deviceCoordinateY");
        double deviceCoordinateZ = msg.getDouble("deviceCoordinateZ");
        if (deviceCoordinateX==0 && deviceCoordinateY==0 && deviceCoordinateZ==0){
            this.isValid = false;
        }
        long sendingTimestamp = msg.getLong("sendingTimestamp");
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

    public boolean isValid() {
        return isValid;
    }
}
