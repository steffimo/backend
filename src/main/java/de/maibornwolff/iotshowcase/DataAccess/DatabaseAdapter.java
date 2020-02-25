package de.maibornwolff.iotshowcase.DataAccess;

import de.maibornwolff.iotshowcase.Message;
import de.maibornwolff.iotshowcase.PlayerScore;

import java.sql.*;
import java.util.List;

public class DatabaseAdapter {
    //Azure SQL-Datenbank
        /*execute statements manually
        CREATE TABLE AccelerometerData (
            SessionID varchar(50),
            DeviceID varchar(50),
            DeviceCoordinateX smallint,
            DeviceCoordinateY smallint,
            DeviceCoordinateZ smallint,
            SendingTimestamp bigint
        );

        CREATE TABLE DeviceIDPool (
            DeviceID varchar(50) PRIMARY KEY,
            SharedAccessKey varchar(50),
            Used bit
        );

        INSERT INTO [dbo].[DeviceIDPool](DeviceID, SharedAccessKey, Used)
        VALUES ('TestDeviceWeb', 'oGCxm9N23jyDtq9EC9LAoqR95PrSEg5uzwpXX9o6R0E=', 'false') ,
        ('TestDeviceWeb1', 'cVrfuANNJk7dTTlL5aOH3oD44n+XGv0sbiRy7SkC4IQ=', 'false'),
        ('TestDeviceWeb2', '0BP7YbFP4N4yIx/FQulGE4t7bLiYTMyTdzkqcJwQqik=', 'false'),
        ('TestDeviceWeb3', 'GyAxo606ng4aW7iQoTH5A7Rls7p4MJN3vviMqni3CeI=', 'false'),
        ('TestDeviceWeb4', 'TjMPEFcIR21xqJyAKwPi6MnOAsCSV0JYMrSGE2xUXU8=', 'false'),
        ('TestDeviceWeb5', 'Tl8C0sP1viGJ+QQIOfzLElqAydqlDlGndjqt4koa8TI=', 'false'),
        ('TestDeviceWeb6', 'Q1DtjJJnc3X6v6cFz4xqObyNVsKKqA2Txl5zphaGLnU=', 'false'),
        ('TestDeviceWeb7', 'HXevZn3m6sd5nDEBx7Y709JztMm/YjrOAuzF1PaENRg=', 'false'),
        ('TestDeviceWeb8', '5wI0ZnAFM5VqpE5gtHrc/ElfSGLtQB2+MSpomY/vHBA=', 'false'),
        ('TestDeviceWeb9', 'Z+rYJQzWFrGECedErffHZ9XARJzW1x5AIlCwwNokVBE=', 'false'),
        ('TestDeviceWeb10', 'Hx4YU03DZjORLWD7VE3g2exA7utdBrUrHQeD4kvP6po=', 'false'),
        ('TestDeviceWeb11', 'gJy6yBJ8GZ7yueXihx47/39mXlL73wFOhLPkkc3aN1g=', 'false'),
        ('TestDeviceWeb12', 'aDCXI7TfDHWo9QE7TUINAnGEpuqBXGqGybhOea3U1Wo=', 'false'),
        ('TestDeviceWeb13', 'vfoouVaHI/gYXw/Wm/2et6S/1iXU+KBx6hG7NdTgtzc=', 'false'),
        ('TestDeviceWeb14', '0/tGQ92S+8fWf0GaH2xzrHxBeodKtAM7tjl+ZhuBv2A=', 'false'),
        ('TestDeviceWeb15', 'eJvZK5Wu0Ub10Qpni4Xyn9PSpMLLhkLGXwiiQqeXv8Y=', 'false'),
        ('TestDeviceWeb16', '+9Xz5en7KtLjC8hKAwmJHBgXxezEPgL8ZXAqhMN86PA=', 'false'),
        ('TestDeviceWeb17', 'cXSF0lCSm7Q5FWQS+eFJT8GMvGQQNDJTYENnDjBp51k=', 'false'),
        ('TestDeviceWeb18', 'D2qD6J5MfVXqKtyh42tB5aNwTYqauNjz/3FKUc7WQLg=', 'false'),
        ('TestDeviceWeb19', 'R2ZA1nRAgnyGxc8Zyf8qMk7lB2UPefv3UrXestplUKE=', 'false'),
        ('TestDeviceWeb20', 'TdOU2W6NED4+gVvuToIrAS1QpPxhINl+SMt0FTKFnDk=', 'false'),
        ('TestDeviceWeb21', 'mRSYBrmUXqJYlkdvy2OTyLV+uSV0/oEarH73Tm1hWfo=', 'false'),
        ('TestDeviceWeb22', 'shi6bnE+ZrZZCggntSdssSyvHF9dfqbDnC8LZYeNXWI=', 'false'),
        ('TestDeviceWeb23', 'rmTubym39I7cnnS3L+vucUguuYsNanrYB+0IEu2EfRQ=', 'false'),
        ('TestDeviceWeb24', 'uM+EKmXMv0xqPfS6G/2oPh1+nkO86pmoUgft1WofJx0=', 'false'),
        ('TestDeviceWeb25', 'oAzON8HzjINhZe8DDS5i02a4hAujRmB51KVLsnh1s+I=', 'false');


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
        connection.close();
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
