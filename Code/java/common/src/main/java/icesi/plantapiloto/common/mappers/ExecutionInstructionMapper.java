package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.ExecutionInstructionDTO;
import icesi.plantapiloto.common.model.ExecutionInstruction;

public class ExecutionInstructionMapper implements Maper<ExecutionInstruction, ExecutionInstructionDTO> {

    private static ExecutionInstructionMapper instance;

    public static ExecutionInstructionMapper getInstance() {
        if (instance == null) {
            instance = new ExecutionInstructionMapper();
        }
        return instance;
    }

    private ExecutionInstructionMapper() {

    }

    @Override
    public ExecutionInstructionDTO asEntityDTO(ExecutionInstruction entity) {
        ExecutionInstructionDTO dto = new ExecutionInstructionDTO(entity.getId(), entity.getExecutionBean().getId(),
                entity.getInstructionBean().getNameTech(), entity.getInstructionBean().getId(),
                entity.getExcTime().getTime());
        return dto;
    }

}
