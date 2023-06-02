package icesi.plantapiloto.common.mappers;

import java.util.List;

import icesi.plantapiloto.common.dtos.ActionDTO;
import icesi.plantapiloto.common.dtos.InstructionDTO;
import icesi.plantapiloto.common.model.Action;
import icesi.plantapiloto.common.model.Instruction;

public class InstructionMapper implements Maper<Instruction, InstructionDTO> {
    private static InstructionMapper instance;

    public static InstructionMapper getInstance() {
        if (instance == null) {
            instance = new InstructionMapper();
        }
        return instance;
    }

    private InstructionMapper() {

    }

    @Override
    public InstructionDTO asEntityDTO(Instruction entity) {
        InstructionDTO dto = new InstructionDTO(entity.getId(), entity.getNameTech(), entity.getPredicate(),
                entity.getType(), null);
        List<Action> actions = entity.getActions();
        if (actions != null) {
            ActionDTO[] actionDTOs = ActionMapper.getInstance().asEntityDTO(actions)
                    .toArray(new ActionDTO[actions.size()]);
            dto.actions = actionDTOs;
        }
        return dto;
    }
}
