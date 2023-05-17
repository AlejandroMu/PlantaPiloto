package icesi.plantapiloto.cli.actionCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.ActionManagerControllerPrx;
import icesi.plantapiloto.common.dtos.ActionDTO;

public class ActionCLI implements CommanLineInterface {
    private ActionManagerControllerPrx prx;

    public ActionCLI(Communicator communicator) {
        prx = ActionManagerControllerPrx.checkedCast(communicator.stringToProxy(System.getProperty("action")));
    }

    @Override
    public String usage(String pading) {
        StringBuilder builder = new StringBuilder(pading + "usage: action help || action command\n");
        builder.append(pading + " action add -n {name}* -d {displayName}* -e {expression}*: add action\n")
                .append(pading + " action list -n {name}: list actions\n");
        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {

        if (command.matches("action\\s+help.*")) {
            writer.write(usage(" "));
        } else if (command.matches("action\\s+add.*")) {
            writer.write(actionAddControl(command));
        } else if (command.matches("action\\s+list.*")) {
            writer.write(actionListControl(command));
        } else {
            return false;
        }
        return true;
    }

    private String actionListControl(String command) {
        Map<String, String> props = parseOptions(command);
        String n = props.get("-n");
        String d = props.get("-d");
        String e = props.get("-e");

        if (n != null && d != null && e != null) {
            int id = prx.saveAction(n, d, e);
            return "Succesfull, action's Id: " + id;
        }

        return errorMessage();
    }

    private String actionAddControl(String command) {
        Map<String, String> props = parseOptions(command);
        String n = props.get("-n");
        ActionDTO[] values = null;
        if (n != null) {
            values = prx.findActionByNameMatch(n);
        } else {
            values = prx.findActions();
        }
        return encoderList(values);
    }

}
