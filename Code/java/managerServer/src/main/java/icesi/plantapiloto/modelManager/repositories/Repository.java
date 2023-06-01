package icesi.plantapiloto.modelManager.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface Repository<T, K> {

    public default T save(T element, EntityManager manager) {

        manager.persist(element);
        return element;
    };

    public default T update(T element, EntityManager manager) {

        element = manager.merge(element);
        return element;
    };

    public default void deleteById(K id, EntityManager manager) {

        manager.remove(findById(id, manager).get());

    }

    public default Optional<T> findById(K id, EntityManager manager) {
        Class<T> type = getType();
        Optional<T> element = Optional.ofNullable(manager.find(type, id));
        return element;
    }

    public default List<T> findAll(EntityManager manager) {
        Class<T> type = getType();
        String query = "From " + type.getSimpleName();
        List<T> values = manager
                .createQuery(query, type)
                .getResultList();

        return values;
    }

    public default List<T> executeQuery(EntityManager manager, String query, Object... params) {
        return executeQuery(manager, query, false, params);
    }

    @SuppressWarnings("unchecked")
    public default List<T> executeQuery(EntityManager manager, String query, boolean isNative, Object... params) {
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
