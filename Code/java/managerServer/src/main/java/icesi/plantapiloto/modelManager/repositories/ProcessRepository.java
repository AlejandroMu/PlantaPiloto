package icesi.plantapiloto.modelManager.repositories;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Process;

public class ProcessRepository implements Repository<Process, Integer> {
    private static ProcessRepository instance;
    private EntityManager manager;

    private ProcessRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static ProcessRepository getInstance() {
        if (instance == null) {
            instance = new ProcessRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<Process> getType() {
        return Process.class;
    }
}
