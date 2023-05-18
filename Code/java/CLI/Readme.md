## Commands documentation.

### Asset commnads

- help: 

 usage: asset help || asset command
  commands: * required
   asset add -n {name}* -d {desc}* -t {typeId}* -w {workSpaceId} -s {state}* -p {parenId} [-m {metadataProp} {metadataValue} {metadataDesc}]+
   asset setpoint {assetId}* {value}*: set asset actuator value
   asset list ( | -t {typeId} | -s {state} | -w {workSpace}): list assets
   asset remove {assetId}*: remove asset by id

  usage: measure help || measure command
   measure add -a {assetId}* -v {value}* -e {exeId}* -t {time}: long representation of time millis
   measure list [-e {execId}*] |[-a {assetId}* -e {execId} -i {initTime}* -f {endTime}: long representation of time millis]

  usage: workspace help || workspace command
   workspace add -n {name}* -d {desc}* -dep {workId}
   workspace list -w {parent}: list workspaces
   workspace drivers ls -w {workSpace}: list drivers
   workspace drivers add -n {name}* -s {serviceProxy}* -w {workSpace}*: create drivers
   workspace types add -n {name}* -d {desc}* -dr {driverId}*: create types
   workspace types ls -d {driverId}: list types

  usage: action help || action command
   action add -n {name}* -d {displayName}* -e {expression}*: add action
   action list -n {name}: list actions

  usage: instruction help || instruction command
   instruction add -n {name}* -p {predicate}* -t {type}*
   instruction list -n {nameMatch}
   instruction action add -a {actionId}* -i {instrId}*

  usage: process help || process command
   process add -n {name}* -d {desc}* -w {workId}*
   process list -w {workSpace}*
   process start -p {processId}* -o {operName}*
   process stop -e {execId}*
   process pause -e {execId}*
   process continue -e {execId}*
   process execution list -p {processId}* -s {startD}* -e {endDate}
   process asset add -p {processId}* -a {assetId}* -d {delay}*
   process instruction add -p {processId}* -i {instId}*
   process instruction apply -e {execId}* -i {instId}*
