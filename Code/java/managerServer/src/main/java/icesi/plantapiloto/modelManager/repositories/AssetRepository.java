package icesi.plantapiloto.modelManager.repositories;

import java.util.HashMap;
import java.util.List;

import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Type;

public class AssetRepository implements Repository<Asset, Integer> {

    public List<Asset> findByType(Type type) {
        String query = "From Asset a Where a.typeBean.id = :type";
        HashMap<String, Object> props = new HashMap<>();
        props.put("type", type.getId());
        List<Asset> result = executeQuery(query, props, false);
        return result;
    }

    @Override
    public Class<Asset> getType() {
        return Asset.class;
    }

}
