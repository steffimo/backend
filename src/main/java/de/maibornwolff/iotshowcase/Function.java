package de.maibornwolff.iotshowcase;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with IoTHub and CosmosDB
 */
public class Function {

    @FunctionName("IoTHub-Data")
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
