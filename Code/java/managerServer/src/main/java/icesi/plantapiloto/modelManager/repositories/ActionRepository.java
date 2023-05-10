package icesi.plantapiloto.modelManager.repositories;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Action;

public class ActionRepository implements Repository<Action, String> {
    private static ActionRepository instance;
    private EntityManager manager;

    private ActionRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static ActionRepository getInstance() {
        if (instance == null) {
            instance = new ActionRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<Action> getType() {
        return Action.class;
    }

}
