#!/bin/bash

component=$1
host=$2
password=$3

remoteBase="planta/$component"
codigo_salida=0
if [ "$4" != "-s" ]; then
  ./gradlew $component:comprimir
  codigo_salida=$?
fi

if [ $codigo_salida -eq 0 ]; then
    sshpass -p $password ssh -o StrictHostKeyChecking=no $host "mkdir -p $remoteBase/ && rm -rf $remoteBase/*"
    sshpass -p $password scp distributable/$component.zip $host:$remoteBase/
    sshpass -p $password ssh $host "cd $remoteBase/ && unzip $component.zip && chmod +x run.sh && ./run.sh $component -f"
else
  echo "Error"
fi