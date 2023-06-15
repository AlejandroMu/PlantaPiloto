package icesi.plantapiloto.common.mappers;

import java.util.List;
import java.util.stream.Collectors;

import icesi.plantapiloto.common.dtos.InstructionDTO;
import icesi.plantapiloto.common.dtos.ProcessDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Instruction;
import icesi.plantapiloto.common.model.Process;
import icesi.plantapiloto.common.model.ProcessAsset;

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
                entity.getWorkSpaceBean().getId(), null, null);
        List<Instruction> instructions = entity.getInstructions();
        if (instructions != null) {
            dto.instructions = InstructionMapper.getInstance().asEntityDTO(instructions)
                    .toArray(new InstructionDTO[instructions.size()]);
        }
        List<ProcessAsset> pAssets = entity.getProcessAssets();
        List<Asset> assets = pAssets == null ? null
                : pAssets.stream().map(pa -> pa.getAsset()).collect(Collectors.toList());
        if (assets != null) {
            dto.assets = AssetMapper.getInstance().asEntityDTO(assets).toArray(new AssetDTO[assets.size()]);
        }
        return dto;
    }

}
