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
            String deviceId = resultSet.getString("DeviceID");
            String sessionId = resultSet.getString("SessionID");
            //int sendingTimestamp = resultSet.getInt("SendingTimestamp");
            playerScoreList.add(new PlayerScore(energy, deviceId, sessionId, 1));
        }
        return playerScoreList;
    }
}
