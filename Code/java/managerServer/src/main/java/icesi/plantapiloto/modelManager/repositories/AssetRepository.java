package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Asset;

public class AssetRepository implements Repository<Asset, Integer> {

    private static AssetRepository instance;
    private EntityManager manager;

    private AssetRepository() {
        manager = managerFactory.createEntityManager();

    }

    public static AssetRepository getInstance() {
        if (instance == null) {
            instance = new AssetRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<Asset> getType() {
        return Asset.class;
    }

    public List<Asset> findByType(Integer type) {
        String query = "From Asset a Where a.typeBean.id = ?1";
        List<Asset> result = executeQuery(query, new Object[] { type });
        return result;
    }

    public List<Asset> findByState(String state) {
        String query = "From Asset a Where a.assetState = ?1";
        List<Asset> result = executeQuery(query, new Object[] { state });
        return result;
    }
}
