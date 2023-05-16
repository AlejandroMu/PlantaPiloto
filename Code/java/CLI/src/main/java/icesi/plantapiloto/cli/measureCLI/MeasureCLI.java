package icesi.plantapiloto.cli.measureCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;

public class MeasureCLI implements CommanLineInterface {

    private MeasurementManagerControllerPrx prx;

    public MeasureCLI(Communicator com) {
        prx = MeasurementManagerControllerPrx.checkedCast(com.stringToProxy(System.getProperty("measure")));
    }

    @Override
    public String usage(String pad) {
        StringBuilder builder = new StringBuilder();
        builder.append(pad + "usage: measure help || measure command\n")
                .append(pad + " measure add "
                        + "-a {assetId}* "
                        + "-v {value}* "
                        + "-e {exeId}* "
                        + "-t {time}: long representation of time millis\n")
                .append(pad + " measure list "
                        + "[-e {execId}*] |"
                        + "[-a {assetId}* "
                        + "-e {execId} "
                        + "-i {initTime}* "
                        + "-f {endTime}: long representation of time millis]\n");

        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {

        if (command.matches("\\s+measure\\s+help.*")) {
            writer.append(usage("   "));
        } else if (command.matches("\\s+measure\\s+add.*")) {
            writer.append(addMeasureControl(command));
        } else if (command.matches("\\s+measure\\s+list.*")) {
            writer.append(listMeasureControl(command));
        } else {
            return false;
        }

        writer.flush();
        return true;
    }

    private String listMeasureControl(String command) {
        String split[] = command.split(" ");
        Integer e = null;
        Integer a = null;
        Long i = null;
        Long f = System.currentTimeMillis();
        for (int j = 0; j < split.length; j++) {
            if (split[j].equals("-e")) {
                e = Integer.parseInt(split[++j]);
            } else if (split[j].equals("-a")) {
                a = Integer.parseInt(split[++j]);
            } else if (split[j].equals("-i")) {
                i = Long.parseLong(split[++j]);
            } else if (split[j].equals("-f")) {
                f = Long.parseLong(split[++j]);
            }
        }
        MeasurementDTO[] values = null;
        if (e != null) {
            values = prx.getMeasurmentsByExecution(e);
        }
        if (a != null && i != null) {
            if (values == null) {
                values = prx.getMeasurments(a, i, f);
            } else {
                final Integer asset = a;
                final Long init = i;
                final Long fin = f;
                values = Arrays.asList(values).stream()
                        .filter(m -> m.assetId == asset && m.timeStamp >= init && m.timeStamp <= fin)
                        .toArray(MeasurementDTO[]::new);
            }
        }
        if (values == null) {
            return "Failed: verify data required:\n" + usage("   ");
        }

        return encoderList(values);
    }

    private String addMeasureControl(String command) {
        MeasurementDTO dto = new MeasurementDTO();
        dto.timeStamp = System.currentTimeMillis();
        int count = 0;
        String split[] = command.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-a")) {
                count++;
                dto.assetId = Integer.parseInt(split[++i]);
            } else if (split[i].equals("-v")) {
                count++;
                dto.value = Double.parseDouble(split[++i]);
            } else if (split[i].equals("-e")) {
                count++;
                dto.exeId = Integer.parseInt(split[++i]);
            } else if (split[i].equals("-t")) {
                dto.timeStamp = Long.parseLong(split[++i]);
            }
        }
        if (count != 3) {
            return "Failed: verify data required:\n" + usage("   ");
        }
        prx.saveAssetValue(new MeasurementDTO[] { dto });
        return "Succed!!";
    }

}
