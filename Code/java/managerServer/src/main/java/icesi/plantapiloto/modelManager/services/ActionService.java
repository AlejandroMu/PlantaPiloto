package icesi.plantapiloto.modelManager.services;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.modelManager.repositories.ActionRepository;

public class ActionService {

    private ActionRepository actionRepository;

    public ActionService() {
        actionRepository = ActionRepository.getInstance();
    }

    public int saveAction(String nameTec, String displayName, String expression, EntityManager manager) {
        Action action = new Action();
        action.setFunctionAct(expression);
        action.setNameTech(nameTec);
        action.setNameUser(displayName);
        actionRepository.save(action, manager);
        return action.getId();
    }

    public Action getAction(int actionId, EntityManager manager) {
        return actionRepository.findById(actionId, manager).get();
    }

    public List<Action> getActions(EntityManager manager) {
        return actionRepository.findAll(manager);
    }

    public List<Action> getActionsByNameMatch(String namepattern, EntityManager manager) {
        return actionRepository.findByNameMatch(namepattern, manager);
    }

}
