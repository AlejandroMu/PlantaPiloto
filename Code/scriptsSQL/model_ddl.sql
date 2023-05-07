SET search_path to asset_manager;

INSERT INTO WORK_SPACE VALUES(nextval('work_space_seq'),'Laboratorio de BioQuimica','Laboratorio de práctica IBQ planta piloto',null);

INSERT INTO DRIVER VALUES(nextval('driver_seq'),'PLC Driver','DriverAsset:tcp -h xrio -p 5431',1);

INSERT INTO TYPE VALUES(nextval('type_seq'),'PLC','PLC Allen Bradley','Asset');
INSERT INTO TYPE VALUES(nextval('type_seq'),'PLC_TAG','TAG de un PLC Allen Bradley','Asset');
INSERT INTO TYPE VALUES(nextval('type_seq'),'Auto','Instrucción se valida y ejecuta por el sistema','Instruction');
INSERT INTO TYPE VALUES(nextval('type_seq'),'Manual','Instrucción se valida y ejecuta por el operario','Instruction');
INSERT INTO TYPE VALUES(nextval('type_seq'),'Mixto','Instrucción se valida por el operario y ejecuta el sistema','Instruction');
--- id state name desc type driv parent
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','B40B','Bio Reactor 40B',1,1,null);
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','ANTIESPUMANTE.ACC','Tag del Bio Reactor 40B',2,1,1);
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','ENT_AGITADOR','Tag del Bio Reactor 40B',2,1,1);       
INSERT INTO ASSET VALUES(nextval('asset_seq'),'A','ENT_FCV101','Tag del Bio Reactor 40B',2,1,1);

INSERT INTO META_DATA VALUES(nextval('meta_data_seq'),'plc.ip','B40B','Dirección ip del bio reactor 40B',1);
INSERT INTO META_DATA VALUES(nextval('meta_data_seq'),'plc.slot','0','Slot de conexión del bio reactor 40B',1);

INSERT INTO PROCESS VALUES(nextval('process_seq'),'Proceso de Prueba','Proceso para probar la aplicación',1);
--- process asset delay
INSERT INTO PROCESS_ASSET VALUES(1,1,30000);

INSERT INTO INSTRUCTION VALUES(nextval('instruction_seq'),'Cambiar valor de agitador','cuando quiera (expression)',4);
--- inst process
INSERT INTO INSTRUCTION_PROCESS VALUES(1,1);

INSERT INTO ACTION VALUES(nextval('action_seq'),'SET_AGI','Cambiar valor agitador','colocar el agitador en X valor (expression)');
INSERT INTO ACTION_INSTRUCTION VALUES(1,1);
