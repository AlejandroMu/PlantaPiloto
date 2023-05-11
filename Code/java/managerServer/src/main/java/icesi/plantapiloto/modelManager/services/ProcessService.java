package icesi.plantapiloto.modelManager.services;

import java.sql.Timestamp;

import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.Process;
import icesi.plantapiloto.modelManager.repositories.ExecutionRepository;

public class ProcessService {

    private ExecutionRepository executionRepository;

    public ProcessService() {
        executionRepository = ExecutionRepository.getInstance();
    }

    public Process getProcessOfExeId(int execId) {
        Execution execution = executionRepository.findById(execId).get();

        return execution.getProcessBean();
    }

    public void endProcess(int execId) {
        Execution execution = executionRepository.findById(execId).get();
        execution.setEndDate(new Timestamp(System.currentTimeMillis()));
        executionRepository.save(execution);
    }

}
