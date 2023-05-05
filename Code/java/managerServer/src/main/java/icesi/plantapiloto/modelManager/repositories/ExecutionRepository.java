package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.Execution;

public class ExecutionRepository implements Repository<Execution, Integer> {
    @Override
    public Class<Execution> getType() {
        return Execution.class;
    }
}
