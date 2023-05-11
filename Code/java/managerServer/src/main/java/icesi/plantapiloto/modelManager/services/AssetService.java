package icesi.plantapiloto.modelManager.services;

import java.util.HashSet;
import java.util.List;

import icesi.plantapiloto.common.controllers.DriverAssetPrx;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Process;
import icesi.plantapiloto.common.model.ProcessAsset;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.Main;
import icesi.plantapiloto.modelManager.repositories.AssetRepository;
import icesi.plantapiloto.modelManager.repositories.DriverRepository;
import icesi.plantapiloto.modelManager.repositories.MetaDataRepository;
import icesi.plantapiloto.modelManager.repositories.TypeRepository;

public class AssetService {

    public static final String ASSET_ACTIVE_STATE = "A";
    // Repositories
    private AssetRepository assetRepository;
    private TypeRepository typeRepository;
    private DriverRepository driverRepository;
    private MetaDataRepository metaDataRepository;

    // services

    private ProcessService processService;

    public AssetService() {
        assetRepository = AssetRepository.getInstance();
        typeRepository = TypeRepository.getInstance();
        driverRepository = DriverRepository.getInstance();
        metaDataRepository = MetaDataRepository.getInstance();
        processService = new ProcessService();
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
        // if (asset.getMetaData() != null) {

        // }
    }

    public void addMetadata(Asset asset, MetaData... metaDatas) {
        for (MetaData metaData : metaDatas) {
            boolean isValid = metaData.getDescription() != null
                    && metaData.getName() != null
                    && metaData.getValue() != null;

            if (isValid) {
                metaData.setAssetBean(asset);
                metaDataRepository.save(metaData);
            } else {
                System.out.println("Metadata invalid: " + (new JsonEncoder()).encode(metaData));
            }

        }
    }

    public void captureAssets(MeasurementManagerControllerPrx callback,
            int execId, boolean newProcess) {

        Process process = processService.getProcessOfExeId(execId);
        List<ProcessAsset> Procesassets = process.getProcessAssets();
        AssetMapper mapper = AssetMapper.getInstance();

        for (ProcessAsset processAsset : Procesassets) {
            Asset asset = processAsset.getAsset();
            long period = processAsset.getDelayRead();
            Driver driver = asset.getDriverBean();
            DriverAssetPrx prx = DriverAssetPrx
                    .checkedCast(Main.communicator.stringToProxy(driver.getServiceProxy()));
            prx.readAsset(mapper.asEntityDTO(asset), callback, execId, period, newProcess);
        }

    }

    public void changeSetPoint(AssetDTO assetDto, double value) {
        Asset asset = assetRepository.findById(assetDto.assetId).get();
        DriverAssetPrx prx = DriverAssetPrx
                .checkedCast(Main.communicator.stringToProxy(asset.getDriverBean().getServiceProxy()));
        prx.setPointAsset(AssetMapper.getInstance().asEntityDTO(asset), value);
    }

    public List<Asset> getAssetsByType(Type type) {

        return assetRepository.findByType(type.getId());
    }

    public List<Asset> getAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> getAssetsByState(String state) {
        return assetRepository.findByState(state);
    }

    public void deletById(int id) {
        assetRepository.deleteById(id);
    }

    public void stopCapure(int execId) {
        System.out.println("Stop process Service: " + execId);

        Process process = processService.getProcessOfExeId(execId);
        Asset[] assets = process.getProcessAssets().stream().map(pa -> pa.getAsset()).toArray(Asset[]::new);
        HashSet<Integer> drivId = new HashSet<>();
        for (Asset asset : assets) {
            Driver driver = asset.getDriverBean();
            if (!drivId.contains(driver.getId())) {
                DriverAssetPrx prx = DriverAssetPrx
                        .checkedCast(Main.communicator.stringToProxy(driver.getServiceProxy()));
                System.out.println("Stop process req driver: " + execId);
                prx.stopRead(execId);
                drivId.add(driver.getId());
            }

        }
        processService.endProcess(execId);
    }
}
