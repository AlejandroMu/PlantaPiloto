package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.AssetManagerController;
import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.mappers.TypeMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.services.AssetService;
import icesi.plantapiloto.modelManager.services.TypeService;

public class AssetController implements AssetManagerController {

    private AssetService service;
    private TypeService typeService;

    public AssetController() {

    }

    /**
     * @param service the service to set
     */
    public void setService(AssetService service) {
        this.service = service;
    }

    /**
     * @param typeService the typeService to set
     */
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    @Override
    public void changeAssetValue(int asset, double value, Current current) {
        service.changeSetPoint(asset, value);
    }

    @Override
    public int saveAsset(Asset asset, Current current) {
        Asset assetN = service.createAsset(asset.getName(), asset.getDescription(),
                asset.getTypeBean() == null ? null : asset.getTypeBean().getId(),
                asset.getWorkSpace() == null ? null : asset.getWorkSpace().getId(),
                asset.getAsset() == null ? null : asset.getAsset().getId(),
                asset.getAssetState());
        List<MetaData> meta = asset.getMetaData();
        System.out.println("Add Asset: metadata: " + meta.size());
        if (meta != null) {
            service.addMetadata(assetN, meta.toArray(new MetaData[meta.size()]));
        }
        return assetN.getId();
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
    public AssetDTO[] findByWorkSpace(int workSpaceId, Current current) {
        List<Asset> assets = service.getAssetsByWorkSpace(workSpaceId);
        return AssetMapper.getInstance().asEntityDTO(assets).toArray(AssetDTO[]::new);
    }

    @Override
    public TypeDTO[] findTypesByDriver(int driverId, Current current) {
        List<Type> types = typeService.findByDriver(driverId);
        return TypeMapper.getInstance().asEntityDTO(types).toArray(TypeDTO[]::new);
    }

}
