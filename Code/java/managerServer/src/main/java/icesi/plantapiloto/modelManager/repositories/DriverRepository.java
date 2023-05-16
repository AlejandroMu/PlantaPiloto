package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Driver;

public class DriverRepository implements Repository<Driver, Integer> {
    private static DriverRepository instance;
    private EntityManager manager;

    private DriverRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static DriverRepository getInstance() {
        if (instance == null) {
            instance = new DriverRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<Driver> getType() {
        return Driver.class;
    }

    public List<Driver> findByWorkSpace(int workSpaceId) {
        String query = "From Driver d Where d.workSpaceBean.id = ?1";
        return executeQuery(query, workSpaceId);
    }
}
