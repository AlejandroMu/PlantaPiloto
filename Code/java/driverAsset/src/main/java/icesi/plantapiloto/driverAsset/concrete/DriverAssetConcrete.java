package icesi.plantapiloto.driverAsset.concrete;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;

public interface DriverAssetConcrete {

    MeasurementDTO[] readAsset(AssetDTO[] asset, int execId);

    void setPointAsset(AssetDTO asset, double value);

}
