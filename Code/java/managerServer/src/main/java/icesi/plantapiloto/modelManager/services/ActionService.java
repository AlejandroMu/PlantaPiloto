package icesi.plantapiloto.modelManager.services;

import java.util.List;

import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.modelManager.repositories.ActionRepository;

public class ActionService {

    private ActionRepository actionRepository;

    public ActionService() {
        actionRepository = ActionRepository.getInstance();
    }

    public int saveAction(String nameTec, String displayName, String expression) {
        Action action = new Action();
        action.setFunctionAct(expression);
        action.setNameTech(nameTec);
        action.setNameUser(displayName);
        actionRepository.save(action);
        return action.getId();
    }

    public Action getAction(int actionId) {
        return actionRepository.findById(actionId).get();
    }

    public List<Action> getActions() {
        return actionRepository.findAll();
    }

    public List<Action> getActionsByNameMatch(String namepattern) {
        return actionRepository.findByNameMatch(namepattern);
    }

}
