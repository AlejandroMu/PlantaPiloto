package icesi.plantapiloto.cli.workSpaceCLI;

import java.io.BufferedWriter;
import java.io.IOException;

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
        String name = null;
        String desc = null;
        Integer driId = null;

        String split[] = command.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-n")) {
                name = split[++i];
            } else if (split[i].equals("-d")) {
                desc = split[++i];
            } else if (split[i].equals("-dr")) {
                driId = Integer.parseInt(split[++i]);
            }
        }

        if (name != null && desc != null && driId != null) {
            int di = prx.saveAssetType(name, desc, driId);
            return "Seccesfull type's id: " + di + "\n";
        }

        return "Failed data incomplete or is wrong: " + usage(" ");
    }

    private String workSpaceDriversAddControl(String command) {
        String name = null;
        String service = null;
        Integer workId = null;

        String split[] = command.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-n")) {
                name = split[++i];
            } else if (split[i].equals("-s")) {
                service = split[++i];
            } else if (split[i].equals("-w")) {
                workId = Integer.parseInt(split[++i]);
            }
        }

        if (name != null && service != null && workId != null) {
            int di = prx.saveDriver(name, service, workId);
            return "Seccesfull driver's id: " + di + "\n";
        }

        return "Failed data incomplete or is wrong: " + usage(" ");
    }

    public String workSpaceListControl(String command) {
        String split[] = command.split(" ");
        Integer idDep = null;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-w")) {
                idDep = Integer.parseInt(split[++i]);
            }
        }
        WorkSpaceDTO[] values = null;
        if (idDep != null) {
            values = prx.findWorkSpacesByDepartment(idDep);
        } else {
            values = prx.findWorkSpaces();
        }

        return encoderList(values);
    }

    public String workSpaceAddControl(String command) {
        String split[] = command.split(" ");
        String name = null;
        String desc = null;
        Integer depart = -1;

        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-n")) {
                name = split[++i];
            } else if (split[i].equals("-d")) {
                desc = split[++i];
            } else if (split[i].equals("-dep")) {
                depart = Integer.parseInt(split[++i]);
            }
        }
        if (name != null && desc != null) {
            int id = prx.saveWorkSpace(name, desc, depart);
            return "Add succesfull Id: " + id + "\n";
        } else {
            return "Failed data incomplete or is wrong: " + usage(" ");
        }
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

    public String assetTypesControl(String command) {
        String split[] = command.split(" ");
        String driver = null;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-d")) {
                driver = split[++i];
            }
        }
        TypeDTO[] types = null;
        if (driver != null) {
            types = prx.findTypesByDriver(Integer.parseInt(driver));
        } else {
            types = prx.findAllTypes();
        }
        return encoderList(types);
    }
}
