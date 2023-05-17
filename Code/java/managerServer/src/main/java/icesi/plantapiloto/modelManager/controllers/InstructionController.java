package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.InstructionManagerController;
import icesi.plantapiloto.common.dtos.ActionDTO;
import icesi.plantapiloto.common.dtos.InstructionDTO;
import icesi.plantapiloto.common.mappers.ActionMapper;
import icesi.plantapiloto.common.mappers.InstructionMapper;
import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.common.model.Instruction;
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
        return service.saveInstruction(nameTec, predicate, type);
    }

    @Override
    public void addActionToInstruction(int actionId, int instId, Current current) {
        service.addActionToInstruction(actionId, instId);
    }

    @Override
    public InstructionDTO[] findInstructions(Current current) {
        List<Instruction> instructions = service.getInstructions();
        return InstructionMapper.getInstance().asEntityDTO(instructions).toArray(InstructionDTO[]::new);
    }

    @Override
    public InstructionDTO[] findInstructionsByNameMatch(String namepattern, Current current) {
        List<Instruction> instructions = service.getInstructionsByNameMatch(namepattern);
        return InstructionMapper.getInstance().asEntityDTO(instructions).toArray(InstructionDTO[]::new);
    }

    public ActionDTO[] findActions(int intructionId, Current current) {
        List<Action> actions = service.getActions(intructionId);
        return ActionMapper.getInstance().asEntityDTO(actions).toArray(ActionDTO[]::new);
    }
}
