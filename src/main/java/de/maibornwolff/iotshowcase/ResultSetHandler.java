package de.maibornwolff.iotshowcase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetHandler {

    public List<PlayerScore> getPlayerScoreList(ResultSet resultSet) throws SQLException {
        List<PlayerScore> playerScoreList = new ArrayList<>();
        while (resultSet.next()) {
            Double energy = resultSet.getDouble("Energy");
            String deviceID = resultSet.getString("DeviceID");
            String sessionID = resultSet.getString("SessionID");
            playerScoreList.add(new PlayerScore(energy, deviceID, sessionID));
        }
        return playerScoreList;
    }
}
