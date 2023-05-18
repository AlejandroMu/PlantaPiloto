package icesi.plantapiloto.cli.workSpaceCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.WorkSpaceManagerControllerPrx;
import icesi.plantapiloto.common.dtos.DriverDTO;
import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.dtos.WorkSpaceDTO;

public class WorkSpapceCLI implements CommanLineInterface {
    private WorkSpaceManagerControllerPrx prx;

    public WorkSpapceCLI(Communicator communicator) {
        prx = WorkSpaceManagerControllerPrx.checkedCast(communicator.stringToProxy(System.getProperty("work")));
    }

    @Override
    public String usage(String pading) {
        StringBuilder builder = new StringBuilder(pading + "usage: workspace help || workspace command\n");
        builder.append(pading + " workspace add -n {name}* -d {desc}* -dep {workId}\n")
                .append(pading + " workspace list -w {parent}: list workspaces\n")
                .append(pading + " workspace drivers ls -w {workSpace}: list drivers\n")
                .append(pading
                        + " workspace drivers add -n {name}* -s {serviceProxy}* -w {workSpace}*: create drivers\n")
                .append(pading + " workspace types add -n {name}* -d {desc}* -dr {driverId}*: create types\n")
                .append(pading + " workspace types ls -d {driverId}: list types\n");

        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {
        if (command.matches("workspace\\s+help.*")) {
            writer.write(usage(" "));
        } else if (command.matches("workspace\\s+add.*")) {
            writer.write(workSpaceAddControl(command));
        } else if (command.matches("workspace\\s+list.*")) {
            writer.write(workSpaceListControl(command));
        } else if (command.matches("workspace\\s+drivers\\s+ls.*")) {
            writer.write(assetDriversControl(command));
        } else if (command.matches("workspace\\s+drivers\\s+add.*")) {
            writer.write(workSpaceDriversAddControl(command));
        } else if (command.matches("workspace\\s+types\\s+ls.*")) {
            writer.write(assetTypesControl(command));
        } else if (command.matches("workspace\\s+types\\s+add.*")) {
            writer.write(workSpaceTypesAddControl(command));
        } else {
            return false;
        }
        return true;
    }

    private String workSpaceTypesAddControl(String command) {
        Map<String, String> props = parseOptions(command);

        String name = props.get("-n");
        String desc = props.get("-d");
        String driId = props.get("-dr");

        if (!anyNull(name, desc, driId)) {
            int di = prx.saveAssetType(name, desc, Integer.parseInt(driId));
            return "Seccesfull type's id: " + di + "\n";
        }

        return errorMessage();
    }

    private String workSpaceDriversAddControl(String command) {
        Map<String, String> props = parseOptions(command);

        String name = props.get("-n");
        String service = props.get("-s");
        String workId = props.get("-w");

        if (!anyNull(name, service, workId)) {
            int di = prx.saveDriver(name, service, Integer.parseInt(workId));
            return "Seccesfull driver's id: " + di + "\n";
        }

        return errorMessage();
    }

    public String workSpaceListControl(String command) {
        Map<String, String> props = parseOptions(command);
        String dep = props.get("-w");
        WorkSpaceDTO[] values = null;
        if (dep != null) {
            Integer idDep = Integer.parseInt(dep);
            values = prx.findWorkSpacesByDepartment(idDep);
        } else {
            values = prx.findWorkSpaces();
        }

        return encoderList(values);
    }

    public String workSpaceAddControl(String command) {
        final Map<String, String> props = parseOptions(command);

        String name = props.get("-n");
        String desc = props.get("-d");
        String depart = props.get("-dep");
        if (!anyNull(name, desc)) {
            int depId = depart == null ? -1 : Integer.parseInt(depart);
            int id = prx.saveWorkSpace(name, desc, depId);
            return "Add succesfull Id: " + id + "\n";
        } else {
            return errorMessage() + " props: \n "
                    + props.keySet().stream().reduce("", (a, k) -> a + "k: " + k + " Value:" + props.get(k) + "\n");
        }
    }

    public String assetDriversControl(String command) {
        Map<String, String> props = parseOptions(command);
        String work = props.get("-w");

        DriverDTO[] driverDTOs = null;
        if (work != null) {
            driverDTOs = prx.findDriversByWorkSpace(Integer.parseInt(work));
        } else {
            driverDTOs = prx.findAllDrivers();
        }

        return encoderList(driverDTOs);
    }

    public String assetTypesControl(String command) {
        Map<String, String> props = parseOptions(command);
        String driver = props.get("-d");
        TypeDTO[] types = null;
        if (driver != null) {
            types = prx.findTypesByDriver(Integer.parseInt(driver));
        } else {
            types = prx.findAllTypes();
        }
        return encoderList(types);
    }
}
