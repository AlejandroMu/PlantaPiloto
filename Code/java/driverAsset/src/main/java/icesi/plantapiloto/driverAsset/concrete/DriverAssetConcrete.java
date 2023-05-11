package icesi.plantapiloto.driverAsset.concrete;

import java.util.List;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;

public interface DriverAssetConcrete {

    List<MeasurementDTO> readAsset(List<AssetDTO> asset, int execId);

    void setPointAsset(AssetDTO asset, double value);

}
