package icesi.plantapiloto.cli.workSpaceCLI;

import java.io.BufferedWriter;
import java.io.IOException;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.WorkSpaceManagerControllerPrx;
import icesi.plantapiloto.common.dtos.DriverDTO;

public class WorkSpapceCLI implements CommanLineInterface {
    private WorkSpaceManagerControllerPrx prx;

    public WorkSpapceCLI(Communicator communicator) {
        prx = WorkSpaceManagerControllerPrx.checkedCast(communicator.stringToProxy(System.getProperty("work")));
    }

    @Override
    public String usage(String pading) {
        StringBuilder builder = new StringBuilder();
        builder.append(pading + " workspace drivers (-w {workSpace}): list asset drivers\n");

        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {
        if (command.matches("workspace\\s+drivers.*")) {
            writer.write(assetDriversControl(command));

        }
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consoleIteractive'");
    }

    public String assetDriversControl(String command) {
        String split[] = command.split(" ");
        String work = null;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-w")) {
                work = split[++i];
            }
        }
        DriverDTO[] driverDTOs = null;
        if (work != null) {
            driverDTOs = prx.findDriversByWorkSpace(Integer.parseInt(work));
        } else {
            driverDTOs = prx.findAllDrivers();
        }

        return encoderList(driverDTOs);
    }
}
