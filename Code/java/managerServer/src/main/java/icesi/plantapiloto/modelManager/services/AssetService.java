package icesi.plantapiloto.modelManager.services;

import java.util.List;

import icesi.plantapiloto.common.controllers.DriverAssetPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.common.model.WorkSpace;
import icesi.plantapiloto.modelManager.Main;
import icesi.plantapiloto.modelManager.repositories.AssetRepository;
import icesi.plantapiloto.modelManager.repositories.TypeRepository;
import icesi.plantapiloto.modelManager.repositories.WorkSpaceRepository;

public class AssetService {

    public static final String ASSET_ACTIVE_STATE = "A";
    public static final String ASSET_REMOVED_STATE = "R";

    // Repositories
    private AssetRepository assetRepository;
    private TypeRepository typeRepository;
    private WorkSpaceRepository workSpaceRepository;

    private MeasurementService measurementService;

    public AssetService() {
        assetRepository = AssetRepository.getInstance();
        typeRepository = TypeRepository.getInstance();
        workSpaceRepository = WorkSpaceRepository.getInstance();
    }

    /**
     * @param measurementService the measurementService to set
     */
    public void setMeasurementService(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    public Asset createAsset(String name, String desc, Integer type, Integer workSpace, Integer parent, String state) {
        Type t = typeRepository.findById(type).get();
        Asset asset = new Asset();
        asset.setAssetState(state);
        asset.setDescription(desc);
        asset.setName(name);
        asset.setTypeBean(t);
        if (workSpace != null) {
            WorkSpace dr = workSpaceRepository.findById(workSpace).get();
            asset.setWorkSpace(dr);
        }
        saveAsset(asset, parent);
        return asset;
    }

    public void saveAsset(Asset asset, Integer assetParent) {
        boolean isValid = asset.getName() != null
                && asset.getTypeBean() != null
                && asset.getDescription() != null
                && asset.getWorkSpace() != null;
        if (!isValid) {
            return;
        }
        if (asset.getAssetState() == null) {
            asset.setAssetState(ASSET_ACTIVE_STATE);
        }
        if (asset.getAsset() == null && assetParent != null && assetParent != -1) {
            Asset parent = assetRepository.findById(assetParent).get();
            asset.setAsset(parent);
        }
        assetRepository.save(asset);
    }

    public void addMetadata(int asset, MetaData... metaDatas) {
        Asset as = assetRepository.findById(asset).get();
        addMetadata(as, metaDatas);
    }

    public void addMetadata(Asset asset, MetaData... metaDatas) {
        for (MetaData metaData : metaDatas) {
            boolean isValid = metaData.getDescription() != null
                    && metaData.getName() != null
                    && metaData.getValue() != null;

            if (isValid) {
                metaData.setAssetBean(asset);
                asset.addMetaData(metaData);
                assetRepository.update(asset);
            } else {
                System.out.println("Metadata invalid: " + (new JsonEncoder()).encode(metaData));
            }

        }
    }

    public void changeSetPoint(int assetDto, double value) {
        Asset asset = assetRepository.findById(assetDto).get();
        DriverAssetPrx prx = DriverAssetPrx
                .checkedCast(Main.communicator.stringToProxy(asset.getTypeBean().getDriver().getServiceProxy()));
        int execId = prx.setPointAsset(AssetMapper.getInstance().asEntityDTO(asset), value);
        if (execId != -1) {
            MeasurementDTO dto = new MeasurementDTO(assetDto, asset.getName(), value, execId,
                    System.currentTimeMillis());
            measurementService.saveMeasurements(new MeasurementDTO[] { dto });
        } else {
            System.out.println("no esta en ninguna ejecución");
        }
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
        Asset asset = assetRepository.findById(id).get();
        asset.setAssetState(ASSET_REMOVED_STATE);
        assetRepository.update(asset);
    }

    public List<Asset> getAssetsByWorkSpace(int workSpaceId) {
        return assetRepository.findByWorkSpace(workSpaceId);
    }

    public Asset getAssetById(int asset) {
        return assetRepository.findById(asset).get();
    }
}
