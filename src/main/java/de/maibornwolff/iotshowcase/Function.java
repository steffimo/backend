package de.maibornwolff.iotshowcase;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.json.JSONObject;

import java.sql.*;


/**
 * DataIngestion: Azure Function with IoTHub and CosmosDB
 * DataAnalytics: Azure Function with CosmosDB and HttpTrigger
 */
public class Function {

    @FunctionName("DataIngestion")
    public void transferToOperate(
            @EventHubTrigger(name = "msg",
                    eventHubName = "myeventhubname",
                    connection = "myconnvarname") String message,
            final ExecutionContext context) {
        context.getLogger().info(message);
        //example message [{"sessionID":"s-id1274168885","deviceID":"d-id2047831712","deviceCoordinateX":14.601536193152906,"deviceCoordinateY":5.84587092154832,"deviceCoordinateZ":7.0314324375097375,"timestamp":"ddmmyyyy"}]

        //sortieren/filtern
        //TODO Zeitraum 10 Sekunden im Frontend!
        //TODO Überprüfung der Validität der Werte z.B. von X,Y,Z-Koords
        message = message.substring(1, message.length() - 1);
        JSONObject msg = new JSONObject(message);
        String sessionID = msg.getString("sessionID");
        String deviceID = msg.getString("deviceID");
        double deviceCoordinateX = msg.getDouble("deviceCoordinateX");
        double deviceCoordinateY = msg.getDouble("deviceCoordinateY");
        double deviceCoordinateZ = msg.getDouble("deviceCoordinateZ");
        String timestamp = msg.getString("timestamp");


        //Azure SQL-Datenbank

        // Connect to database
        String hostName = "showcase-iot-data-server.database.windows.net";
        String dbName = "ShowcaseIoTData";
        String user = "showcase-chef";
        String password = "IoT4urWork";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);

            System.out.println("Query data example:");
            System.out.println("=========================================");

            //Create and execute a INSERT SQL statement
            String insertSql = "INSERT INTO [SalesLT].[Product]( [Name], [ProductNumber], [Color], [ProductCategoryID], [StandardCost], [ListPrice], [SellStartDate] )" +
                    "VALUES ('myNewProduct',123456789,'NewColor',1,100,100,GETDATE() );";


            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(insertSql)) {

                System.out.println("Element inserted");
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @FunctionName("DataAnalytics")
    public void fetchToOperate() {
        // Connect to database
        String hostName = "showcase-iot-data-server.database.windows.net";
        String dbName = "ShowcaseIoTData";
        String user = "showcase-chef";
        String password = "IoT4urWork";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);

            System.out.println("Query data example:");
            System.out.println("=========================================");

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT TOP 20 pc.Name as CategoryName, p.name as ProductName "
                    + "FROM [SalesLT].[ProductCategory] pc "
                    + "JOIN [SalesLT].[Product] p ON pc.productcategoryid = p.productcategoryid";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement
                System.out.println("Top 20 categories:");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + " "
                            + resultSet.getString(2));
                }
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO Maximum berechnen
        //TODO Daten an Frontend zur Auswertung weitergeben (HTTP)
    }*/
}


