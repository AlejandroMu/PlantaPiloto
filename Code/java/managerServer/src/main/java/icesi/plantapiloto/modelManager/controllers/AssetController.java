package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import javax.persistence.EntityManager;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.AssetManagerController;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.modelManager.entityManager.ManagerPool;
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
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.changeSetPoint(asset, value, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public AssetDTO[] findByType(int type, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Asset> asset = service.getAssetsByType(type, manager);
        AssetDTO[] ret = AssetMapper.getInstance().asEntityDTO(asset).toArray(new AssetDTO[asset.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public AssetDTO[] findAll(Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Asset> assets = service.getAssets(manager);
        AssetDTO[] ret = AssetMapper.getInstance().asEntityDTO(assets).toArray(new AssetDTO[assets.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public AssetDTO[] findByState(String state, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Asset> assets = service.getAssetsByState(state, manager);
        AssetDTO[] ret = AssetMapper.getInstance().asEntityDTO(assets).toArray(new AssetDTO[assets.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public void deletById(int id, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.deletById(id, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public AssetDTO[] findByWorkSpace(int workSpaceId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Asset> assets = service.getAssetsByWorkSpace(workSpaceId, manager);
        AssetDTO[] ret = AssetMapper.getInstance().asEntityDTO(assets).toArray(new AssetDTO[assets.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public int saveAsset(String name, String desc, int typeId, int workId, int assetP, String state,
            MetaData[] metaDatas, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        Asset assetN = service.createAsset(name, desc, typeId, workId, assetP, state, manager);
        if (metaDatas != null) {
            service.addMetadata(assetN, manager, metaDatas);
        }
        int ret = assetN.getId();
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public void addMetadataToAsset(MetaData[] metaDatas, int assetId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.addMetadata(assetId, manager, metaDatas);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public AssetDTO findById(int id, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        AssetDTO ret = AssetMapper.getInstance().asEntityDTO(service.getAssetById(id, manager));
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public void updateAsset(AssetDTO asset, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.updateAsset(asset, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

}
