package icesi.plantapiloto.modelManager.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public interface Repository<T, K> {
    public static EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("planta");

    public default T save(T element) {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(element);
        manager.getTransaction().commit();
        manager.close();
        return element;
    };

    public default Optional<T> findById(K id) {
        Class<T> type = getType();
        EntityManager manager = managerFactory.createEntityManager();
        Optional<T> element = Optional.ofNullable(manager.find(type, id));
        manager.close();
        return element;
    }

    public default List<T> findAll() {
        Class<T> type = getType();

        EntityManager manager = managerFactory.createEntityManager();
        String query = "From " + type.getSimpleName();
        System.out.println(query);
        List<T> values = manager
                .createQuery(query, type)
                .getResultList();
        manager.close();
        return values;
    }

    public default List<T> executeQuery(String query, Object... params) {
        return executeQuery(query, false, params);
    }

    @SuppressWarnings("unchecked")
    public default List<T> executeQuery(String query, boolean isNative, Object... params) {
        EntityManager manager = managerFactory.createEntityManager();
        Class<?> type = getType();
        Query queryMan = null;
        if (isNative) {
            queryMan = manager.createNativeQuery(query, type);
        } else {
            queryMan = manager.createQuery(query, type);
        }
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                queryMan = queryMan.setParameter((i + 1), params[i]);

            }
        }
        List<T> resutl = (List<T>) queryMan.getResultList();
        return resutl;
    }

    public Class<T> getType();

}
