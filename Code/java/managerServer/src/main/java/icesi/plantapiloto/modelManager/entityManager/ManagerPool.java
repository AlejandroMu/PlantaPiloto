package icesi.plantapiloto.modelManager.entityManager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagerPool {
    private EntityManagerFactory managerFactory;
    private List<EntityManager> entityManagers;
    private String nameUnit;
    private int nEntities;

    public ManagerPool() {
        entityManagers = new ArrayList<>();
        nameUnit = "planta";
        nEntities = 5;
        managerFactory = Persistence.createEntityManagerFactory(nameUnit);
        for (int i = 0; i < nEntities; i++) {
            entityManagers.add(managerFactory.createEntityManager());
        }
    }

    public EntityManager getManager(boolean isT) {
        synchronized (entityManagers) {
            if (!isT) {
                return entityManagers.get(0);
            }
            EntityManager current = null;
            while (current == null) {
                for (int i = 0; i < entityManagers.size(); i++) {
                    EntityManager tmp = entityManagers.get(i);
                    if (!tmp.getTransaction().isActive()) {
                        current = tmp;
                        current.getTransaction().begin();
                        break;
                    }

                }
                if (current == null) {
                    Thread.yield();
                }
            }
            return current;

        }
    }

}
