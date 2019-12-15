package de.maibornwolff.iotshowcase;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.documentdb.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataIngestion: Azure Function with IoTHub and CosmosDB
 * DataAnalytics: Azure Function with CosmosDB and HttpTrigger
 */
public class Function {
    private DocumentClient client;
    private HashMap<String, DeviceDocument> deviceDocuments = new HashMap<String, DeviceDocument>();

    @FunctionName("DataIngestion")
    public void transferToOperate(
            @EventHubTrigger(name = "msg",
                    eventHubName = "myeventhubname",
                    connection = "myconnvarname") String message,
            final ExecutionContext context ) throws IOException, DocumentClientException {
        context.getLogger().info(message);
        //example message [{"sessionID":"s-id1274168885","deviceID":"d-id2047831712","deviceCoordinateX":14.601536193152906,"deviceCoordinateY":5.84587092154832,"deviceCoordinateZ":7.0314324375097375,"timestamp":"ddmmyyyy"}]

        //sortieren/filtern
        //TODO Zeitraum 10 Sekunden => Daten sammeln
        //TODO Hinzufügen von X,Y,Z-Koords+Timestamp in Liste eines Geräts
        DeviceDocument deviceDocument = new DeviceDocument();
        deviceDocument.setDeviceID("d-id2047831712");
        deviceDocument.setSessionID("s-id1274168885");
        SmartphoneData smartphoneData1 = new SmartphoneData();
        smartphoneData1.setDeviceCoordinateX(14.601536193152906);
        smartphoneData1.setDeviceCoordinateY(5.84587092154832);
        smartphoneData1.setDeviceCoordinateZ(7.0314324375097375);
        smartphoneData1.setTimestamp("ddmmyyyy");
        ArrayList<SmartphoneData> smartphones = new ArrayList<SmartphoneData>();
        smartphones.add(smartphoneData1);
        deviceDocument.setSmartphoneData(smartphones);
        //deviceDocument = createDeviceDocument(message);

        //CosmosDB

        //Verbindung mit dem Cosmos DB Account
        //this.client = new DocumentClient("https://showcasedata.documents.azure.com:443/", "9AahygSstd60HofssExl7JG3BJkv2R3zijAF1OuqPxgxhOSkarkcS6ATT4hyN5of3hnM1wRR4NFKe7GKkYSxaA==", new ConnectionPolicy(), ConsistencyLevel.Session);
        //Verbindung mit dem Cosmos DB Emulator
        this.client = new DocumentClient("https://localhost:8081", "C2y6yDjf5/R+ob0N8A7Cgv30VRDJIWEHLM+4QDU5DE2nQ9nDuVTqobD4b8mGGyPMbIZnqyMsEcaGQy67XIw/Jw==", new ConnectionPolicy(), ConsistencyLevel.Session);
        //Verbindung mit der DB herstellen, ggf. neue DB erstellen
        this.createDatabaseIfNotExists("AccelerometerDB");
        //Erstelle Container für (jede) Session (TODO sessionID)
        this.createDocumentCollectionIfNotExists("AccelerometerDB", "SessionCollection");
        //Erstelle Element für (jedes) Gerät (deviceID)
        String collectionLink = String.format("/dbs/%s/colls/%s", "AccelerometerDB", "SessionCollection");
        this.client.createDocument(collectionLink, deviceDocument, new RequestOptions(), true);
        this.writeToConsoleAndPromptToContinue(String.format("Created Device %s", deviceDocument.getDeviceID()));
    }

    private DeviceDocument createDeviceDocument(String message) {
        return null;
    }

    private void createDatabaseIfNotExists(String databaseName) throws DocumentClientException, IOException {
        String databaseLink = String.format("/dbs/%s", databaseName);

        // Check to verify a database with the id=AccelerometerDB does not exist
        try {
            this.client.readDatabase(databaseLink, null);
            this.writeToConsoleAndPromptToContinue(String.format("Found %s", databaseName));
        } catch (DocumentClientException de) {
            // If the database does not exist, create a new database
            if (de.getStatusCode() == 404) {
                Database database = new Database();
                database.setId(databaseName);

                this.client.createDatabase(database, null);
                this.writeToConsoleAndPromptToContinue(String.format("Created %s", databaseName));
            } else {
                throw de;
            }
        }
    }

    private void writeToConsoleAndPromptToContinue(String text) throws IOException {
        System.out.println(text);
        System.out.println("Press any key to continue ...");
        System.in.read();
    }

    private void createDocumentCollectionIfNotExists(String databaseName, String collectionName) throws IOException,
            DocumentClientException {
        String databaseLink = String.format("/dbs/%s", databaseName);
        String collectionLink = String.format("/dbs/%s/colls/%s", databaseName, collectionName);

        try {
            this.client.readCollection(collectionLink, null);
            writeToConsoleAndPromptToContinue(String.format("Found %s", collectionName));
        } catch (DocumentClientException de) {
            if (de.getStatusCode() == 404) {
                DocumentCollection collectionInfo = new DocumentCollection();
                collectionInfo.setId(collectionName);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.setOfferThroughput(400);
                this.client.createCollection(databaseLink, collectionInfo, requestOptions);
                this.writeToConsoleAndPromptToContinue(String.format("Created %s", collectionName));
            } else {
                throw de;
            }
        }
    }




}
