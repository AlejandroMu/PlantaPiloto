package icesi.plantapiloto.common.mappers;

import java.util.List;

import icesi.plantapiloto.common.dtos.ExecutionDTO;
import icesi.plantapiloto.common.dtos.ExecutionInstructionDTO;
import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.ExecutionInstruction;

public class ExecutionMapper implements Maper<Execution, ExecutionDTO> {
    private static ExecutionMapper instance;

    public static ExecutionMapper getInstance() {
        if (instance == null) {
            instance = new ExecutionMapper();
        }
        return instance;
    }

    private ExecutionMapper() {

    }

    @Override
    public ExecutionDTO asEntityDTO(Execution entity) {
        ExecutionDTO dto = new ExecutionDTO(entity.getId(), entity.getProcessBean().getId(),
                entity.getProcessBean().getName(), entity.getOperUsername(), entity.getStartDate().getTime(),
                entity.getEndDate() != null ? entity.getEndDate().getTime() : -1, entity.getStatus(),
                null);
        List<ExecutionInstruction> exeIns = entity.getExecutionInstructions();
        if (exeIns != null) {
            ExecutionInstructionDTO logs[] = ExecutionInstructionMapper.getInstance().asEntityDTO(exeIns)
                    .toArray(new ExecutionInstructionDTO[exeIns.size()]);

            dto.logs = logs;
        }
        return dto;

    }

}
