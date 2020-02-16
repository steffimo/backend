package de.maibornwolff.iotshowcase.DataAccess;

import de.maibornwolff.iotshowcase.Message;

import java.sql.*;

public class DatabaseAdapter {
    //Azure SQL-Datenbank
        /*execute CREATE TABLE statement manually
        CREATE TABLE AccelerometerData (
            SessionID varchar(50),
            DeviceID varchar(50),
            DeviceCoordinateX smallint,
            DeviceCoordinateY smallint,
            DeviceCoordinateZ smallint,
            SendingTimestamp bigint
        );

         CREATE TABLE DeviceIDPool (
            DeviceID varchar(50),
            SharedAccessKey varchar(50),
            Used bit
        );

        INSERT INTO [dbo].[DeviceIDPool](DeviceID, SharedAccessKey, Used)
        VALUES ('TestDeviceWeb', 'ZYwl6LA2+OlxOKWOPqjhx1qDFR+2oNZFavQuQp/t1Ao=', 'false') ,
        ('TestDeviceWeb1', '', 'false'),
        ('TestDeviceWeb2', '', 'false'),
        ('TestDeviceWeb3', '', 'false'),
        ('TestDeviceWeb4', '', 'false'),
        ('TestDeviceWeb5', '', 'false'),
        ('TestDeviceWeb6', '', 'false'),
        ('TestDeviceWeb7', '', 'false'),
        ('TestDeviceWeb8', '', 'false'),
        ('TestDeviceWeb9', '', 'false'),
        ('TestDeviceWeb10', '', 'false'),
        ('TestDeviceWeb11', '', 'false'),
        ('TestDeviceWeb12', '', 'false'),
        ('TestDeviceWeb13', '', 'false'),
        ('TestDeviceWeb14', '', 'false');
         */

    public Connection connectToDatabase() throws SQLException {
        //String hostName = "showcase-server1.database.windows.net";
        String hostName = "showcase-server.database.windows.net";
        String dbName = "IoTShowcaseData";
        String user = "showcase-chef";
        //String password = "Workshop4u";
        String password = "IoT4urWork";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        connection = DriverManager.getConnection(url);
        System.out.println("Successful connection");
        return connection;
    }

    public void createInsertStatement(Connection connection, Message message) throws SQLException {
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
        String selectSql = "SELECT SessionID, DeviceID, SQRT(SUM(DeviceCoordinateX/100*DeviceCoordinateX/100+DeviceCoordinateY/100*DeviceCoordinateY/100+DeviceCoordinateZ/100*DeviceCoordinateZ/100)) AS Energy\n" +
                "FROM [dbo].[AccelerometerData]\n" +
                "GROUP BY SessionID, DeviceID\n" +
                "ORDER BY Energy DESC";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSql);
        return resultSet;
    }

    public ResultSet createSelectStatementForHighscoreSession(Connection connection, String session) throws SQLException {
        System.out.println("Query data example:");
        System.out.println("=========================================");

        // Create and execute a SELECT SQL statement - be careful of putting an '\n' in the statement, otherwise doesn't work
        String selectSql = "SELECT DeviceID, SQRT(SUM(DeviceCoordinateX/100*DeviceCoordinateX/100+DeviceCoordinateY/100*DeviceCoordinateY/100+DeviceCoordinateZ/100*DeviceCoordinateZ/100)) AS Energy\n" +
                "FROM [dbo].[AccelerometerData]\n" +
                "WHERE SessionID =" + "'" + session + "'\n" +
                "GROUP BY DeviceID\n" +
                "ORDER BY Energy DESC";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSql);
        return resultSet;
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
}
