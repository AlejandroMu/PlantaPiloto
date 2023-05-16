package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Type;

public class TypeRepository implements Repository<Type, Integer> {
    private static TypeRepository instance;
    private EntityManager manager;

    private TypeRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static TypeRepository getInstance() {
        if (instance == null) {
            instance = new TypeRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<Type> getType() {
        return Type.class;
    }

    public List<Type> findByDriver(int driverId) {
        String query = "From Type t Where t.driver.id = ?1";
        return executeQuery(query, driverId);
    }
}
