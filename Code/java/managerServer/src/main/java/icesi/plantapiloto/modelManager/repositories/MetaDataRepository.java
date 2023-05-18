package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.MetaData;

public class MetaDataRepository implements Repository<MetaData, Integer> {
    private static MetaDataRepository instance;

    private MetaDataRepository() {
    }

    public static MetaDataRepository getInstance() {
        if (instance == null) {
            instance = new MetaDataRepository();
        }
        return instance;
    }

    @Override
    public Class<MetaData> getType() {
        return MetaData.class;
    }
}
