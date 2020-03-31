package de.maibornwolff.iotshowcase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultSetHandler {

    public List<PlayerScore> getPlayerScoreListOverall(ResultSet resultSet) throws SQLException {
        List<PlayerScore> playerScoreList = new ArrayList<>();
        while (resultSet.next()) {
            Double energy = resultSet.getDouble("Energy");
            String deviceID = resultSet.getString("DeviceID");
            String sessionID = resultSet.getString("SessionID");
            playerScoreList.add(new PlayerScore(energy, deviceID, sessionID));
        }
        return playerScoreList;
    }

    public List<PlayerScore> getPlayerScoreListSession(ResultSet resultSet) throws SQLException {
        List<PlayerScore> playerScoreList = new ArrayList<>();
        while (resultSet.next()) {
            Double nonNormedEnergy = resultSet.getDouble("Energy");
            System.out.println(nonNormedEnergy);
            Integer messageNumber = resultSet.getInt("MessageNumber");
            System.out.println(messageNumber);
            Double energy = (nonNormedEnergy/messageNumber)*(nonNormedEnergy/messageNumber);
            System.out.println(energy);
            String deviceID = resultSet.getString("DeviceID");
            String sessionID = resultSet.getString("SessionID");
            playerScoreList.add(new PlayerScore(energy, deviceID, sessionID));
        }
        Collections.sort(playerScoreList);
        System.out.println(playerScoreList.toString());
        return playerScoreList;
    }

    public Device getDeviceIDWithKey(ResultSet resultSet) throws SQLException {
        resultSet.next();
        String deviceID = resultSet.getString("DeviceID");
        String sharedAccessKey = resultSet.getString("SharedAccessKey");
        return new Device(deviceID, sharedAccessKey);
    }

}
