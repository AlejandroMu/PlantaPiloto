package icesi.plantapiloto.modelManager.repositories;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Execution;

public class ExecutionRepository implements Repository<Execution, Integer> {
    private static ExecutionRepository instance;
    private EntityManager manager;

    private ExecutionRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static ExecutionRepository getInstance() {
        if (instance == null) {
            instance = new ExecutionRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<Execution> getType() {
        return Execution.class;
    }
}
