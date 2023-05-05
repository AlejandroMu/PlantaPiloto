package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.Instruction;

public class InstructionRepository implements Repository<Instruction, String> {
    @Override
    public Class<Instruction> getType() {
        return Instruction.class;
    }
}
