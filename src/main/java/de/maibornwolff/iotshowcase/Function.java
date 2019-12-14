package de.maibornwolff.iotshowcase;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

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
            final ExecutionContext context )
    {
        context.getLogger().info(message);

        //sortieren/filtern

        //CosmosDB
    }


}
