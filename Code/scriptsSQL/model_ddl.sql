SET search_path to asset_manager;

--- id name desc work_space_parent
INSERT INTO WORK_SPACE VALUES(nextval('work_space_seq'),'Laboratorio de BioQuimica','Laboratorio de práctica IBQ planta piloto',null);

--- id name proxy work_space
INSERT INTO DRIVER VALUES(nextval('driver_seq'),'PLC Driver','DriverAsset:tcp -h localhost-p 5431',1);

--- id name desc driver
INSERT INTO ASSET_TYPE VALUES(nextval('type_seq'),'PLC','PLC Allen Bradley',1);
INSERT INTO ASSET_TYPE VALUES(nextval('type_seq'),'PLC_TAG','TAG de un PLC Allen Bradley',1);

--- id state name desc type workspace parent
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','B40B','Bio Reactor 40B',1,1,null);
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','ANTIESPUMANTE.ACC','Tag del Bio Reactor 40B',2,1,1);
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','ENT_AGITADOR','Tag del Bio Reactor 40B',2,1,1);       
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','ENT_FCV101','Tag del Bio Reactor 40B',2,1,1);

--- id propName propValue propDesc asset
INSERT INTO META_DATA VALUES(nextval('meta_data_seq'),'plc.ip','B40B','Dirección ip del bio reactor 40B',1);
INSERT INTO META_DATA VALUES(nextval('meta_data_seq'),'plc.slot','0','Slot de conexión del bio reactor 40B',1);

--- id name desc workspace
INSERT INTO PROCESS VALUES(nextval('process_seq'),'Proceso de Prueba','Proceso para probar la aplicación',1);

--- process asset delay
INSERT INTO PROCESS_ASSET VALUES(1,1,30000);

--- id name trigger type
INSERT INTO INSTRUCTION VALUES(nextval('instruction_seq'),'Cambiar valor de agitador','cuando quiera (expression)','Manual');

--- inst process
INSERT INTO INSTRUCTION_PROCESS VALUES(1,1);

--- id nameTec nameDisplay actionExpression
INSERT INTO ACTION VALUES(nextval('action_seq'),'SET_AGI','Cambiar valor agitador','colocar el agitador en X valor (expression)');

--- action instruction
INSERT INTO ACTION_INSTRUCTION VALUES(1,1);
