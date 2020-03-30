package de.maibornwolff.iotshowcase.DataAccess;

import de.maibornwolff.iotshowcase.Message;
import de.maibornwolff.iotshowcase.PlayerScore;

import java.sql.*;
import java.util.List;

public class DatabaseAdapter {
    //Azure SQL-Datenbank
        /*execute statements manually
        CREATE TABLE DeviceIDPool (
            DeviceID varchar(50) PRIMARY KEY,
            SharedAccessKey varchar(50),
            Used bit
        );

        INSERT INTO [dbo].[DeviceIDPool](DeviceID, SharedAccessKey, Used)
                VALUES ('TestDeviceWeb', '7dMOA5fcSKQODmGDs+VzyzjKp1DHb8hlvJGXm2hqEr8=', 'false') ,
                ('TestDeviceWeb1', 'DzTBIWIIjZi16w2hW4D/MgusoxMPElsFJ7wiiWJnkrk=', 'false'),
                ('TestDeviceWeb2', 'N5fDaN2VL8Rj6xJfpT36mPa2CZ2wEsQhH0Djt4Ps8xM=', 'false'),
                ('TestDeviceWeb3', 'K8ylEcouEtmU9JtKGRoGZ6LEJtYDy/1pXU+K1SZrWug=', 'false'),
                ('TestDeviceWeb4', 'qh5OCtSWdA9bzRxapXfosEMaCG0oO/BTWyUhZQ3r6lw=', 'false'),
                ('TestDeviceWeb5', 'x1X6u99NwUnnqAiIYsXPgBnkx0n6JYw7qOIGIoIMp1w=', 'false'),
                ('TestDeviceWeb6', 'rtKgNV3IUYXyXmCHoJDNqi3Y/PnZ8D4ONYcEtU/GgFI=', 'false'),
                ('TestDeviceWeb7', 'yKGsWoUInbsYXk0t6/RioSatHXT1WqH83o6Dfrihej8=', 'false'),
                ('TestDeviceWeb8', '5lSR/QEY3c59O83EstFA7mejO0N5rHM7Ug1lrRguX/4=', 'false'),
                ('TestDeviceWeb9', '3NakAQfnHfBpCMqv9Jz3HSVftCVNINI6RwXzzp9ZpfE=', 'false'),
                ('TestDeviceWeb10', 'xfGEfNKenzPfYyII2Jg2saGt8cHXzv+vkiEa3FwU4oA=', 'false'),
                ('TestDeviceWeb11', 'qGduvRmVxFvdMOmK935NYcxeAlLeyWno3yOJB/FXVBM=', 'false'),
                ('TestDeviceWeb12', 'fL5wlXvNgfUi33bGbDDmo4zHmhfS+Fgo9pZRgRIP6zc=', 'false'),
                ('TestDeviceWeb13', 'R61mEdu3TjWbYO3q8G+/iHv+hNkhdNo7fDMXmFBzopw=', 'false'),
                ('TestDeviceWeb14', '8AcH/Vh03DCOC2knUnu4DhPffDalfj3WPraFQnr31w0=', 'false'),
                ('TestDeviceWeb15', 'cXyI49v1Hq7sUzo8V5jv5lxpvOFqLsP3083EBY7E1EI=', 'false'),
                ('TestDeviceWeb16', 'OFwoP5SWEaWUqKtcQM9c3unMevIbs0/CBq8OaEwDH3I=', 'false'),
                ('TestDeviceWeb17', '+so9DTVTCk6IO4I4cH0YTiGCMaO1gWIoUmy7+Vl4jTY=', 'false'),
                ('TestDeviceWeb18', 'kRTp0U9GMAu2kgoNodk3IpNzeg7J/MhcoV21U8P1ryA=', 'false'),
                ('TestDeviceWeb19', 'lO6IkisELwSV8oXbo1tOcqBzoB2FbkiuqVU1TDk4xQY=', 'false'),
                ('TestDeviceWeb20', 'TCQKHqnmPqJ4p4cNCzNEfveQ3fJ6nYYp3tpkIly1Rm8=', 'false'),
                ('TestDeviceWeb21', 'sjQw8aJA2HDhWmGSeQqjtcHlMNtpxyEioUvrtsIoN44=', 'false'),
                ('TestDeviceWeb22', '9lFsGBAhYYaz6wuSbk8jmhRC4BYD2xvRb0wichpRdkU=', 'false'),
                ('TestDeviceWeb23', 'xCRPtjJEFosmsTYMhXWK7qz6rVNnduqrHffCCb2aPds=', 'false'),
                ('TestDeviceWeb24', 'REX+q8mFDg1/Hw65N/nYPdhO4w54Pycj0XIjJPQVy4M=', 'false'),
                ('TestDeviceWeb25', 'O8K0fbAzURVlaiuGrarXT9x6fqZcGSUbjWwqz4fJJx8=', 'false');

        CREATE TABLE AccelerometerData (
                    SessionID varchar(50),
                    DeviceID varchar(50),
                    DeviceCoordinateX smallint,
                    DeviceCoordinateY smallint,
                    DeviceCoordinateZ smallint,
                    SendingTimestamp bigint
                );

        CREATE TABLE Highscores (
                    DeviceID varchar(50),
                    Energy float(24),
                    SessionID varchar(50)
                );
         */

    public Connection connectToDatabase() throws SQLException {
        String hostName = "showcase-server.database.windows.net";
        String dbName = "IoTShowcaseData";
        String user = "showcase-chef";
        String password = "IoT4urWork";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        connection = DriverManager.getConnection(url);
        System.out.println("Successful connection");
        return connection;
    }

    public void createInsertStatementForAccelerometerData(Connection connection, Message message) throws SQLException {
        System.out.println("Query data example:");
        System.out.println("=========================================");
        String insertSql = "INSERT INTO [dbo].[AccelerometerData](SessionID, DeviceID, DeviceCoordinateX, DeviceCoordinateY, DeviceCoordinateZ, SendingTimestamp)" +
                "VALUES ('" + message.getSessionID() + "','" + message.getDeviceID() + "'," + message.getDeviceCoordinateX() * 100 + "," + message.getDeviceCoordinateY() * 100 + "," + message.getDeviceCoordinateZ() * 100 + ",'" + message.getSendingTimestamp() + "')";
        Statement statement = null;
        statement = connection.createStatement();
        statement.execute(insertSql);
        System.out.println("Element inserted");
    }

    public ResultSet createSelectStatementForHighscoreOverall(Connection connection) throws SQLException {
        System.out.println("Query data example:");
        System.out.println("=========================================");

        // Create and execute a SELECT SQL statement - be careful of putting an '\n' in the statement, otherwise doesn't work
        String selectSql = "SELECT * FROM [dbo].[Highscores]\n" +
                "ORDER BY Energy DESC";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSql);
        return resultSet;
    }

    public ResultSet createSelectStatementForHighscoreSession(Connection connection, String session, String beginningTimestamp) throws SQLException {
        System.out.println("Query data example:");
        System.out.println("=========================================");

        // Create and execute a SELECT SQL statement - be careful of putting an '\n' in the statement, otherwise doesn't work
        String selectSql = "SELECT DeviceID, SQRT(SUM(DeviceCoordinateX/100*DeviceCoordinateX/100+DeviceCoordinateY/100*DeviceCoordinateY/100+DeviceCoordinateZ/100*DeviceCoordinateZ/100)) AS Energy, SessionID\n" +
                "FROM [dbo].[AccelerometerData]\n" +
                "WHERE SessionID =" + "'" + session + "' AND SendingTimestamp >= "+beginningTimestamp+" AND SendingTimestamp <= "+beginningTimestamp+10000+"\n" +
                "GROUP BY SessionID, DeviceID\n" +
                "ORDER BY Energy DESC";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSql);
        return resultSet;
    }

    public void createInsertHighscoresTable(Connection connection, List<PlayerScore> playerScoreList) throws SQLException {
        String values = "";
        for (PlayerScore playerScore : playerScoreList) {
            String value = "('" + playerScore.getDeviceID() + "', " + playerScore.getEnergy() + ", '" + playerScore.getSessionID() + "'),";
            values = values.concat(value);
        }
        values = values.substring(0, values.length() - 1);
        System.out.println("Values-String: " + values);
        String insertSql = "INSERT INTO [dbo].[Highscores] (DeviceID, Energy, SessionID) " + "VALUES " + values;

        Statement statement = connection.createStatement();
        statement.execute(insertSql);
        System.out.println("Session " + playerScoreList.get(0).getSessionID() + " inserted in HighscoresTable");
        connection.close();
    }

    public ResultSet createSelectStatementForDeviceID(Connection connection) throws SQLException {
        System.out.println("Getting deviceID with key:");
        System.out.println("=========================================");

        // Create and execute a SELECT SQL statement - be careful of putting an '\n' in the statement, otherwise doesn't work
        String selectSql = "SELECT TOP (1) DeviceID, SharedAccessKey\n" +
                "FROM [dbo].[DeviceIDPool]\n" +
                "WHERE Used = 'False'";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSql);
        return resultSet;
    }

    public void updateDeviceIDPool(Connection connection, String deviceID, boolean usedState) throws SQLException {
        String updateSql = "UPDATE [dbo].[DeviceIDPool] \n" +
                "SET Used = '" + usedState + "'\n" +
                "WHERE DeviceID='" + deviceID + "'";

        Statement statement = connection.createStatement();
        statement.execute(updateSql);
        System.out.println("UsedState from " + deviceID + " updated");
        connection.close();
    }

    public void createDeleteStatementForAccelerometerData(Connection connection) throws SQLException {
        String deleteSql= "DELETE FROM [dbo].[AccelerometerData]";
        Statement statement = connection.createStatement();
        statement.execute(deleteSql);
        System.out.println("AccelerometerData deleted");
        connection.close();
    }
}
