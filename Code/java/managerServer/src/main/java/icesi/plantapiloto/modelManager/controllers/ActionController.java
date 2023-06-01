package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import javax.persistence.EntityManager;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.ActionManagerController;
import icesi.plantapiloto.common.dtos.ActionDTO;
import icesi.plantapiloto.common.mappers.ActionMapper;
import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.modelManager.entityManager.ManagerPool;
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
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        int ret = service.saveAction(nameTec, displayName, expression, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public ActionDTO[] findActions(Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Action> actions = service.getActions(manager);
        ActionDTO[] ret = ActionMapper.getInstance().asEntityDTO(actions).toArray(ActionDTO[]::new);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public ActionDTO[] findActionByNameMatch(String namepattern, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Action> actions = service.getActionsByNameMatch(namepattern, manager);
        ActionDTO[] ret = ActionMapper.getInstance().asEntityDTO(actions).toArray(ActionDTO[]::new);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

}
