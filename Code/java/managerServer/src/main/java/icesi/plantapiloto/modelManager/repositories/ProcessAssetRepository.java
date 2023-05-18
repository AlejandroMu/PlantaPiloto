package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.ProcessAsset;

public class ProcessAssetRepository implements Repository<ProcessAsset, Integer> {
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

}
