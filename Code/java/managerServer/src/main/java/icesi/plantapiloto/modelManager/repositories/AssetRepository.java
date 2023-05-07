package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import icesi.plantapiloto.common.model.Asset;

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

    public List<Asset> findByType(Integer type) {
        String query = "From Asset a Where a.typeBean.id = ?1";
        List<Asset> result = executeQuery(query, new Object[] { type });
        return result;
    }

    @Override
    public Class<Asset> getType() {
        return Asset.class;
    }

}
