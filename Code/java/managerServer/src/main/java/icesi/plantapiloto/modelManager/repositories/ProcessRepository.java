package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.Process;

public class ProcessRepository implements Repository<Process, Integer> {
    @Override
    public Class<Process> getType() {
        return Process.class;
    }
}
