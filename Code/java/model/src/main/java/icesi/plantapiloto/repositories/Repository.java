package icesi.plantapiloto.repositories;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import icesi.plantapiloto.enityManager.Manager;

public interface Repository<T, K> {

    public default void save(T element) {
        EntityManager manager = Manager.managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(element);
        manager.getTransaction().commit();
        manager.close();
    };

    public default Optional<T> findById(K id) {
        EntityManager manager = Manager.managerFactory.createEntityManager();
        Type[] parameterizedType = ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments();
        Class<T> type = (Class<T>) parameterizedType[0];
        manager.close();
        return Optional.ofNullable(manager.find(type, id));
    }

    public default List<T> findAll() {
        EntityManager manager = Manager.managerFactory.createEntityManager();
        Type[] parameterizedType = ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments();
        Class<T> type = (Class<T>) parameterizedType[0];
        List<T> values = manager
                .createQuery("FROM :tableName", type)
                .setParameter("tableName", type.getTypeName())
                .getResultList();
        manager.close();
        return values;
    }

}
