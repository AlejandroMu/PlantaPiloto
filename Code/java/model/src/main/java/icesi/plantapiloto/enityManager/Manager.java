package icesi.plantapiloto.enityManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.zeroc.Ice.Current;

/**
 * ChannelManager
 */
public class Manager {
    public static final EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("planta");
    public static final EntityManager manager = managerFactory.createEntityManager();

    public void begin(Current current) {
        manager.getTransaction().begin();
    }

    public void commit(Current current) {
        manager.getTransaction().commit();
    }

}