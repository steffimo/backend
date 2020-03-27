package de.maibornwolff.iotshowcase;

import com.google.gson.Gson;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.signalr.SignalRConnectionInfo;
import com.microsoft.azure.functions.signalr.SignalRMessage;
import com.microsoft.azure.functions.signalr.annotation.SignalRConnectionInfoInput;
import com.microsoft.azure.functions.signalr.annotation.SignalROutput;
import de.maibornwolff.iotshowcase.DataAccess.DatabaseAdapter;

import java.sql.*;
import java.util.List;
import java.util.Optional;


/**
 * GetDeviceID: Azure Function for managing deviceID for IoTHub, getting an unused deviceID.
 * ResetDeviceID: Azure Function for managing deviceID for IoTHub, resetting an used deviceID.
 * negotiate: Azure Function with connection information for SignalR Service
 * DataIngestion: Azure Function with IoTHub and SQL Database
 * DataAnalytics: Azure Function with SQL Database and HttpTrigger for overall highscore
 * DataAnalyticsSession: Azure Function with SQL Database and HttpTrigger for session highscore
 * DeleteOldAccelerometerData: Azure Function with SQL Database and HttpTrigger for refreshing database table AccelerometerData
 */
public class Function {

    @FunctionName("GetDeviceID")
    public HttpResponseMessage getDeviceIDFromPool(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request for deviceID.");

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            ResultSet resultSet = databaseAdapter.createSelectStatementForDeviceID(connection);
            ResultSetHandler resultSetHandler = new ResultSetHandler();
            Gson gson = new Gson();
            Device device = resultSetHandler.getDeviceIDWithKey(resultSet);
            String json = gson.toJson(device);
            databaseAdapter.updateDeviceIDPool(connection, device.getDeviceID() , true);
            return request.createResponseBuilder(HttpStatus.OK).body(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution for getting deviceID failed").build();
        }
    }

    @FunctionName("ResetDeviceID")
    public HttpResponseMessage resetDeviceIDFromPool(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request for resetting deviceID.");
        String deviceID = request.getQueryParameters().get("deviceID");
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            databaseAdapter.updateDeviceIDPool(connection, deviceID, false);
            return request.createResponseBuilder(HttpStatus.OK).body("deviceID "+deviceID + " successfully reset").build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution for resetting failed").build();
        }
    }


    @FunctionName("negotiate")
    public SignalRConnectionInfo negotiate(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> req,
            @SignalRConnectionInfoInput(
                    name = "connectionInfo",
                    hubName = "iotData") SignalRConnectionInfo connectionInfo) {
        return connectionInfo;
    }


    @FunctionName("DataIngestion")
    @SignalROutput(name = "$return", hubName = "iotData")
    public SignalRMessage transferToOperate(
            @EventHubTrigger(name = "msg",
                    eventHubName = "myeventhubname",
                    connection = "myconnvarname") String message,
            final ExecutionContext context) {
        context.getLogger().info(message);
        //example message [{"sessionID":"S20200310371_47","deviceID":"Testplayer","deviceCoordinateX":-16.98,"deviceCoordinateY":31.08,"deviceCoordinateZ":-2.48,"sendingTimestamp":1583841016539}]

        message = message.substring(1, message.length() - 1);
        Message newMessage = new Message(message);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            databaseAdapter.createInsertStatementForAccelerometerData(connection, newMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new SignalRMessage("newMessage", message);
    }

    @FunctionName("DataAnalytics")
    public HttpResponseMessage fetchToOperate(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request for getting overall Highscore.");

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
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution for overall highscore failed").build();
        }
    }

    @FunctionName("DataAnalyticsSession")
    public HttpResponseMessage fetchToOperate2(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request for getting session highscore.");

        String session = request.getQueryParameters().get("session");
        String beginningTimestamp = request.getQueryParameters().get("begin");
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            ResultSet resultSet = databaseAdapter.createSelectStatementForHighscoreSession(connection, session, beginningTimestamp);
            ResultSetHandler resultSetHandler = new ResultSetHandler();
            Gson gson = new Gson();
            List<PlayerScore> playerScoreList = resultSetHandler.getPlayerScoreList(resultSet);
            databaseAdapter.createInsertHighscoresTable(connection, playerScoreList);
            String json = gson.toJson(playerScoreList);
            connection.close();
            return request.createResponseBuilder(HttpStatus.OK).body(json).build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution for session highscore failed").build();
        }
    }

    @FunctionName("DeleteOldAccelerometerData")
    public HttpResponseMessage deleteOldAccelerometerData(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request for delete old AccelerometerData.");
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Connection connection = null;
        try {
            connection = databaseAdapter.connectToDatabase();
            databaseAdapter.createDeleteStatementForAccelerometerData(connection);
            return request.createResponseBuilder(HttpStatus.OK).body("AccelerometerData successfully deleted").build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Statement execution for deleting failed").build();
        }
    }
}

/*
Bei Fehler "The binding type(s) 'eventHubTrigger' are not registered. Please ensure the type is correct and the binding extension is installed. Hosting environment: Production"
C:\Users\stefaniemo\AppData\Local\Temp\Functions
Ordner ExtensionBundles löschen und clean package
*/