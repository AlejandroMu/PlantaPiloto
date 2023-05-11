package icesi.plantapiloto.cli.assetCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.AssetManagerControllerPrx;
import icesi.plantapiloto.common.dtos.DriverDTO;
import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;

public class AssetCLI implements CommanLineInterface {

    private AssetManagerControllerPrx prx;

    public AssetCLI(Communicator communicator) {
        prx = AssetManagerControllerPrx.checkedCast(communicator.stringToProxy(System.getProperty("asset")));
    }

    @Override
    public String usage() {
        StringBuilder builder = new StringBuilder();
        builder.append(" usage: asset help || asset command\n")
                .append("  commands: * required\n")
                .append("   asset add -n {name}*"
                        + " -d {desc}*"
                        + " -t {typeId}*"
                        + " -dr {drivenID}"
                        + " -s {state}*"
                        + " -p {parenId}"
                        + " [-m {metadataProp} {metadataValue} {metadataDesc}]+\n")
                .append("   asset list ( | -t {typeId} | -s {state}): list assets \n")
                .append("   asset types: list asset types\n")
                .append("   asset drivers: list asset drivers\n")
                .append("   asset remove {assetId}*: remove asset by id\n")
                .append("   asset run {execId}*: run capture asset by process exetion id\n")
                .append("   asset stop {execId}*: stop capture asset by process exetion id\n");

        return builder.toString();
    }

    @Override
    public void consoleIteractive(String command, BufferedWriter writer) throws IOException {
        if (command.matches("asset\\s+help.*")) {
            writer.write(usage());
        } else if (command.matches("asset\\s+add.*")) {
            writer.write(assetAddControl(command));
        } else if (command.matches("asset\\s+list.*")) {
            writer.write(assetListControl(command));

        } else if (command.matches("asset\\s+types.*")) {
            writer.write(assetTypesControl(command));

        } else if (command.matches("asset\\s+drivers.*")) {
            writer.write(assetDriversControl(command));

        } else if (command.matches("asset\\s+remove.*")) {
            writer.write(assetRemoveControl(command));

        } else if (command.matches("asset\\s+run.*")) {
            writer.write(runCapture(command));

        } else if (command.matches("asset\\s+stop.*")) {
            writer.write(stopCapture(command));

        } else {
            writer.write(usage());
        }
        writer.flush();

    }

    private String stopCapture(String command) {
        int execId = Integer.parseInt(command.split(" ")[2]);

        prx.stopCapture(execId);

        return "Process stoped\n";
    }

    private String runCapture(String command) {
        int execId = Integer.parseInt(command.split(" ")[2]);

        prx.captureAssets(execId);

        return "Process runing\n";
    }

    public String assetAddControl(String command) {
        String split[] = command.split(" ");
        String name = null;
        String desc = null;
        int typeId = -1;
        int drId = -1;
        String state = null;
        int parentId = -1;
        List<MetaData> metaDatas = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-n")) {
                name = split[++i];
            } else if (split[i].equals("-d")) {
                desc = split[++i];
            } else if (split[i].equals("-t")) {
                typeId = Integer.parseInt(split[++i]);
            } else if (split[i].equals("-dr")) {
                drId = Integer.parseInt(split[++i]);
            } else if (split[i].equals("-s")) {
                state = split[++i];
            } else if (split[i].equals("-p")) {
                parentId = Integer.parseInt(split[++i]);
            } else if (split[i].equals("-m")) {
                MetaData metaData = new MetaData();
                metaData.setName(split[++i]);
                metaData.setValue(split[++i]);
                metaData.setDescription(split[++i]);
                metaDatas.add(metaData);
            }
        }
        boolean isValid = name != null
                && desc != null
                && typeId != -1
                && drId != -1
                && state != null;
        if (!isValid) {
            return "failed save, one or more fields are wrongs\n" + usage();
        }
        Asset asset = new Asset();

        asset.setName(name);
        asset.setDescription(desc);
        Type t = new Type();
        t.setId(typeId);
        asset.setTypeBean(t);
        Driver d = new Driver();
        d.setId(drId);
        asset.setDriverBean(d);
        asset.setAssetState(state);
        if (parentId != -1) {
            Asset paren = new Asset();
            paren.setId(parentId);
            asset.setAsset(paren);

        }
        if (metaDatas.size() > 0) {
            System.out.println("Metadatas: " + metaDatas.size());

            asset.setMetaData(metaDatas);
        }

        if (isValid) {
            prx.saveAsset(asset);
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
            String type = null;
            String state = null;
            for (int i = 2; i < parse.length; i++) {
                if (parse[i].equals("-t")) {
                    type = parse[++i];
                } else if (parse[i].equals("-s")) {
                    state = parse[++i];
                }
            }
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
        }
        return encoderList(values);
    }

    public String assetTypesControl(String command) {
        TypeDTO[] types = prx.findAllTypes();
        return encoderList(types);
    }

    public String assetDriversControl(String command) {
        DriverDTO[] driverDTOs = prx.findAllDrivers();
        return encoderList(driverDTOs);
    }

}
