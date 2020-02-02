package de.maibornwolff.iotshowcase.DataAccess;

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
         */

    public Connection connectToDatabase() throws SQLException {
        String hostName = "showcase-iot-data-server.database.windows.net";
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

    public void createInsertStatement(Connection connection, String sessionID, String deviceID, double deviceCoordinateX, double deviceCoordinateY, double deviceCoordinateZ, long sendingTimestamp) throws SQLException {
        System.out.println("Query data example:");
        System.out.println("=========================================");
        String insertSql = "INSERT INTO [dbo].[AccelerometerData](SessionID, DeviceID, DeviceCoordinateX, DeviceCoordinateY, DeviceCoordinateZ, SendingTimestamp)" +
                "VALUES ('" + sessionID + "','" + deviceID + "'," + deviceCoordinateX + "," + deviceCoordinateY + "," + deviceCoordinateZ + ",'" + sendingTimestamp + "')";
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
                "ORDER BY SessionID, Energy DESC";

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
                "WHERE SessionID ="+"'"+session+"'\n" +
                "GROUP BY DeviceID\n" +
                "ORDER BY Energy DESC";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSql);
        return resultSet;
    }
}
