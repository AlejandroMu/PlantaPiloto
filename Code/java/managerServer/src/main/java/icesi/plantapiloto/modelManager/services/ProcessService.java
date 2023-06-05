package icesi.plantapiloto.modelManager.services;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.consts.AssetState;
import icesi.plantapiloto.common.consts.ExecutionState;
import icesi.plantapiloto.common.controllers.DriverAssetPrx;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.ExecutionInstruction;
import icesi.plantapiloto.common.model.Instruction;
import icesi.plantapiloto.common.model.Process;
import icesi.plantapiloto.common.model.ProcessAsset;
import icesi.plantapiloto.common.model.ProcessAssetPK;
import icesi.plantapiloto.modelManager.repositories.ExecutionInstructionRepository;
import icesi.plantapiloto.modelManager.repositories.ExecutionRepository;
import icesi.plantapiloto.modelManager.repositories.ProcessAssetRepository;
import icesi.plantapiloto.modelManager.repositories.ProcessRepository;
import icesi.plantapiloto.modelManager.repositories.WorkSpaceRepository;

public class ProcessService {

    private MeasurementManagerControllerPrx callback;

    private ExecutionRepository executionRepository;
    private ProcessRepository processRepository;
    private WorkSpaceRepository workSpaceRepository;
    private ExecutionInstructionRepository executionInstructionRepository;
    private ProcessAssetRepository processAssetRepository;

    private InstructionService instructionService;
    private AssetService assetService;
    private DriverService driverService;

    public ProcessService() {
        executionRepository = ExecutionRepository.getInstance();
        processRepository = ProcessRepository.getInstance();
        workSpaceRepository = WorkSpaceRepository.getInstance();
        executionInstructionRepository = ExecutionInstructionRepository.getInstance();
        processAssetRepository = ProcessAssetRepository.getInstance();
    }

    /**
     * @param driverService the driverService to set
     */
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * @param instructionService the instructionService to set
     */
    public void setInstructionService(InstructionService instructionService) {
        this.instructionService = instructionService;
    }

    /**
     * @param assetService the assetService to set
     */
    public void setAssetService(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(MeasurementManagerControllerPrx callback) {
        this.callback = callback;
    }

    public Process getProcessOfExeId(int execId, EntityManager manager) {
        Execution execution = executionRepository.findById(execId, manager).get();

        return execution.getProcessBean();
    }

    public int startProcess(int processId, String name, EntityManager manager) {
        Process process = processRepository.findById(processId, manager).get();
        List<Execution> exs = executionRepository.findExecutionActives(processId,
                manager);
        if (exs.size() > 0) {
            System.out.println(
                    "Process has a execution active: " + exs.get(0).getId() + " status: " + exs.get(0).getStatus());
            return -1;
        }
        Execution newEx = new Execution();
        newEx.setProcessBean(process);
        newEx.setStartDate(new Timestamp(System.currentTimeMillis()));
        newEx.setOperUsername(name);
        newEx.setStatus(ExecutionState.PAUSED.getValue());
        executionRepository.save(newEx, manager);
        runExecution(newEx, manager);
        if (newEx.getStatus().equals(ExecutionState.RUNNING.getValue())) {
            return newEx.getId();
        } else {
            return -1;
        }
    }

    public void runExecution(Execution execution, EntityManager manager) {
        if (!execution.getStatus().equals(ExecutionState.PAUSED.getValue())) {
            System.out.println("execution can't run again");
            return;
        }
        Process process = execution.getProcessBean();
        List<ProcessAsset> procesassets = process.getProcessAssets();
        AssetMapper mapper = AssetMapper.getInstance();
        if (procesassets != null && procesassets.size() > 0) {
            boolean running = false;
            for (ProcessAsset processAsset : procesassets) {
                Asset asset = processAsset.getAsset();
                if (asset.getAssetState().equals(AssetState.ACTIVE.getValue())) {
                    long period = processAsset.getDelayRead();
                    Driver driver = asset.getTypeBean().getDriver();
                    DriverAssetPrx prx = driverService.getDriverProxy(driver);
                    if (prx != null) {
                        prx.readAsset(mapper.asEntityDTO(asset), callback, execution.getId(), period, false);
                        asset.setAssetState(AssetState.BUSY.getValue());
                        assetService.updateAsset(asset, manager);
                        running = true;
                    }

                } else {
                    System.out.println("The asset is not avaible");
                }
            }
            if (running) {
                execution.setStatus(ExecutionState.RUNNING.getValue());
            } else {
                execution.setStatus(ExecutionState.STOPED.getValue());
            }

        } else {
            execution.setStatus(ExecutionState.STOPED.getValue());
            System.out.println("Process dont have assets");
        }
        executionRepository.update(execution, manager);

    }

    public void runExecution(int execution, EntityManager manager) {
        runExecution(executionRepository.findById(execution, manager).get(), manager);
    }

    public void stopExecutionProcess(int execId, EntityManager manager) {
        stopCapure(executionRepository.findById(execId, manager).get(), manager);
    }

    public void stopCapure(Execution execution, EntityManager manager) {
        if (execution.getStatus().equals(ExecutionState.STOPED.getValue())) {
            System.out.println("The execution was already stopped");
            return;
        }

        Process process = execution.getProcessBean();
        if (process.getProcessAssets() != null && process.getProcessAssets().size() > 0) {
            Asset[] assets = process.getProcessAssets().stream().map(pa -> pa.getAsset()).toArray(Asset[]::new);
            HashSet<Integer> drivId = new HashSet<>();
            for (Asset asset : assets) {
                Driver driver = asset.getTypeBean().getDriver();
                if (!drivId.contains(driver.getId())) {
                    DriverAssetPrx prx = driverService.getDriverProxy(driver);
                    if (prx != null) {
                        prx.stopRead(execution.getId());
                        drivId.add(driver.getId());
                        asset.setAssetState(AssetState.ACTIVE.getValue());
                        assetService.updateAsset(asset, manager);
                    }
                }

            }
            execution.setStatus(ExecutionState.STOPED.getValue());
            execution.setEndDate(new Timestamp(System.currentTimeMillis()));
            executionRepository.update(execution, manager);
        }
    }

    public void pauseExecutionProcess(int execId, EntityManager manager) {
        pauseExecutionProcess(executionRepository.findById(execId, manager).get(), manager);
    }

    public void pauseExecutionProcess(Execution execution, EntityManager manager) {
        if (!execution.getStatus().equals(ExecutionState.RUNNING.getValue())) {
            System.out.println("The execution can't be paused");
            return;
        }
        Process process = execution.getProcessBean();
        if (process.getProcessAssets() != null && process.getProcessAssets().size() > 0) {
            Asset[] assets = process.getProcessAssets().stream().map(pa -> pa.getAsset()).toArray(Asset[]::new);
            HashSet<Integer> drivId = new HashSet<>();
            for (Asset asset : assets) {
                Driver driver = asset.getTypeBean().getDriver();
                if (!drivId.contains(driver.getId())) {
                    DriverAssetPrx prx = driverService.getDriverProxy(driver);
                    if (prx != null) {
                        prx.pauseReader(execution.getId());
                        drivId.add(driver.getId());
                    }
                }
            }
            execution.setStatus(ExecutionState.PAUSED.getValue());
            executionRepository.update(execution, manager);
        }
    }

    public void continueExecutionProcess(int execId, EntityManager manager) {
        continueExecutionProcess(executionRepository.findById(execId, manager).get(), manager);

    }

    public void continueExecutionProcess(Execution execution, EntityManager manager) {
        if (!execution.getStatus().equals(ExecutionState.PAUSED.getValue())) {
            System.out.println("The execution can't continue");
            return;
        }
        Process process = execution.getProcessBean();

        if (process.getProcessAssets() != null && process.getProcessAssets().size() > 0) {
            Asset[] assets = process.getProcessAssets().stream().map(pa -> pa.getAsset()).toArray(Asset[]::new);
            HashSet<Integer> drivId = new HashSet<>();
            for (Asset asset : assets) {
                Driver driver = asset.getTypeBean().getDriver();
                if (!drivId.contains(driver.getId())) {
                    DriverAssetPrx prx = driverService.getDriverProxy(driver);
                    if (prx != null) {
                        prx.resumeReader(execution.getId());
                        drivId.add(driver.getId());

                    }
                }

            }
            execution.setStatus(ExecutionState.RUNNING.getValue());
            executionRepository.update(execution, manager);
        }
    }

    public int saveProcess(String name, String desc, int workSpaceId, EntityManager manager) {
        Process process = new Process();
        process.setDescription(desc);
        process.setName(name);
        process.setWorkSpaceBean(workSpaceRepository.findById(workSpaceId, manager).get());
        processRepository.save(process, manager);
        return process.getId();
    }

    public void addInstructionToProcess(int instId, int processId, EntityManager manager) {
        Instruction instruction = instructionService.getInstruction(instId, manager);
        Process process = processRepository.findById(processId, manager).get();

        process.addInstruction(instruction);

        processRepository.update(process, manager);

    }

    public void applyIntructionToExecution(int instId, int execId, EntityManager manager) {
        Instruction ins = instructionService.getInstruction(instId, manager);
        Execution exc = executionRepository.findById(execId, manager).get();
        applyIntructionToExecution(ins, exc, manager);
    }

    public void applyIntructionToExecution(Instruction instId, Execution execId, EntityManager manager) {
        boolean contains = instId.getProcesses().stream().anyMatch(p -> p.getId() == execId.getProcessBean().getId());
        if (contains) {
            ExecutionInstruction exeIns = new ExecutionInstruction();
            exeIns.setExcTime(new Timestamp(System.currentTimeMillis()));
            exeIns.setExecutionBean(execId);
            exeIns.setInstructionBean(instId);
            executionInstructionRepository.save(exeIns, manager);
        } else {
            System.out.println("La instrucción no es aplicable a la ejecución, no pertenecen al mismo proceso");
            // TODO: error report
        }
    }

    public List<Process> getProcessesByWorkSpace(int workSpaceId, EntityManager manager) {

        return processRepository.findByWorkSpace(workSpaceId, manager);
    }

    public List<Execution> getExecutionByProcessIdAndDateStartBetween(int processId, long startDate, long endDate,
            String run, EntityManager manager) {
        return executionRepository.findByProcessAndStartDateBetween(processId, startDate, endDate, run, manager);
    }

    public void addAssetToProcess(int asset, int processId, long period, EntityManager manager) {
        Asset asset2 = assetService.getAssetById(asset, manager);
        Process process = processRepository.findById(processId, manager).get();
        if (asset2.getWorkSpace().getId() == process.getWorkSpaceBean().getId()) {
            ProcessAsset v = new ProcessAsset();
            ProcessAssetPK pk = new ProcessAssetPK();
            pk.setAssetId(asset);
            pk.setProcessId(processId);
            v.setId(pk);
            v.setAsset(asset2);
            v.setDelayRead(period);
            v.setProcess(process);
            processAssetRepository.save(v, manager);

        } else {
            System.out.println("el asset no es añadible al proceso, no pertenecen al mismo workspace");
            // TODO: error report
        }
    }

    public List<ProcessAsset> getAssetsOfProcess(int processId, EntityManager manager) {
        Process process = processRepository.findById(processId, manager).get();
        return process.getProcessAssets();
    }

    public List<Execution> findExecutionsRunning(int processId, EntityManager manager) {
        ExecutionState.valueOf(null);
        return executionRepository.findExecutionByState(processId, ExecutionState.RUNNING.getValue(), manager);
    }

    public void updateAssetToProcess(int asset, int processId, long period, EntityManager manager) {
        ProcessAssetPK pk = new ProcessAssetPK();
        pk.setAssetId(asset);
        pk.setProcessId(processId);
        ProcessAsset pa = processAssetRepository.findById(pk, manager).get();
        pa.setDelayRead(period);
        processAssetRepository.update(pa, manager);

    }

    public void removeAssetToProcess(int asset, int processId, EntityManager manager) {
        ProcessAssetPK pk = new ProcessAssetPK();
        pk.setAssetId(asset);
        pk.setProcessId(processId);
        processAssetRepository.deleteById(pk, manager);
    }

}
