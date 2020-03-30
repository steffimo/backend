---
author: Stefanie Motzokan
platforms: java
---

# A simple internet of things showcase 
This is the simple IoT game *ShakeIT*, used as a IoT showcase in the thesis "Developement of an IoT showcase with a didactic concept".\
There are 3 repositories for this: 
- [Backend](https://github.com/steffimo/backend)
- [User Frontend](https://github.com/steffimo/iotgame)
- [Analyse Frontend](https://github.com/steffimo/analysis)

If you're interested in the content of the thesis, here's the abstract:\
Digitalization brought the concept of the "Internet of Things" (IoT) as a central concept into circulation and everyday life in both industry and the private sector. But what is behind it?\
This work is intended to provide insights into the background of the Internet of Things. Using a developed model, typical IoT components and data flows were realized, which can also be traced back to common architectural patterns. The project is based on the requirements of developing an application that has simple structures and can be implemented cost-effectively.\
As a showcase, this model can be used in all areas for education, training and further education. In particular, it is also intended to lay a foundation for sensitization in form of data maturity and data awareness.\
As an example, this model was evaluated with a didactic concept for a workshop designed for pupils from the upper school of the Bavarian Gymnasium.

---The thesis will be published soon, so that here'll gonna be the link ---

## Requirements
- Azure-abonnement - there's a free version for 30 days with 170€ credit
- Apache Maven, version 3.0+
- IntelliJ (included Maven) used as IDE, you could also take Eclipse, Visual Studio Code, ...
- Java Developer Kit (JDK), version 8 (not higher!); to change in IntelliJ, open with right click on your project folder "Open module settings"
- [Azure Command-Line Interface (CLI)](https://docs.microsoft.com/en-gb/cli/azure/?view=azure-cli-latest) for Deploying
- npm

## Preperations in Azure Portal

You 'll need following Azure Components in a Ressourcegroup:
- IoT Hub - this case: ShowcaseHubMW
  - by creating use 
     - Pricing and scale: Free
- SQL Database created parallel with "Computer with SQL Server" - this case: IoTShowcaseData (showcase-server/IoTShowcaseData)
  - by creating use    
     - Resource type: Single database
     - create own server with administration user and password
     - Compute + storage: Configure database => Basic
     - Firewall rules: 'Allow Azure services and resources to access this server' - toggle Yes
     - Advanced data security: No
  - after providing execute the manual marked statements in the comments from java class 'DatabaseAdapter' to create and fill your tables
- Function App (includes an App Service Plan) - this case: iotShowcaseFunctions
  - by creating use 
     - Runtime stack: Java
     - Plan type: Consumption (Serverless)
  - after providing go to Platform features
    - to API => CORS and set Allowed Origins to *
    - to General Settings => Configuration: make sure all values from your local.settings.json in the backend must be defined in Application settings, otherwise add them
- SignalR Service - this case: iotDataMW
   - by creating use 
      - ServiceMode: Serverless
   - after providing go to Settings => CORS and set Allowed Origins to *

## Local Testing
Recommended to install [Azure Functions Core Tools](https://docs.microsoft.com/en-gb/azure/azure-functions/functions-run-local#v2), version 2. It provides a local development environment for writing, running, and debugging Azure Functions.\
The JAVA_HOME environment variable must be set to the install location of the JDK.

## Running 

```
mvn clean 
mvn package
```

For local testing use Plugin: 
```
azure-function:run
```
\
For deploying to Azure Portal:
- for the first time: 
```
az login
```
(Then a browser window will open to log into your account)

- use Plugin: 
```
azure-function:deploy
```