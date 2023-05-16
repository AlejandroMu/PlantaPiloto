package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Instruction;

public class InstructionRepository implements Repository<Instruction, Integer> {
    private static InstructionRepository instance;
    private EntityManager manager;

    private InstructionRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static InstructionRepository getInstance() {
        if (instance == null) {
            instance = new InstructionRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
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
