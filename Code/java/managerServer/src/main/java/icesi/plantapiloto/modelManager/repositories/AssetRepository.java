package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.modelManager.services.AssetService;

public class AssetRepository implements Repository<Asset, Integer> {

    private static AssetRepository instance;

    private AssetRepository() {

    }

    public static AssetRepository getInstance() {
        if (instance == null) {
            instance = new AssetRepository();
        }
        return instance;
    }

    @Override
    public Class<Asset> getType() {
        return Asset.class;
    }

    public List<Asset> findByType(Integer type, EntityManager manager) {
        String query = "From Asset a Where a.typeBean.id = ?1 AND a.assetState != ?2";
        List<Asset> result = executeQuery(manager, query, type, AssetService.ASSET_REMOVED_STATE);
        return result;
    }

    public List<Asset> findByState(String state, EntityManager manager) {
        String query = "From Asset a Where a.assetState = ?1";
        List<Asset> result = executeQuery(manager, query, state);
        return result;
    }

    @Override
    public List<Asset> findAll(EntityManager manager) {
        String query = "From Asset a Where a.assetState != ?1";
        List<Asset> result = executeQuery(manager, query, AssetService.ASSET_REMOVED_STATE);
        return result;
    }

    public List<Asset> findByWorkSpace(int workSpaceId, EntityManager manager) {
        String query = "From Asset a Where a.workSpace.id = ?1 AND a.assetState != ?2";
        List<Asset> result = executeQuery(manager, query, workSpaceId, AssetService.ASSET_REMOVED_STATE);
        return result;
    }
}
