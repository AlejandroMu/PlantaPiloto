package icesi.plantapiloto.cli.processCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.ProcessManagerControllerPrx;
import icesi.plantapiloto.common.dtos.ExecutionDTO;

public class ProcessCLI implements CommanLineInterface {
    private ProcessManagerControllerPrx prx;

    public ProcessCLI(Communicator communicator) {
        prx = ProcessManagerControllerPrx.checkedCast(communicator.stringToProxy(System.getProperty("process")));
    }

    @Override
    public String usage(String pading) {
        StringBuilder builder = new StringBuilder(pading + "usage: process help || process command\n");
        builder.append(pading + " process add -n {name}* -d {desc}* -w {workId}*\n")
                .append(pading + " process list -w {workSpace}*\n")
                .append(pading + " process start -p {processId}* -o {operName}*\n")
                .append(pading + " process stop -e {execId}*\n")
                .append(pading + " process pause -e {execId}*\n")
                .append(pading + " process continue -e {execId}*\n")
                .append(pading
                        + " process execution list -p {processId}* -s {startD}* -e {endDate} -st {running | paused | stoped}\n")
                .append(pading + " process asset add -p {processId}* -a {assetId}* -f {readFreq}*\n")
                .append(pading + " process asset update -p {processId}* -a {assetId}* -f {readFreq}*\n")
                .append(pading + " process asset list -p {processId}*\n")
                .append(pading + " process instruction add -p {processId}* -i {instId}*\n")
                .append(pading + " process instruction apply -e {execId}* -i {instId}*\n");
        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {
        if (command.matches("process\\s+help.*")) {
            writer.write(usage(" "));
        } else if (command.matches("process\\s+add.*")) {
            writer.write(processAddController(command));
        } else if (command.matches("process\\s+start.*")) {
            writer.write(processStartController(command));
        } else if (command.matches("process\\s+stop.*")) {
            writer.write(processStopController(command));
        } else if (command.matches("process\\s+pause.*")) {
            writer.write(processPauseController(command));
        } else if (command.matches("process\\s+continue.*")) {
            writer.write(processContinueController(command));
        } else if (command.matches("process\\s+list.*")) {
            writer.write(processListController(command));
        } else if (command.matches("process\\s+execution\\s+list.*")) {
            writer.write(processExecutionListController(command));
        } else if (command.matches("process\\s+instruction\\s+add.*")) {
            writer.write(processInstAddController(command));
        } else if (command.matches("process\\s+instruction\\s+apply.*")) {
            writer.write(processInstApplyController(command));
        } else if (command.matches("process\\s+asset\\s+add.*")) {
            writer.write(processAssetAddController(command));
        } else if (command.matches("process\\s+asset\\s+list.*")) {
            writer.write(processAsseListController(command));
        } else if (command.matches("process\\s+asset\\s+update.*")) {
            writer.write(processAssetUpdateController(command));
        } else {
            return false;
        }
        return true;
    }

    private String processAssetUpdateController(String command) {
        Map<String, String> props = parseOptions(command);
        String p = props.get("-p");
        String a = props.get("-a");
        String period = props.get("-f");
        if (!anyNull(p, a, period)) {
            prx.updateAssetToProcess(Integer.parseInt(a), Integer.parseInt(p), Long.parseLong(period));
            return "Successfull, added asset to process\n";
        }
        return errorMessage();
    }

    private String processAsseListController(String command) {
        Map<String, String> props = parseOptions(command);
        String proc = props.get("-p");
        if (proc != null) {
            return encoderList(prx.getAssetOfProcess(Integer.parseInt(proc)));
        }
        return errorMessage();
    }

    private String processAssetAddController(String command) {
        Map<String, String> props = parseOptions(command);
        String p = props.get("-p");
        String a = props.get("-a");
        String period = props.get("-f");
        if (!anyNull(p, a, period)) {
            prx.addAssetToProcess(Integer.parseInt(a), Integer.parseInt(p), Long.parseLong(period));
            return "Successfull, added asset to process\n";
        }
        return errorMessage();
    }

    private String processInstApplyController(String command) {
        Map<String, String> props = parseOptions(command);
        String e = props.get("-e");
        String i = props.get("-i");
        if (!anyNull(e, i)) {
            prx.applyIntructionToExecution(Integer.parseInt(i), Integer.parseInt(e));
            return "Successfull, added instruction to execution\n";

        }
        return errorMessage();
    }

    private String processInstAddController(String command) {
        Map<String, String> props = parseOptions(command);
        String p = props.get("-p");
        String i = props.get("-i");
        if (!anyNull(p, i)) {
            prx.addInstructionToProcess(Integer.parseInt(i), Integer.parseInt(p));
            return "Successfull, added instruction to process\n";
        }
        return errorMessage();
    }

    private String processExecutionListController(String command) {
        Map<String, String> props = parseOptions(command);
        String p = props.get("-p");
        String s = props.get("-s");
        String e = props.get("-e");
        String r = props.get("-st");

        if (!anyNull(p, s)) {
            long time = e != null ? Long.parseLong(e) : System.currentTimeMillis();
            ExecutionDTO[] executionDTOs = prx.findExecutions(Integer.parseInt(p), Long.parseLong(s), time, r);
            return encoderList(executionDTOs);
        }
        return errorMessage();
    }

    private String processListController(String command) {
        Map<String, String> props = parseOptions(command);
        String w = props.get("-w");
        if (w != null) {
            return encoderList(prx.findProcessByWorkSpace(Integer.parseInt(w)));
        }
        return errorMessage();
    }

    private String processContinueController(String command) {
        Map<String, String> props = parseOptions(command);
        String e = props.get("-e");
        if (e != null) {
            prx.continueExecutionProcess(Integer.parseInt(e));
            return "Successfull continue execution\n";
        }
        return errorMessage();
    }

    private String processPauseController(String command) {
        Map<String, String> props = parseOptions(command);
        String e = props.get("-e");
        if (e != null) {
            prx.pauseExecutionProcess(Integer.parseInt(e));
            return "Successfull pause execution\n";
        }
        return errorMessage();
    }

    private String processStopController(String command) {
        Map<String, String> props = parseOptions(command);
        String e = props.get("-e");
        if (e != null) {
            prx.stopExecutionProcess(Integer.parseInt(e));
            return "Successfull stop execution\n";
        }
        return errorMessage();
    }

    private String processStartController(String command) {
        Map<String, String> props = parseOptions(command);
        String p = props.get("-p");
        String o = props.get("-o");
        if (!anyNull(p, o)) {
            int execId = prx.startProcess(Integer.parseInt(p), o);
            return "Seccessfull process init with execution's id: " + execId + "\n";
        }
        return errorMessage();
    }

    private String processAddController(String command) {
        Map<String, String> props = parseOptions(command);
        String n = props.get("-n");
        String d = props.get("-d");
        String w = props.get("-w");
        if (!anyNull(n, d, w)) {
            int id = prx.saveProcess(n, d, Integer.parseInt(w));
            return "Sucessfull process'id: " + id + "\n";
        }
        return errorMessage();
    }

}
