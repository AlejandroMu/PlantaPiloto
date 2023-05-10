package icesi.plantapiloto.modelManager.repositories;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.MetaData;

public class MetaDataRepository implements Repository<MetaData, Integer> {
    private static MetaDataRepository instance;
    private EntityManager manager;

    private MetaDataRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static MetaDataRepository getInstance() {
        if (instance == null) {
            instance = new MetaDataRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<MetaData> getType() {
        return MetaData.class;
    }
}
