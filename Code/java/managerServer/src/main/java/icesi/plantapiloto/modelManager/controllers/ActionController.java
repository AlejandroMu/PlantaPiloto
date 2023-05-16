package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.ActionManagerController;
import icesi.plantapiloto.common.dtos.ActionDTO;
import icesi.plantapiloto.common.mappers.ActionMapper;
import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.modelManager.services.ActionService;

public class ActionController implements ActionManagerController {

    private ActionService service;

    /**
     * @param service the service to set
     */
    public void setService(ActionService service) {
        this.service = service;
    }

    @Override
    public int saveAction(String nameTec, String displayName, String expression, Current current) {
        return service.saveAction(nameTec, displayName, expression);
    }

    @Override
    public ActionDTO[] findActions(Current current) {
        List<Action> actions = service.getActions();
        return ActionMapper.getInstance().asEntityDTO(actions).toArray(ActionDTO[]::new);
    }

    @Override
    public ActionDTO[] findActionByNameMatch(String namepattern, Current current) {
        List<Action> actions = service.getActionsByNameMatch(namepattern);
        return ActionMapper.getInstance().asEntityDTO(actions).toArray(ActionDTO[]::new);
    }

}
