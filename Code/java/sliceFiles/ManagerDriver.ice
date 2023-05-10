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
    module dtos{
        class MeasurementDTO{
            int assetId;
            string assetName;
            double value;
            int exeId;
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

    }
    ["java:serializable:icesi.plantapiloto.common.dtos.MeasurementDTO"]
    sequence<byte> MeasurementDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.TypeDTO"]
    sequence<byte> TypeDTO;
    ["java:serializable:icesi.plantapiloto.common.dtos.DriverDTO"]
    sequence<byte> DriverDTO;
    module controllers{

        sequence<AssetDTO> AssetsDTO;
        sequence<Asset> Assets;
        sequence<MeasurementDTO> MeasurementsDTO;
        sequence<int> AssetsIds;
        sequence<TypeDTO> TypesDTO;
        sequence<DriverDTO> DriversDTO;


        interface AssetManagerCallback{
            void saveAssetValue(MeasurementsDTO value);
        }
        
        interface DriverAsset{
            void readAsset(AssetsDTO asset, AssetManagerCallback* server, int execId, long period);
            void stopRead(int exeId);
            void setPointAsset(AssetDTO asset, double value);
        }

        interface AssetManagerController extends AssetManagerCallback{
           void captureAssets(Assets assets, int execId, long period);
           void captureAssetsId(AssetsIds assets, int execId, long period);
           void stopCapture(int execId);

           void changeAssetValue(AssetDTO asset, double value);
           void saveAsset(Asset asset);
           AssetsDTO findByType(int type);
           AssetsDTO findByState(string state);
           AssetsDTO findAll();
           void deletById(int id);
           MeasurementsDTO getMeasurments(AssetDTO asset,long initdata, long endDate);

           TypesDTO findAllTypes();
           DriversDTO findAllDrivers();
        }
    }
}