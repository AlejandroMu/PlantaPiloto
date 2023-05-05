package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.WorkSpace;

public class WorkSpaceRepository implements Repository<WorkSpace, Integer> {
    @Override
    public Class<WorkSpace> getType() {
        return WorkSpace.class;
    }
}
