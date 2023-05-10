package icesi.plantapiloto.modelManager.repositories;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.WorkSpace;

public class WorkSpaceRepository implements Repository<WorkSpace, Integer> {
    private static WorkSpaceRepository instance;
    private EntityManager manager;

    private WorkSpaceRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static WorkSpaceRepository getInstance() {
        if (instance == null) {
            instance = new WorkSpaceRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<WorkSpace> getType() {
        return WorkSpace.class;
    }
}
