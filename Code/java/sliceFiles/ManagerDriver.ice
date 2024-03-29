["java:package:icesi.plantapiloto"]
module common{

    ["java:serializable:icesi.plantapiloto.common.model.Action"]
	sequence<byte> Action;  
	["java:serializable:icesi.plantapiloto.common.model.Asset"]
	sequence<byte> Asset;  
	["java:serializable:icesi.plantapiloto.common.model.Driver"]
	sequence<byte> Driver;  
	["java:serializable:icesi.plantapiloto.common.model.Execution"]
	sequence<byte> Execution;  
	["java:serializable:icesi.plantapiloto.common.model.ExecutionInstruction"]
	sequence<byte> ExecutionInstruction;  
	["java:serializable:icesi.plantapiloto.common.model.ExecutionInstructionPK"]
	sequence<byte> ExecutionInstructionPK;  
	["java:serializable:icesi.plantapiloto.common.model.Instruction"]
	sequence<byte> Instruction;  
	["java:serializable:icesi.plantapiloto.common.model.Measurement"]
	sequence<byte> Measurement;  
	["java:serializable:icesi.plantapiloto.common.model.MetaData"]
	sequence<byte> MetaData;  
	["java:serializable:icesi.plantapiloto.common.model.Process"]
	sequence<byte> Process;  
	["java:serializable:icesi.plantapiloto.common.model.ProcessAsset"]
	sequence<byte> ProcessAsset;  
	["java:serializable:icesi.plantapiloto.common.model.ProcessAssetPK"]
	sequence<byte> ProcessAssetPK;  
	["java:serializable:icesi.plantapiloto.common.model.Type"]
	sequence<byte> Type;  
	["java:serializable:icesi.plantapiloto.common.model.WorkSpace"]
	sequence<byte> WorkSpace;  

    ["java:serializable:icesi.plantapiloto.common.dtos.output.AssetDTO"]
    sequence<byte> AssetDTO;
    sequence<AssetDTO> AssetsDTO;
        
    module dtos{
        class MeasurementDTO{
            int assetId;
            string assetName;
            double value;
            int execId;
            long timeStamp;
        }
        class TypeDTO{
            int id;
            string name;
        	string description;
        	string tableName;
        }
        class DriverDTO{
            int id;
	        string name;
	        string serviceProxy;
            int workSpaceId;
        }
        class ActionDTO{
            int id;
            string nameTech;
            string nameDisplay;
            string expression;
        }
        sequence<ActionDTO> ActionsDTO;
        class InstructionDTO{
            int id;
            string nameTech;
            string predicate;
            string type;
            ActionsDTO actions;
        }
        sequence<InstructionDTO> InstructionsDTO;
        class ProcessDTO{
            int id;
            string name;
            string description;
            int workSpaceId;
            InstructionsDTO instructions;
            AssetsDTO assets;
        }
        class ExecutionInstructionDTO{
            int id;
            int execId;
            string instructionName;
            int instId;
            long time;
        }
        sequence<ExecutionInstructionDTO> ExecLogs;
        class ExecutionDTO{
            int id;
            int processId;
            string processName;
            string operName;
            long startDate;
            long endDate;
            string state;
            ExecLogs logs;
        }
        class WorkSpaceDTO{
            int id;
            string name;
            string desc;
            int departmentId;
        }
        class ProcessAssetDTO{
            int processId;
            AssetDTO asset;
            long period;
        }

    }
    ["java:serializable:icesi.plantapiloto.common.dtos.MeasurementDTO"]
    sequence<byte> MeasurementDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.TypeDTO"]
    sequence<byte> TypeDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.DriverDTO"]
    sequence<byte> DriverDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.ProcessDTO"]
    sequence<byte> ProcessDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.InstructionDTO"]
    sequence<byte> InstructionDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.ActionDTO"]
    sequence<byte> ActionDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.ExecutionDTO"]
    sequence<byte> ExecutionDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.WorkSpaceDTO"]
    sequence<byte> WorkSpaceDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.ProcessAssetDTO"]
    sequence<byte> ProcessAssetDTO;
    module controllers{

        sequence<Asset> Assets;
        sequence<MeasurementDTO> MeasurementsDTO;
        sequence<TypeDTO> TypesDTO;
        sequence<DriverDTO> DriversDTO;
        sequence<ProcessDTO> ProcessesDTO;
        sequence<InstructionDTO> InstructionsDTO;
        sequence<ActionDTO> ActionsDTO;
        sequence<ExecutionDTO> ExecutionsDTO;
        sequence<WorkSpaceDTO> WorkSpacesDTO;
        sequence<MetaData> MetaDatas;
        sequence<ProcessAssetDTO> ProcessAssetsDTO;


        interface MeasurementManagerController{
            void saveAssetValue(MeasurementsDTO value);
            MeasurementsDTO getMeasurments(int assetId, long initdata, long endDate);
            MeasurementsDTO getMeasurmentsByExecution(int execId);
        }

        interface DriverAsset{
            void readAsset(AssetDTO asset, MeasurementManagerController* server, int execId, long period,bool newProcess);
            void stopRead(int exeId);
            int setPointAsset(AssetDTO asset, double value);
            void pauseReader(int execId);
            void resumeReader(int execId);
        }
        
        interface AssetManagerController{
           
           int saveAsset(string name,string desc, int typeId, int workId, int assetP,string state,MetaDatas metaData);
           void updateAsset(AssetDTO asset);
           void addMetadataToAsset(MetaDatas metaDatas, int assetId);

           void changeAssetValue(int assetId, double value);

           AssetsDTO findByType(int type);
           AssetsDTO findByWorkSpace(int workSpaceId);
           AssetsDTO findByState(string state);
           AssetsDTO findAll();
           AssetDTO findById(int id);

           void deletById(int id);

        }

        interface ProcessManagerController{        
           int startProcess(int processId, string userName);
           void stopExecutionProcess(int execId);
           void pauseExecutionProcess(int execId);
           void continueExecutionProcess(int execId);

           int saveProcess(string name, string desc, int workSpaceId);
           int deleteProcessById(int id);

           void addAssetToProcess(int asset, int processId,long period);
           void updateAssetToProcess(int asset, int processId,long period);
           void removeAssetToProcess(int asset, int processId);

           ProcessAssetsDTO getAssetOfProcess(int processId);

           void addInstructionToProcess(int instId, int processId);

           void applyIntructionToExecution(int instId, int execId);

           ProcessesDTO findProcessByWorkSpace(int workSpaceId);

           ExecutionsDTO findExecutions(int processId, long startDate, long endDate,string state);

        }

        interface InstructionManagerController{
           int saveInstruction(string nameTec, string predicate, string type);
           void addActionToInstruction(int actionId, int instId);
           InstructionsDTO findInstructions();
           InstructionsDTO findInstructionsByNameMatch(string namepattern);
           ActionsDTO findActions(int instId);
        }

        interface ActionManagerController{
           int saveAction(string nameTec, string displayName, string expression);
           ActionsDTO findActions();
           ActionsDTO findActionByNameMatch(string namepattern);
        }

        interface WorkSpaceManagerController{
            int saveWorkSpace(string name, string desc, int depart);
            WorkSpacesDTO findWorkSpaces();
            WorkSpacesDTO findWorkSpacesByDepartment(int idDep);
            
            int saveDriver(string name, string serviceProxy, int workSpaceId);
            DriversDTO findAllDrivers();
            DriversDTO findDriversByWorkSpace(int workSpaceId);

            int saveAssetType(string name, string desc, int driver);
            TypesDTO findAllTypes();
            TypesDTO findTypesByDriver(int driverId);

        }
    }
}