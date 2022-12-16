#!/bin/bash

copy_files(){
    mkdir -p bin
    cp controlLayer/plcPlugin/build/libs/plcPlugin.jar bin/
    cp controlLayer/plcPlugin/etherip-1.0.0.jar bin/
    cp controlLayer/plcPlugin/src/main/resources/plugin.conf bin/
    cp controlLayer/MQTT/build/libs/MQTT.jar bin/
    cp controlLayer/scheduleManager/build/libs/scheduler.jar bin/
    cp controlLayer/scheduleManager/src/main/resources/schedule.properties bin/
    
}

run_app(){
    cd bin/
    java -cp "./*" icesi.plantapiloto.scheduleManager.ScheduleManager
}

if [ "$1" = "build" ]
then
    echo "build arg"
    ./gradlew $1
    copy_files
elif [ "$1" = "run" ]
then
    run_app
fi
