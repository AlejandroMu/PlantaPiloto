package icesi.plantapiloto.cli.instructionCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.InstructionManagerControllerPrx;
import icesi.plantapiloto.common.dtos.InstructionDTO;

public class InstructionCLI implements CommanLineInterface {
    private InstructionManagerControllerPrx prx;

    public InstructionCLI(Communicator communicator) {
        prx = InstructionManagerControllerPrx
                .checkedCast(communicator.stringToProxy(System.getProperty("instruction")));
    }

    @Override
    public String usage(String pading) {
        StringBuilder builder = new StringBuilder(pading + "usage: instruction help || instruction command\n");
        builder.append(pading + " instruction add -n {name}* -p {predicate}* -t {type}*\n")
                .append(pading + " instruction list -n {nameMatch}\n")
                .append(pading + " instruction action add -a {actionId}* -i {instrId}*\n");

        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {
        if (command.matches("instruction\\s+help.*")) {
            writer.write(usage(" "));
        } else if (command.matches("instruction\\s+add.*")) {
            writer.write(instructionAddControl(command));
        } else if (command.matches("instruction\\s+list.*")) {
            writer.write(instructionListControl(command));
        } else if (command.matches("instruction\\s+action\\s+add.*")) {
            writer.write(instructionActionAddControl(command));
        } else {
            return false;
        }
        return true;
    }

    private String instructionActionAddControl(String command) {
        Map<String, String> props = parseOptions(command);
        String a = props.get("-a");
        String i = props.get("-i");
        if (!anyNull(a, i)) {
            prx.addActionToInstruction(Integer.parseInt(a), Integer.parseInt(i));
            return "Succesfull, action addded to Instruction";
        }
        return errorMessage();
    }

    private String instructionListControl(String command) {
        Map<String, String> props = parseOptions(command);
        String n = props.get("-n");
        InstructionDTO[] values = null;
        if (n != null) {
            values = prx.findInstructionsByNameMatch(n);
        } else {
            values = prx.findInstructions();
        }
        return encoderList(values);
    }

    private String instructionAddControl(String command) {
        Map<String, String> props = parseOptions(command);
        String n = props.get("-n");
        String p = props.get("-p");
        String t = props.get("-t");
        if (!anyNull(n, p, t)) {
            int id = prx.saveInstruction(n, p, t);
            return "Successful intruction's id: " + id;
        }
        return errorMessage();
    }

}
