package icesi.plantapiloto.modelManager.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import icesi.plantapiloto.modelManager.enityManager.Manager;

public interface Repository<T, K> {

    public default void save(T element) {
        EntityManager manager = Manager.managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(element);
        manager.getTransaction().commit();
        manager.close();
    };

    public default Optional<T> findById(Class<T> type, K id) {
        EntityManager manager = Manager.managerFactory.createEntityManager();
        Optional<T> element = Optional.ofNullable(manager.find(type, id));
        manager.close();
        return element;
    }

    public default List<T> findAll(Class<T> type) {
        EntityManager manager = Manager.managerFactory.createEntityManager();
        String query = "From " + type.getSimpleName();
        System.out.println(query);
        List<T> values = manager
                .createQuery(query, type)
                .getResultList();
        manager.close();
        return values;
    }

}
