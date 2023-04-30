["java:package:icesi.plantapiloto"]
module common{
    dictionary<string,string> ProcessConfig;
    module dtos{
        ["java:serializable:icesi.plantapiloto.common.dtos.AssetDTO"]
        sequence<byte> AssetDTO;        
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

    }
    module controllers{
        ["java:serializable:icesi.plantapiloto.common.dtos.AssetValue"]
        sequence<byte> AssetValue;

         ["java:serializable:icesi.plantapiloto.common.dtos.AssetDTO"]
        sequence<byte> AssetDTO;

        sequence<AssetDTO> Assets;


        interface AssetManagerCallback{
            void saveAssetValue(AssetValue value);
        }
        
        interface DriverAsset{
            void readAsset(Assets asset,ProcessConfig config,AssetManagerCallback* server);
            void stopRead(int exeId);
            void setPointAsset(AssetDTO asset, double value,ProcessConfig config);
        }
    }
}