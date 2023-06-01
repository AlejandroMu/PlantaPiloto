package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Action;

public class ActionRepository implements Repository<Action, Integer> {
    private static ActionRepository instance;

    private ActionRepository() {
    }

    public static ActionRepository getInstance() {
        if (instance == null) {
            instance = new ActionRepository();
        }
        return instance;
    }

    @Override
    public Class<Action> getType() {
        return Action.class;
    }

    public List<Action> findByNameMatch(String namepattern, EntityManager manager) {
        String query = "From Action a Where a.nameTech Like CONCAT('%', ?1, '%') OR a.nameUser Like CONCAT('%', ?1, '%')";
        return executeQuery(manager, query, namepattern);
    }

}
