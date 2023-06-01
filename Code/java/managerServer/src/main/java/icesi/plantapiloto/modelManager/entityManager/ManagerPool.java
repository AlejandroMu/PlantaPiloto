package icesi.plantapiloto.modelManager.entityManager;

import java.util.ArrayDeque;
import java.util.Queue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagerPool {
    private static EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("planta");;
    private static Queue<EntityManager> entityManagers;

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

    public static void init(int j) {
        entityManagers = new ArrayDeque<>();
        for (int i = 0; i < j; i++) {
            entityManagers.add(managerFactory.createEntityManager());
        }
    }

}
