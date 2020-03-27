---
author: Stefanie Motzokan
platforms: java
---

# A simple internet of things showcase 

## Requirements
- Azure-abonnement - there's a free version for 30 days with 170€ credit\
- Apache Maven, version 3.0+\
- IntelliJ (included Maven) used as IDE, you could also take Eclipse, Visual Studio Code, ...\
- Java Developer Kit (JDK), version 8 (not higher!); to change in IntelliJ, open with right click on your project folder "Open module settings"\
- [Azure Command-Line Interface (CLI)](https://docs.microsoft.com/en-gb/cli/azure/?view=azure-cli-latest) for Deploying\
- npm

## Preperations in Azure Portal

You 'll need following Azure Components in a Ressourcegroup:
- IoT Hub - this case: ShowcaseHubMW\
by creating use "Tarif & Skalierung: Free"
- SQL Database created with "Computer with SQL Server" - this case: IoTShowcaseData (showcase-server/IoTShowcaseData)\
by creating use "ressourcetype: single database", create own server with administration user and password, "Compute + Speicher: database configure => Basic", "Firewallrules: 'Anderen Azure-Diensten und -Ressourcen den Zugriff auf diesen Server gestatten' - toggle Yes", "Advanced Data Security aktivieren: No"
after providing execute the manual marked statements in the comments from DatabaseAdapter to create and fill your tables
- Function App (includes an App Service Plan) - this case: iotShowcaseFunctions\
by creating use "Runtimestapel: Java", "Plantyp: Verbrauch serverlos"\
after providing go to Plattformfeatures...\
... under API => CORS and set Allowed Origins to *\
... under general options => configuration: make sure all values from your backends local.settings.json must be defined in Anwendungseinstellungen, otherwise add them
- SignalR Service - this case: iotDataMW\
by creating use "Service Mode: serverless"\
after providing go to options => CORS and set Allowed Origins to *

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