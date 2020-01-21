package de.maibornwolff.iotshowcase;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.Optional;


/**
 * DataIngestion: Azure Function with IoTHub and SQL Database
 * DataAnalytics: Azure Function with SQL Database and HttpTrigger
 */
public class Function {

    @FunctionName("DataIngestion")
    public void transferToOperate(
            @EventHubTrigger(name = "msg",
                    eventHubName = "myeventhubname",
                    connection = "myconnvarname") String message,
            final ExecutionContext context) {
        context.getLogger().info(message);
        //example message [{"sessionID":"s-id1274168885","deviceID":"d-id2047831712","deviceCoordinateX":14.601536193152906,"deviceCoordinateY":5.84587092154832,"deviceCoordinateZ":7.0314324375097375,"sendingtimestamp":"ddmmyyyy"}]

        //sortieren/filtern
        //TODO Überprüfung der Validität der Werte z.B. von X,Y,Z-Koords
        message = message.substring(1, message.length() - 1);
        JSONObject msg = new JSONObject(message);
        String sessionID = msg.getString("sessionID");
        String deviceID = msg.getString("deviceID");
        double deviceCoordinateX = msg.getDouble("deviceCoordinateX");
        double deviceCoordinateY = msg.getDouble("deviceCoordinateY");
        double deviceCoordinateZ = msg.getDouble("deviceCoordinateZ");
        int sendingTimestamp = msg.getInt("sendingTimestamp");


        //Azure SQL-Datenbank
        /*execute CREATE TABLE statement manually
        CREATE TABLE AccelerometerData (
            SessionID char(50),
            DeviceID char(50),
            DeviceCoordinateX smallint,
            DeviceCoordinateY smallint,
            DeviceCoordinateZ smallint,
            SendingTimestamp bigint
        );
         */

        // Connect to database
        String hostName = "showcase-iot-data-server.database.windows.net";
        String dbName = "IoTShowcaseData";
        String user = "showcase-chef";
        String password = "IoT4urWork";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Successful connection");
            System.out.println("Query data example:");
            System.out.println("=========================================");

            //Create and execute a INSERT SQL statement
            String insertSql = "INSERT INTO [dbo].[AccelerometerData](SessionID, DeviceID, DeviceCoordinateX, DeviceCoordinateY, DeviceCoordinateZ, SendingTimestamp)" +
                    "VALUES ('" + sessionID + "','" + deviceID + "'," + deviceCoordinateX + "," + deviceCoordinateY + "," + deviceCoordinateZ + ",'" + sendingTimestamp + "')";

            Statement statement = connection.createStatement();
            statement.execute(insertSql);
            System.out.println("Element inserted");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FunctionName("DataAnalytics")
    public HttpResponseMessage fetchToOperate(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        // Connect to database
        String hostName = "showcase-iot-data-server.database.windows.net";
        String dbName = "IoTShowcaseData";
        String user = "showcase-chef";
        String password = "IoT4urWork";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection");
            System.out.println("Query data example:");
            System.out.println("=========================================");

            // Create and execute a SELECT SQL statement - be careful of putting an '\n' in the statement, otherwise doesn't work
            String selectSql = "SELECT SessionID, DeviceID, SQRT(SUM(DeviceCoordinateX/100*DeviceCoordinateX/100+DeviceCoordinateY/100*DeviceCoordinateY/100+DeviceCoordinateZ/100*DeviceCoordinateZ/100)) AS Energy\n" +
                    "FROM [dbo].[AccelerometerData]\n" +
                    "GROUP BY SessionID, DeviceID\n" +
                    "ORDER BY SessionID, Energy DESC";
            //String selectSql = "SELECT * FROM [dbo].[AccelerometerData]";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement
                System.out.println("Top Players per session");
                try {
                    JSONArray highscores = toJSON(resultSet);
                    connection.close();
                    return request.createResponseBuilder(HttpStatus.OK).body(highscores).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Convertion to JSON failed").build();
                }
            }
            //TODO System.out.println("Top Players");
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution failed").build();
        }
    }

    public JSONArray toJSON(ResultSet rs) throws SQLException {
        JSONObject jsonObject = new JSONObject();
        //Creating a json array
        JSONArray array = new JSONArray();
        //Inserting ResultSet data into the json object
        while(rs.next()) {
            JSONObject record = new JSONObject();
            //Inserting key-value pairs into the json object
            record.put("Energy", rs.getDouble("Energy"));
            record.put("DeviceID", rs.getString("DeviceID"));
            record.put("SessionID", rs.getString("SessionID"));
            array.put(record);
        }
        jsonObject.put("Players_data", array);
        return array;
    }


}

//https://inneka.com/programming/java/repository-element-was-not-specified-in-the-pom-inside-distributionmanagement-element-or-in-daltdep-loymentrepositoryidlayouturl-parameter/


