@echo off

setlocal EnableDelayedExpansion
set ASSET_BROKER=C:/asset_broker

if "%~1"=="" (
    if not exist "C:\apache-artemis-2.28.0" (
        echo "Installing apache artemis ..."
        wget -O "C:/apache-artemis-2.28.0.zip" https://dlcdn.apache.org/activemq/activemq-artemis/2.28.0/apache-artemis-2.28.0-bin.zip
        unzip "C:/apache-artemis-2.28.0.zip" -d "C:/" > NUL
        del "C:/apache-artemis-2.28.0.zip" > NUL
        setx ARTEMIS_HOME "C:\apache-artemis-2.28.0"
        setx PATH "%PATH%;C:\apache-artemis-2.28.0\bin"
        echo "Installed apache artemis ..."

    )
    if not exist "C:\asset_broker" (
        echo "starting asset broker service ..."
        setx ASSET_BROKER "C:\asset_broker"
        set ASSET_BROKER=C:/asset_broker
        set ARTEMIS_HOME=C:/apache-artemis-2.28.0

        echo "Broker Home: !ASSET_BROKER!"
        call !ARTEMIS_HOME!/bin/artemis create --name assetBroker --user admin --password password --allow-anonymous !ASSET_BROKER! > NUL
        call !ASSET_BROKER!/bin/artemis-service.exe install
        call !ASSET_BROKER!/bin/artemis-service.exe start
        echo "started asset broker service ..."
    )
) else (
    if "%1" == "uninstall" (
        if exist "C:\asset_broker" (
            call !ASSET_BROKER!/bin/artemis-service.exe stop
            call !ASSET_BROKER!/bin/artemis-service.exe uninstall
            rmdir /s /q "C:/asset_broker"
        )
        goto :eof
    ) else (
        call !ASSET_BROKER!/bin/artemis-service.exe %1
    )

)
