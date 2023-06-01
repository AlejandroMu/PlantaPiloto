package icesi.plantapiloto.modelManager.entityManager;

import java.util.ArrayDeque;
import java.util.Queue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagerPool {
    private EntityManagerFactory managerFactory;
    private static Queue<EntityManager> entityManagers;
    private String nameUnit;
    private int nEntities;

    public ManagerPool() {
        entityManagers = new ArrayDeque<>();
        nameUnit = "planta";
        nEntities = 10;
        managerFactory = Persistence.createEntityManagerFactory(nameUnit);
        for (int i = 0; i < nEntities; i++) {
            entityManagers.add(managerFactory.createEntityManager());
        }
    }

    public static EntityManager getManager() {
        synchronized (entityManagers) {
            while (entityManagers.isEmpty()) {
                Thread.yield();
            }

            return entityManagers.poll();

        }
    }

    public static void close(EntityManager manager) {
        manager.clear();
        entityManagers.add(manager);
    }

}
