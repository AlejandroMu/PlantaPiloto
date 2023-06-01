package icesi.plantapiloto.modelManager.services;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.common.model.Instruction;
import icesi.plantapiloto.modelManager.repositories.InstructionRepository;

public class InstructionService {
    private InstructionRepository instructionRepository;

    private ActionService actionService;

    public InstructionService() {
        instructionRepository = InstructionRepository.getInstance();
    }

    /**
     * @param actionService the actionService to set
     */
    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public int saveInstruction(String nameTec, String predicate, String type, EntityManager manager) {
        Instruction instruction = new Instruction();
        instruction.setNameTech(nameTec);
        instruction.setPredicate(predicate);
        instruction.setType(type);
        instructionRepository.save(instruction, manager);
        return instruction.getId();
    }

    public Instruction getInstruction(int instId, EntityManager manager) {
        return instructionRepository.findById(instId, manager).get();
    }

    public void addActionToInstruction(int actionId, int instId, EntityManager manager) {
        Action action = actionService.getAction(actionId, manager);
        Instruction instruction = instructionRepository.findById(instId, manager).get();

        instruction.addAction(action);
        instructionRepository.update(instruction, manager);
    }

    public List<Instruction> getInstructions(EntityManager manager) {
        return instructionRepository.findAll(manager);
    }

    public List<Instruction> getInstructionsByNameMatch(String namepattern, EntityManager manager) {
        return instructionRepository.findByNameMatch(namepattern, manager);
    }

    public List<Action> getActions(int intructionId, EntityManager manager) {
        Instruction instruction = instructionRepository.findById(intructionId, manager).get();
        return instruction.getActions();
    }

}
