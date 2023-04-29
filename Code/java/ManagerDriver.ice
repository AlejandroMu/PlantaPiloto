["java:package:icesi.plantapiloto.modelManager"]
module controllers{

    dictionary<string,string> ProcessConfig;

    class AssetDTO{
        int assetId;
        string name;
        string typeName;
        string state;
    }    
       
    sequence<AssetDTO> Assets;

    class MeasurementDTO{
        int assetId;
        double value;
        long timeStamp;
    }

    sequence<MeasurementDTO> Measuresment;

    class AssetValue{
        Measuresment values;
        int exeId;
    }

    interface AssetManagerCallback{
        void saveAssetValue(AssetValue value);
    }
    
    interface DriverAsset{
        void readAsset(Assets asset,ProcessConfig config,AssetManagerCallback* server);
        void stopRead(int exeId);
        void setPointAsset(AssetDTO asset, double value,ProcessConfig config);
    }

}