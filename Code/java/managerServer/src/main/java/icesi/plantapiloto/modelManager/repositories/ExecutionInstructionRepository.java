package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.ExecutionInstruction;

public class ExecutionInstructionRepository implements Repository<ExecutionInstruction, Integer> {

    private static ExecutionInstructionRepository instance;

    private ExecutionInstructionRepository() {
    }

    public static ExecutionInstructionRepository getInstance() {
        if (instance == null) {
            instance = new ExecutionInstructionRepository();
        }
        return instance;
    }

    @Override
    public Class<ExecutionInstruction> getType() {
        return ExecutionInstruction.class;
    }

}
