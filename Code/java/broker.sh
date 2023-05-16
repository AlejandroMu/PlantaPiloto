#!/bin/bash


ASSET_BROKER=/opt/asset_broker

if [ $# -gt 0 ]; then
    if [ "$1" == "uninstall" ]; then
        if [ -d $ASSET_BROKER ]; then
            $ASSET_BROKER/bin/artemis-service stop
            rm -r $ASSET_BROKER
        fi
    else 
        $ASSET_BROKER/bin/artemis-service $1
    fi 
else
    ARTEMIS_HOME=/opt/apache-artemis-2.28.0
    if [ ! -d $ARTEMIS_HOME ]; then
        echo "Installing apache artemis ..."
        wget -O "/opt/apache-artemis-2.28.0.zip" https://dlcdn.apache.org/activemq/activemq-artemis/2.28.0/apache-artemis-2.28.0-bin.zip
        unzip "/opt/apache-artemis-2.28.0.zip" -d "/opt" > /dev/null 2>&1
        rm "/opt/apache-artemis-2.28.0.zip" > /dev/null 2>&1
        echo "export ARTEMIS_HOME=$ARTEMIS_HOME" >> ~/.bash_profile
        echo "export PATH=\$PATH:\$ARTEMIS_HOME/bin" >> ~/.bash_profile
        echo "Installed apache artemis ..."
    fi
    if [ ! -d $ASSET_BROKER ]; then
        echo "starting asset broker service ..."
        echo "export ASSET_BROKER=$ASSET_BROKER" >> ~/.bash_profile

        echo "Broker Home: $ASSET_BROKER"
        $ARTEMIS_HOME/bin/artemis create --name assetBroker --user admin --password password --allow-anonymous $ASSET_BROKER > /dev/null 2>&1
        $ASSET_BROKER/bin/artemis-service start
    fi
    echo "installed"

fi


