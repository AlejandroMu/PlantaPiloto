package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import icesi.plantapiloto.common.model.ProcessAsset;
import icesi.plantapiloto.common.model.ProcessAssetPK;

public class ProcessAssetRepository implements Repository<ProcessAsset, ProcessAssetPK> {
    private static ProcessAssetRepository instance;

    private ProcessAssetRepository() {
    }

    public static ProcessAssetRepository getInstance() {
        if (instance == null) {
            instance = new ProcessAssetRepository();
        }
        return instance;
    }

    @Override
    public Class<ProcessAsset> getType() {
        return ProcessAsset.class;
    }

    public List<ProcessAsset> findByProcess(Integer id) {
        String query = "From ProcessAsset pa Where pa.process.id = ?1";
        return executeQuery(query, id);
    }

}
