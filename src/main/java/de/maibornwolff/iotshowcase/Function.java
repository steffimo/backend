package de.maibornwolff.iotshowcase;

import com.google.gson.Gson;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import de.maibornwolff.iotshowcase.DataAccess.DatabaseAdapter;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

        //TODO neue Klasse Message? mit Filter/Sortieren? und *100? Und createInsertStatement(connection, message)?
        message = message.substring(1, message.length() - 1);
        JSONObject msg = new JSONObject(message);
        String sessionID = msg.getString("sessionID");
        String deviceID = msg.getString("deviceID");
        double deviceCoordinateX = msg.getDouble("deviceCoordinateX");
        double deviceCoordinateY = msg.getDouble("deviceCoordinateY");
        double deviceCoordinateZ = msg.getDouble("deviceCoordinateZ");
        int sendingTimestamp = msg.getInt("sendingTimestamp");

        //sortieren/filtern
        /*if (deviceCoordinateX==0 && deviceCoordinateY==0 && deviceCoordinateZ==0){
            return;
        }*/
        deviceCoordinateX = deviceCoordinateX * 100;
        deviceCoordinateY = deviceCoordinateY * 100;
        deviceCoordinateZ = deviceCoordinateZ * 100;

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            databaseAdapter.createInsertStatement(connection, sessionID, deviceID, deviceCoordinateX, deviceCoordinateY, deviceCoordinateZ, sendingTimestamp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FunctionName("DataAnalytics")
    public HttpResponseMessage fetchToOperate(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            ResultSet resultSet = databaseAdapter.createSelectStatementForHighscoreOverall(connection);
            ResultSetHandler resultSetHandler = new ResultSetHandler();
            Gson gson = new Gson();
            String json = gson.toJson(resultSetHandler.getPlayerScoreList(resultSet));
            connection.close();
            return request.createResponseBuilder(HttpStatus.OK).body(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution failed").build();
        }
    }

    @FunctionName("DataAnalyticsSession")
    public HttpResponseMessage fetchToOperate2(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            ResultSet resultSet = databaseAdapter.createSelectStatementForHighscoreSession(connection);
            ResultSetHandler resultSetHandler = new ResultSetHandler();
            Gson gson = new Gson();
            String json = gson.toJson(resultSetHandler.getPlayerScoreList(resultSet));
            connection.close();
            return request.createResponseBuilder(HttpStatus.OK).body(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution failed").build();
        }
    }

}


//Notiz: ca. 20 Incomings pro Gerät? iPhone
//ca 71 Incomings von Huawei
//https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html
//https://www.powerslides.com/powerpoint-industry/technology-templates/cloud-software-architecture/