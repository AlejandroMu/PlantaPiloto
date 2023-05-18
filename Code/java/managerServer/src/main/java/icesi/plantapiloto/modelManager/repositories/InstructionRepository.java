package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import icesi.plantapiloto.common.model.Instruction;

public class InstructionRepository implements Repository<Instruction, Integer> {
    private static InstructionRepository instance;

    private InstructionRepository() {
    }

    public static InstructionRepository getInstance() {
        if (instance == null) {
            instance = new InstructionRepository();
        }
        return instance;
    }

    @Override
    public Class<Instruction> getType() {
        return Instruction.class;
    }

    public List<Instruction> findByNameMatch(String namepattern) {
        String query = "From Instruction i Where i.nameTech Like CONCAT('%', ?1, '%')";
        return executeQuery(query, namepattern);
    }
}
