package icesi.plantapiloto.cli.measureCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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

        if (command.matches("measure\\s+help.*")) {
            writer.append(usage("   "));
        } else if (command.matches("measure\\s+add.*")) {
            writer.append(addMeasureControl(command));
        } else if (command.matches("measure\\s+list.*")) {
            writer.append(listMeasureControl(command));
        } else {
            return false;
        }

        return true;
    }

    private String listMeasureControl(String command) {
        Map<String, String> props = parseOptions(command);
        Integer e = Integer.parseInt(props.get("-e"));
        Integer a = Integer.parseInt(props.get("-a"));
        Long i = Long.parseLong(props.get("-i"));
        Long f = props.get("-f") == null ? System.currentTimeMillis() : Long.parseLong(props.get("-f"));
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
            return errorMessage();
        }

        return encoderList(values);
    }

    private String addMeasureControl(String command) {
        try {
            Map<String, String> props = parseOptions(command);
            MeasurementDTO dto = new MeasurementDTO();
            dto.assetId = Integer.parseInt(props.get("-a"));
            dto.value = Double.parseDouble(props.get("-v"));
            dto.exeId = Integer.parseInt(props.get("-e"));

            dto.timeStamp = props.get("-t") == null ? System.currentTimeMillis() : Long.parseLong(props.get("-e"));

            prx.saveAssetValue(new MeasurementDTO[] { dto });
            return "Succed!!\n";
        } catch (NumberFormatException e) {
            return errorMessage();
        }
    }

}
