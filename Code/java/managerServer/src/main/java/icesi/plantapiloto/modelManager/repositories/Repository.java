package icesi.plantapiloto.modelManager.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import icesi.plantapiloto.modelManager.entityManager.ManagerPool;

public interface Repository<T, K> {

    public ManagerPool pool = new ManagerPool();

    public default EntityManager getManager(boolean isT) {
        return pool.getManager(isT);
    }

    public default T save(T element) {

        EntityManager manager = getManager(true);
        manager.persist(element);
        manager.getTransaction().commit();
        manager.clear();
        return element;
    };

    public default T update(T element) {

        EntityManager manager = getManager(true);
        element = manager.merge(element);
        manager.getTransaction().commit();
        manager.clear();
        return element;
    };

    public default void deleteById(K id) {
        EntityManager manager = getManager(true);
        boolean isActive = manager.getTransaction().isActive();
        if (!isActive) {
            manager.getTransaction().begin();
        }
        manager.remove(findById(id).get());
        manager.getTransaction().commit();
        manager.clear();

    }

    public default Optional<T> findById(K id) {
        EntityManager manager = getManager(false);
        Class<T> type = getType();
        Optional<T> element = Optional.ofNullable(manager.find(type, id));
        return element;
    }

    public default List<T> findAll() {
        EntityManager manager = getManager(false);
        Class<T> type = getType();
        String query = "From " + type.getSimpleName();
        List<T> values = manager
                .createQuery(query, type)
                .getResultList();

        return values;
    }

    public default List<T> executeQuery(String query, Object... params) {
        return executeQuery(query, false, params);
    }

    @SuppressWarnings("unchecked")
    public default List<T> executeQuery(String query, boolean isNative, Object... params) {
        EntityManager manager = getManager(false);
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
