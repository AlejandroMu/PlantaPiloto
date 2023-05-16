package icesi.plantapiloto.modelManager.services;

import java.util.List;

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

    public int saveInstruction(String nameTec, String predicate, String type) {
        Instruction instruction = new Instruction();
        instruction.setNameTech(nameTec);
        instruction.setPredicate(predicate);
        instruction.setType(type);
        instructionRepository.save(instruction);
        return instruction.getId();
    }

    public Instruction getInstruction(int instId) {
        return instructionRepository.findById(instId).get();
    }

    public void addActionToInstruction(int actionId, int instId) {
        Action action = actionService.getAction(actionId);
        Instruction instruction = instructionRepository.findById(instId).get();

        instruction.addAction(action);
        instructionRepository.update(instruction);
    }

    public List<Instruction> getInstructions() {
        return instructionRepository.findAll();
    }

    public List<Instruction> getInstructionsByNameMatch(String namepattern) {
        return instructionRepository.findByNameMatch(namepattern);
    }

}
