package icesi.plantapiloto.driverAsset.concrete;

import java.util.Map;

import icesi.plantapiloto.modelManager.controllers.AssetDTO;
import icesi.plantapiloto.modelManager.controllers.MeasurementDTO;

public interface DriverAssetConcrete {

    MeasurementDTO[] readAsset(AssetDTO[] asset, Map<String, String> config);

    void setPointAsset(AssetDTO asset, double value, Map<String, String> config);

}
