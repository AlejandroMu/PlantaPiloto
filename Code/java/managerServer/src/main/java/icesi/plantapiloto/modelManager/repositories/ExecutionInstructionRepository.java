package icesi.plantapiloto.modelManager.repositories;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.ExecutionInstruction;

public class ExecutionInstructionRepository implements Repository<ExecutionInstruction, Integer> {

    private EntityManager manager;
    private static ExecutionInstructionRepository instance;

    private ExecutionInstructionRepository() {
        manager = managerFactory.createEntityManager();
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

    @Override
    public EntityManager getManager() {
        return manager;
    }

}
