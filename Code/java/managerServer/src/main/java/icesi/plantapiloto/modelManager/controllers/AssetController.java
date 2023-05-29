package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.AssetManagerController;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.services.AssetService;

public class AssetController implements AssetManagerController {

    private AssetService service;

    /**
     * @param service the service to set
     */
    public void setService(AssetService service) {
        this.service = service;
    }

    @Override
    public void changeAssetValue(int asset, double value, Current current) {
        service.changeSetPoint(asset, value);
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
    public AssetDTO[] findByWorkSpace(int workSpaceId, Current current) {
        List<Asset> assets = service.getAssetsByWorkSpace(workSpaceId);
        return AssetMapper.getInstance().asEntityDTO(assets).toArray(AssetDTO[]::new);
    }

    @Override
    public int saveAsset(String name, String desc, int typeId, int workId, int assetP, String state,
            MetaData[] metaDatas, Current current) {
        Asset assetN = service.createAsset(name, desc, typeId, workId, assetP, state);
        if (metaDatas != null) {
            service.addMetadata(assetN, metaDatas);
        }
        return assetN.getId();
    }

    @Override
    public void addMetadataToAsset(MetaData[] metaDatas, int assetId, Current current) {
        service.addMetadata(assetId, metaDatas);
    }

    @Override
    public AssetDTO findById(int id, Current current) {

        return AssetMapper.getInstance().asEntityDTO(service.getAssetById(id));
    }

}
