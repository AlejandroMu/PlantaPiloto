package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.AssetManagerController;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.DriverDTO;
import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.mappers.DriverMapper;
import icesi.plantapiloto.common.mappers.TypeMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.services.AssetService;
import icesi.plantapiloto.modelManager.services.DriverService;
import icesi.plantapiloto.modelManager.services.TypeService;

public class AssetController implements AssetManagerController {

    private AssetService service;
    private TypeService typeService;
    private DriverService driverService;

    private MeasurementManagerControllerPrx callback;

    /**
     * @return the callback
     */
    public MeasurementManagerControllerPrx getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(MeasurementManagerControllerPrx callback) {
        this.callback = callback;
    }

    public AssetController() {
        service = new AssetService();
        typeService = new TypeService();
        driverService = new DriverService();
    }

    @Override
    public void captureAssets(int execId, Current current) {
        service.captureAssets(callback, execId, false);
    }

    @Override
    public void changeAssetValue(AssetDTO asset, double value, Current current) {
        service.changeSetPoint(asset, value);
    }

    @Override
    public void saveAsset(Asset asset, Current current) {
        Asset assetN = service.createAsset(asset.getName(), asset.getDescription(),
                asset.getTypeBean() == null ? null : asset.getTypeBean().getId(),
                asset.getDriverBean() == null ? null : asset.getDriverBean().getId(),
                asset.getAsset() == null ? null : asset.getAsset().getId(),
                asset.getAssetState());
        List<MetaData> meta = asset.getMetaData();
        System.out.println("Add Asset: metadata: " + meta.size());
        if (meta != null) {
            service.addMetadata(assetN, meta.toArray(new MetaData[meta.size()]));
        }
    }

    @Override
    public AssetDTO[] findByType(int type, Current current) {
        Type t = new Type();
        t.setId(type);
        List<Asset> asset = service.getAssetsByType(t);
        return AssetMapper.getInstance().asEntityDTO(asset).toArray(AssetDTO[]::new);
    }

    @Override
    public AssetDTO[] findAll(Current current) {
        List<Asset> assets = service.getAssets();
        return AssetMapper.getInstance().asEntityDTO(assets).toArray(AssetDTO[]::new);

    }

    @Override
    public AssetDTO[] findByState(String state, Current current) {
        List<Asset> assets = service.getAssetsByState(state);
        return AssetMapper.getInstance().asEntityDTO(assets).toArray(AssetDTO[]::new);

    }

    @Override
    public void deletById(int id, Current current) {

        service.deletById(id);
    }

    @Override
    public TypeDTO[] findAllTypes(Current current) {

        List<Type> types = typeService.findAll();
        return TypeMapper.getInstance().asEntityDTO(types).toArray(TypeDTO[]::new);
    }

    @Override
    public DriverDTO[] findAllDrivers(Current current) {

        List<Driver> drivers = driverService.findAll();
        return DriverMapper.getInstance().asEntityDTO(drivers).toArray(DriverDTO[]::new);
    }

    @Override
    public void stopCapture(int execId, Current current) {
        service.stopCapure(execId);
    }

}
