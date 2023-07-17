#!/bin/bash
component=$1
restart=$2
props=""
if [ "$component" == "managerServer" ]; then
    mainClass="icesi.plantapiloto.modelManager.Main"
    props="-props prod.properties"
elif [ "$component" == "plcDriver" ]; then
    mainClass="icesi.plantapiloto.plcDriver.PlcDriver"
elif [ "$component" == "dummyDriver" ]; then
    mainClass="icesi.plantapiloto.dummyDriver.DummyDriver"
else
    echo "no component"
    exit -1
fi

process=$(ps -ef | awk '$0 ~ /'''$mainClass'''/ && $8 ~ /java/ { print $2}')
script_dir=$(dirname "$(readlink -f "$0")")
cd $script_dir
kill_f=0

home_path=$(cd ~ && pwd)
crontab_prog=$(crontab -l)

if [[ $crontab_prog != *"$script_dir/run.sh"* ]]; then
    echo "agendando tarea $(date)" 
    crontab_line="*/15 * * * * . $home_path/.bashrc; $script_dir/run.sh $component >> $script_dir/cron.log 2>&1"
    echo "$crontab_line" > cron_program
    crontab cron_program
fi

if [ "$restart" == "-f" ] && [ -n "$process" ]; then
    kill -9 $process
    rm -f $component.out
    kill_f=1
fi
if [[ $kill_f -eq 1  ||  -z "$process" ]]; then
    nohup java -cp "./*" $mainClass $props >> $component.out 2>> $component.out &
    echo "starting $component"
fi
process=$(ps -ef | awk '$0 ~ /'''$mainClass'''/ && $8 ~ /java/ { print $2}')
echo "process id: $process"
