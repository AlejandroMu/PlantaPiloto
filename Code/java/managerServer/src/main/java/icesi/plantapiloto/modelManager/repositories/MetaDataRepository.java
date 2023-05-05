package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.MetaData;

public class MetaDataRepository implements Repository<MetaData, Integer> {
    @Override
    public Class<MetaData> getType() {
        return MetaData.class;
    }
}
