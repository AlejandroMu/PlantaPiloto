package icesi.plantapiloto.modelManager.assetsManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import icesi.plantapiloto.common.controllers.AssetManagerCallbackPrx;
import icesi.plantapiloto.common.controllers.DriverAssetPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.Measurement;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.Main;
import icesi.plantapiloto.modelManager.repositories.AssetRepository;
import icesi.plantapiloto.modelManager.repositories.DriverRepository;
import icesi.plantapiloto.modelManager.repositories.ExecutionRepository;
import icesi.plantapiloto.modelManager.repositories.MeasurementRepository;
import icesi.plantapiloto.modelManager.repositories.MetaDataRepository;
import icesi.plantapiloto.modelManager.repositories.TypeRepository;

public class AssetService {

    public static final String ASSET_ACTIVE_STATE = "A";

    private AssetRepository assetRepository;
    private MeasurementRepository measurementRepository;
    private ExecutionRepository executionRepository;
    private TypeRepository typeRepository;
    private DriverRepository driverRepository;
    private MetaDataRepository metaDataRepository;

    public AssetService() {
        assetRepository = new AssetRepository();
        measurementRepository = new MeasurementRepository();
    }

    public Asset createAsset(String name, String desc, Integer type, Integer driver, Integer parent, String state) {
        Type t = typeRepository.findById(type).get();
        Asset asset = new Asset();
        asset.setAssetState(state);
        asset.setDescription(desc);
        asset.setName(name);
        asset.setTypeBean(t);
        if (driver != null) {
            Driver dr = driverRepository.findById(driver).get();
            asset.setDriverBean(dr);
        }
        saveAsset(asset, parent);
        return asset;
    }

    public void saveAsset(Asset asset, Integer assetParent) {
        boolean isValid = asset.getName() != null
                && asset.getTypeBean() != null
                && asset.getDescription() != null
                && (asset.getDriverBean() != null || asset.getAsset() != null);
        if (!isValid) {
            return;
        }
        if (asset.getAssetState() == null) {
            asset.setAssetState(ASSET_ACTIVE_STATE);
        }
        if (asset.getAsset() == null && assetParent != null) {
            Asset parent = assetRepository.findById(assetParent).get();
            asset.setAsset(parent);
        }
        if (asset.getDriverBean() == null) {
            asset.setDriverBean(asset.getAsset().getDriverBean());
        }
        assetRepository.save(asset);
    }

    public void addMetadata(Asset asset, MetaData... metaDatas) {
        for (MetaData metaData : metaDatas) {
            boolean isValid = metaData.getDescription() != null
                    && metaData.getName() != null
                    && metaData.getValue() != null;
            if (isValid) {
                metaData.setAssetBean(asset);
                metaDataRepository.save(metaData);
            }

        }
    }

    public void saveMeasurements(MeasurementDTO[] dto) {
        for (MeasurementDTO measurementDTO : dto) {
            Timestamp timestamp = new Timestamp(measurementDTO.timeStamp);
            Execution execution = executionRepository.findById(measurementDTO.exeId).get();
            Asset asset = assetRepository.findById(measurementDTO.assetId).get();
            Measurement measurement = new Measurement();
            measurement.setAssetBean(asset);
            measurement.setExecutionBean(execution);
            measurement.setTime(timestamp);
            measurement.setValue(measurementDTO.value);
            measurementRepository.save(measurement);
        }
    }

    public void captureAssets(Asset[] assets, AssetManagerCallbackPrx callback,
            int execId, long period) {
        AssetDTO[] dtos = AssetMapper.getInstance().asAssetDto(assets);
        HashMap<Integer, DriverAssetPrx> proxies = new HashMap<>();
        HashMap<Integer, List<AssetDTO>> dtosDr = new HashMap<>();

        for (int i = 0; i < dtos.length; i++) {
            Driver driver = assets[i].getDriverBean();
            DriverAssetPrx prx = proxies.get(driver.getId());
            if (prx == null) {
                prx = DriverAssetPrx
                        .checkedCast(Main.communicator.stringToProxy(driver.getServiceProxy()));
            }
            proxies.put(driver.getId(), prx);
            List<AssetDTO> d = dtosDr.get(driver.getId());
            if (d == null) {
                d = new ArrayList<>();
            }
            d.add(dtos[i]);
            dtosDr.put(driver.getId(), d);
        }
        Iterator<Integer> keys = proxies.keySet().iterator();
        while (keys.hasNext()) {
            int key = keys.next();
            DriverAssetPrx prx = proxies.get(key);
            prx.readAsset((AssetDTO[]) dtosDr.get(key).toArray(), callback, execId, period);
        }

    }

    public void changeSetPoint(AssetDTO assetDto, double value) {
        Asset asset = assetRepository.findById(assetDto.assetId).get();
        DriverAssetPrx prx = DriverAssetPrx
                .checkedCast(Main.communicator.stringToProxy(asset.getDriverBean().getServiceProxy()));
        prx.setPointAsset(AssetMapper.getInstance().asAssetDto(asset), value);
    }

    public void captureAssets(int[] assetsId, AssetManagerCallbackPrx callback, int execId, long period) {
        Asset[] assets = new Asset[assetsId.length];
        for (int i = 0; i < assets.length; i++) {
            assets[i] = assetRepository.findById(assetsId[i]).get();
        }
        captureAssets(assets, callback, execId, period);
    }

    public List<Asset> getAssetsByType(Type type) {

        return assetRepository.findByType(type);
    }

}
