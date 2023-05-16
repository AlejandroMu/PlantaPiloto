package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.WorkSpaceDTO;
import icesi.plantapiloto.common.model.WorkSpace;

public class WorkSpaceMapper implements Maper<WorkSpace, WorkSpaceDTO> {
    private static WorkSpaceMapper instance;

    public static WorkSpaceMapper getInstance() {
        if (instance == null) {
            instance = new WorkSpaceMapper();
        }
        return instance;
    }

    private WorkSpaceMapper() {

    }

    @Override
    public WorkSpaceDTO asEntityDTO(WorkSpace entity) {
        WorkSpaceDTO dto = new WorkSpaceDTO(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getWorkSpace() == null ? -1 : entity.getWorkSpace().getId());
        return dto;
    }

}
