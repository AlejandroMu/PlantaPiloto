package icesi.plantapiloto.driverAsset.concrete;

import java.util.Map;

import icesi.plantapiloto.common.dtos.AssetDTO;
import icesi.plantapiloto.common.dtos.MeasurementDTO;

public interface DriverAssetConcrete {

    MeasurementDTO[] readAsset(AssetDTO[] asset, Map<String, String> config);

    void setPointAsset(AssetDTO asset, double value, Map<String, String> config);

}
