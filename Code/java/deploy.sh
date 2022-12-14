#!/bin/bash

./gradlew build
mkdir bin
cp controlLayer/plcPlugin/build/libs/plcPlugin.jar bin/
cp java/controlLayer/plcPlugin/etherip-1.0.0.jar bin/
cp java/controlLayer/plcPlugin/src/main/resources/plugin.conf bin/
cp java/controlLayer/MQTT/build/libs/MQTT.jar bin/
cp java/controlLayer/scheduleManager/build/libs/scheduler.jar bin/
cp java/controlLayer/scheduleManager/src/main/resources/schedule.properties bin/
java -cp "./*" icesi.plantapiloto.scheduleManager.ScheduleManager




