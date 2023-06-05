package icesi.plantapiloto.modelManager.services;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.controllers.DriverAssetPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.common.model.WorkSpace;
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

    private DriverService driverService;

    public AssetService() {
        assetRepository = AssetRepository.getInstance();
        typeRepository = TypeRepository.getInstance();
        workSpaceRepository = WorkSpaceRepository.getInstance();
    }

    /**
     * @param driverService the driverService to set
     */
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * @param measurementService the measurementService to set
     */
    public void setMeasurementService(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    public Asset createAsset(String name, String desc, Integer type, Integer workSpace, Integer parent, String state,
            EntityManager manager) {
        Type t = typeRepository.findById(type, manager).get();
        Asset asset = new Asset();
        asset.setAssetState(state);
        asset.setDescription(desc);
        asset.setName(name);
        asset.setTypeBean(t);
        if (workSpace != null) {
            WorkSpace dr = workSpaceRepository.findById(workSpace, manager).get();
            asset.setWorkSpace(dr);
        }
        saveAsset(asset, parent, manager);
        return asset;
    }

    public void saveAsset(Asset asset, Integer assetParent, EntityManager manager) {
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
            Asset parent = assetRepository.findById(assetParent, manager).get();
            asset.setAsset(parent);
        }
        assetRepository.save(asset, manager);
    }

    public void addMetadata(int asset, EntityManager manager, MetaData... metaDatas) {
        Asset as = assetRepository.findById(asset, manager).get();
        addMetadata(as, manager, metaDatas);
    }

    public void addMetadata(Asset asset, EntityManager manager, MetaData... metaDatas) {
        for (MetaData metaData : metaDatas) {
            boolean isValid = metaData.getDescription() != null
                    && metaData.getName() != null
                    && metaData.getValue() != null;

            if (isValid) {
                metaData.setAssetBean(asset);
                asset.addMetaData(metaData);
            } else {
                System.out.println("Metadata invalid: " + (new JsonEncoder()).encode(metaData));
            }

        }
        assetRepository.update(asset, manager);
    }

    public void changeSetPoint(int assetDto, double value, EntityManager manager) {
        Asset asset = assetRepository.findById(assetDto, manager).get();
        DriverAssetPrx prx = driverService.getDriverProxy(asset.getTypeBean().getDriver());
        if (prx == null) {
            System.err.println("is not posible connect with driver");
            return;
        }
        int execId = prx.setPointAsset(AssetMapper.getInstance().asEntityDTO(asset), value);

        if (execId != -1) {
            MeasurementDTO dto = new MeasurementDTO(assetDto, asset.getName(), value, execId,
                    System.currentTimeMillis());
            measurementService.saveMeasurements(new MeasurementDTO[] { dto }, manager);
        } else {
            System.out.println("no esta en ninguna ejecución");
        }
    }

    public List<Asset> getAssetsByType(int type, EntityManager manager) {

        return assetRepository.findByType(type, manager);
    }

    public List<Asset> getAssets(EntityManager manager) {
        return assetRepository.findAll(manager);
    }

    public List<Asset> getAssetsByState(String state, EntityManager manager) {
        return assetRepository.findByState(state, manager);
    }

    public void deletById(int id, EntityManager manager) {
        Asset asset = assetRepository.findById(id, manager).get();
        asset.setAssetState(ASSET_REMOVED_STATE);
        assetRepository.update(asset, manager);
    }

    public List<Asset> getAssetsByWorkSpace(int workSpaceId, EntityManager manager) {
        return assetRepository.findByWorkSpace(workSpaceId, manager);
    }

    public Asset getAssetById(int asset, EntityManager manager) {
        return assetRepository.findById(asset, manager).get();
    }

    public void updateAsset(AssetDTO assetDto, EntityManager manager) {
        Asset asset = assetRepository.findById(assetDto.assetId, manager).get();
        asset.setName(assetDto.name);
        asset.setDescription(assetDto.description);

        if (!asset.getTypeBean().getName().equals(assetDto.typeName)) {
            Type type = typeRepository.findByName(assetDto.typeName, manager);
            asset.setTypeBean(type);
        }

        if (asset.getWorkSpace().getId() != assetDto.workId) {
            WorkSpace workSpace = workSpaceRepository.findById(assetDto.workId, manager).get();
            asset.setWorkSpace(workSpace);
        }
        if (assetDto.parent != null) {
            Asset par = assetRepository.findById(asset.getAsset().getId(), manager).get();
            asset.setAsset(par);
        } else {
            asset.setAsset(null);
        }
        asset.setAssetState(assetDto.state);
        Map<String, String> props = assetDto.props;
        if (props != null) {
            asset.getMetaData().stream().forEach(x -> {
                if (props.containsKey(x.getName())) {
                    x.setValue(props.get(x.getName()));
                }
            });
        }
        updateAsset(asset, manager);
    }

    public void updateAsset(Asset asset, EntityManager manager) {
        assetRepository.update(asset, manager);
    }
}
