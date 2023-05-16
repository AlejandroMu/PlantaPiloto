package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.ProcessManagerController;
import icesi.plantapiloto.common.dtos.ExecutionDTO;
import icesi.plantapiloto.common.dtos.ProcessDTO;
import icesi.plantapiloto.common.mappers.ExecutionMapper;
import icesi.plantapiloto.common.mappers.ProcessMaper;
import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.Process;
import icesi.plantapiloto.modelManager.services.ProcessService;

public class ProcessController implements ProcessManagerController {

    private ProcessService service;

    /**
     * @param service the service to set
     */
    public void setService(ProcessService service) {
        this.service = service;
    }

    @Override
    public int startProcess(int processId, String userName, Current current) {
        return service.startProcess(processId, userName);
    }

    @Override
    public void stopExecutionProcess(int execId, Current current) {
        service.stopExecutionProcess(execId);
    }

    @Override
    public void pauseExecutionProcess(int execId, Current current) {
        service.pauseExecutionProcess(execId);
    }

    @Override
    public void continueExecutionProcess(int execId, Current current) {
        service.continueExecutionProcess(execId);

    }

    @Override
    public int saveProcess(String name, String desc, int workSpaceId, Current current) {
        return service.saveProcess(name, desc, workSpaceId);
    }

    @Override
    public void addInstructionToProcess(int instId, int processId, Current current) {
        service.addInstructionToProcess(instId, processId);
    }

    @Override
    public void applyIntructionToExecution(int instId, int execId, Current current) {
        service.applyIntructionToExecution(instId, execId);
    }

    @Override
    public ProcessDTO[] findProcessByWorkSpace(int workSpaceId, Current current) {
        List<Process> processes = service.getProcessesByWorkSpace(workSpaceId);
        return ProcessMaper.getInstance().asEntityDTO(processes).toArray(ProcessDTO[]::new);
    }

    @Override
    public ExecutionDTO[] findExecutions(int processId, long startDate, long endDate, Current current) {
        List<Execution> executions = service.getExecutionByProcessIdAndDate(processId, startDate, endDate);
        return ExecutionMapper.getInstance().asEntityDTO(executions).toArray(ExecutionDTO[]::new);
    }

}
