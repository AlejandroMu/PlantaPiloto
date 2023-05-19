package icesi.plantapiloto.modelManager.repositories;

import java.sql.Timestamp;
import java.util.List;

import icesi.plantapiloto.common.model.Execution;

public class ExecutionRepository implements Repository<Execution, Integer> {
    private static ExecutionRepository instance;

    private ExecutionRepository() {
    }

    public static ExecutionRepository getInstance() {
        if (instance == null) {
            instance = new ExecutionRepository();
        }
        return instance;
    }

    @Override
    public Class<Execution> getType() {
        return Execution.class;
    }

    public List<Execution> findByProcessAndStartDateBetween(int processId, long startDate, long endDate) {
        String query = "From Execution e Where e.processBean.id = ?1 "
                + "AND e.startDate between ?2 AND ?3 ";
        return executeQuery(query, processId, new Timestamp(startDate), new Timestamp(endDate));
    }

    public List<Execution> findExecutionsRunning(int processId) {
        String query = "From Execution e Where e.processBean.id = ?1 "
                + "AND e.endDate IS NULL ";
        return executeQuery(query, processId);
    }
}
