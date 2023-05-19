package icesi.plantapiloto.cli.assetCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.AssetManagerControllerPrx;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.model.MetaData;

public class AssetCLI implements CommanLineInterface {

    private AssetManagerControllerPrx prx;

    public AssetCLI(Communicator communicator) {
        prx = AssetManagerControllerPrx.checkedCast(communicator.stringToProxy(System.getProperty("asset")));
    }

    @Override
    public String usage(String pad) {
        StringBuilder builder = new StringBuilder();
        builder.append(pad + "usage: asset help || asset command\n")
                .append(pad + "commands: * required\n")
                .append(pad + " asset add -n {name}*"
                        + " -d {desc}*"
                        + " -t {typeId}*"
                        + " -w {workSpaceId}*"
                        + " -s {state}*"
                        + " -p {parenId}"
                        + " [-m {metadataProp} {metadataValue} {metadataDesc}]+\n")
                .append(pad + " asset setpoint {assetId}* {value}*: set asset actuator value\n")
                .append(pad + " asset list ( | -t {typeId} | -s {state} | -w {workSpace}): list assets \n")
                .append(pad + " asset remove {assetId}*: remove asset by id\n");

        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {
        if (command.matches("asset\\s+help.*")) {
            writer.write(usage("    "));
        } else if (command.matches("asset\\s+add.*")) {
            writer.write(assetAddControl(command));
        } else if (command.matches("asset\\s+list.*")) {
            writer.write(assetListControl(command));

        } else if (command.matches("asset\\s+remove.*")) {
            writer.write(assetRemoveControl(command));

        } else if (command.matches("asset\\s+setpoint.*")) {
            writer.write(assetSetPoint(command));

        } else {
            return false;
        }
        return true;

    }

    public String assetSetPoint(String command) {
        String split[] = command.split(" ");
        int assetId = Integer.parseInt(split[2]);
        double value = Double.parseDouble(split[3]);
        prx.changeAssetValue(assetId, value);
        return "success\n";
    }

    public String assetAddControl(String command) {
        String split[] = command.split(" ");
        Map<String, String> props = parseOptions(command);
        String name = props.get("-n");
        String desc = props.get("-d");
        Integer typeId = Integer.parseInt(props.get("-t"));
        Integer workId = Integer.parseInt(props.get("-w"));
        String state = props.get("-s");
        Integer parentId = props.get("-p") == null ? -1 : Integer.parseInt(props.get("-p"));
        List<MetaData> metaDatas = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-m")) {
                MetaData metaData = new MetaData();
                metaData.setName(split[++i]);
                metaData.setValue(split[++i]);
                metaData.setDescription(split[++i]);
                metaDatas.add(metaData);
            }
        }
        boolean isValid = !anyNull(name, desc, typeId, workId, state);
        if (!isValid) {
            return errorMessage();
        }

        if (isValid) {
            prx.saveAsset(name, desc, typeId, workId, parentId, state, metaDatas.toArray(MetaData[]::new));
        }

        return "Saved\n";
    }

    public String assetRemoveControl(String command) {
        try {
            String id = command.split(" ")[2];
            prx.deletById(Integer.parseInt(id));
            return "deleted success\n";

        } catch (Exception e) {
            e.printStackTrace();
            return "deleted failed\n";
        }
    }

    public String assetListControl(String command) {
        String parse[] = command.split(" ");
        AssetDTO[] values = null;
        if (parse.length == 2) {
            values = prx.findAll();
        } else {
            Map<String, String> props = parseOptions(command);
            String type = props.get("-t");
            String state = props.get("-s");
            String work = props.get("-w");
            if (type != null) {
                values = prx.findByType(Integer.parseInt(type));
            }
            if (state != null) {
                if (values != null) {
                    final String s = state;
                    values = Arrays.asList(values).stream().filter(v -> v.state.equals(s)).toArray(AssetDTO[]::new);
                } else {
                    values = prx.findByState(state);
                }
            }
            if (work != null) {
                final int s = Integer.parseInt(work);
                if (values != null) {
                    values = Arrays.asList(values).stream().filter(v -> v.workId == s).toArray(AssetDTO[]::new);
                } else {
                    values = prx.findByWorkSpace(s);
                }
            }
        }
        return encoderList(values);
    }

}
