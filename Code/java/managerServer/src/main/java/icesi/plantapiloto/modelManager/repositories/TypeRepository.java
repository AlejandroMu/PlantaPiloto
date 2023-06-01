package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Type;

public class TypeRepository implements Repository<Type, Integer> {
    private static TypeRepository instance;

    private TypeRepository() {
    }

    public static TypeRepository getInstance() {
        if (instance == null) {
            instance = new TypeRepository();
        }
        return instance;
    }

    @Override
    public Class<Type> getType() {
        return Type.class;
    }

    public List<Type> findByDriver(int driverId, EntityManager manager) {
        String query = "From Type t Where t.driver.id = ?1";
        return executeQuery(manager, query, driverId);
    }
}
