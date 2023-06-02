package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import javax.persistence.EntityManager;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.InstructionManagerController;
import icesi.plantapiloto.common.dtos.ActionDTO;
import icesi.plantapiloto.common.dtos.InstructionDTO;
import icesi.plantapiloto.common.mappers.ActionMapper;
import icesi.plantapiloto.common.mappers.InstructionMapper;
import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.common.model.Instruction;
import icesi.plantapiloto.modelManager.entityManager.ManagerPool;
import icesi.plantapiloto.modelManager.services.InstructionService;

public class InstructionController implements InstructionManagerController {

    private InstructionService service;

    /**
     * @param service the service to set
     */
    public void setService(InstructionService service) {
        this.service = service;
    }

    @Override
    public int saveInstruction(String nameTec, String predicate, String type, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        int ret = service.saveInstruction(nameTec, predicate, type, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public void addActionToInstruction(int actionId, int instId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.addActionToInstruction(actionId, instId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public InstructionDTO[] findInstructions(Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Instruction> instructions = service.getInstructions(manager);
        InstructionDTO[] ret = InstructionMapper.getInstance().asEntityDTO(instructions)
                .toArray(new InstructionDTO[instructions.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public InstructionDTO[] findInstructionsByNameMatch(String namepattern, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Instruction> instructions = service.getInstructionsByNameMatch(namepattern, manager);
        InstructionDTO[] ret = InstructionMapper.getInstance().asEntityDTO(instructions)
                .toArray(new InstructionDTO[instructions.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    public ActionDTO[] findActions(int intructionId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Action> actions = service.getActions(intructionId, manager);
        ActionDTO[] ret = ActionMapper.getInstance().asEntityDTO(actions).toArray(new ActionDTO[actions.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;

    }
}
