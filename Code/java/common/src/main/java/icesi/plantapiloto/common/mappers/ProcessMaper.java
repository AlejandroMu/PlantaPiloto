package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.ProcessDTO;
import icesi.plantapiloto.common.model.Process;

public class ProcessMaper implements Maper<Process, ProcessDTO> {

    private static ProcessMaper instance;

    public static ProcessMaper getInstance() {
        if (instance == null) {
            instance = new ProcessMaper();
        }
        return instance;
    }

    private ProcessMaper() {

    }

    @Override
    public ProcessDTO asEntityDTO(Process entity) {
        ProcessDTO dto = new ProcessDTO(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getWorkSpaceBean().getId());
        return dto;
    }

}
