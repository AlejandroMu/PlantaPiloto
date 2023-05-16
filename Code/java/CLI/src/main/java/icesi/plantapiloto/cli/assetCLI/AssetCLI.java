package icesi.plantapiloto.cli.assetCLI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.cli.CommanLineInterface;
import icesi.plantapiloto.common.controllers.AssetManagerControllerPrx;
import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.common.model.WorkSpace;

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
                        + " -w {workSpaceId}"
                        + " -s {state}*"
                        + " -p {parenId}"
                        + " [-m {metadataProp} {metadataValue} {metadataDesc}]+\n")
                .append(pad + " asset setpoint {assetId}* {value}*: set asset actuator value\n")
                .append(pad + " asset list ( | -t {typeId} | -s {state} | -w {workSpace}): list assets \n")
                .append(pad + " asset types (-d {driverId}): list asset types\n")
                .append(pad + " asset remove {assetId}*: remove asset by id\n");

        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {
        if (command.matches("\\s+asset\\s+help.*")) {
            writer.write(usage("    "));
        } else if (command.matches("asset\\s+add.*")) {
            writer.write(assetAddControl(command));
        } else if (command.matches("asset\\s+list.*")) {
            writer.write(assetListControl(command));

        } else if (command.matches("asset\\s+types.*")) {
            writer.write(assetTypesControl(command));

        } else if (command.matches("asset\\s+remove.*")) {
            writer.write(assetRemoveControl(command));

        } else if (command.matches("asset\\s+setpoint.*")) {
            writer.write(assetSetPoint(command));

        } else {
            return false;
        }
        writer.flush();
        return true;

    }

    public String assetSetPoint(String command) {
        String split[] = command.split(" ");
        int assetId = Integer.parseInt(split[2]);
        double value = Double.parseDouble(split[3]);
        prx.changeAssetValue(assetId, value);
        return "success";
    }

    public String assetAddControl(String command) {
        String split[] = command.split(" ");
        String name = null;
        String desc = null;
        int typeId = -1;
        int workId = -1;
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
            } else if (split[i].equals("-w")) {
                workId = Integer.parseInt(split[++i]);
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
                && workId != -1
                && state != null;
        if (!isValid) {
            return "failed save, one or more fields are wrongs\n" + usage(" ");
        }
        Asset asset = new Asset();

        asset.setName(name);
        asset.setDescription(desc);
        Type t = new Type();
        t.setId(typeId);
        asset.setTypeBean(t);
        WorkSpace d = new WorkSpace();
        d.setId(workId);
        asset.setWorkSpace(d);
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
            String work = null;
            for (int i = 2; i < parse.length; i++) {
                if (parse[i].equals("-t")) {
                    type = parse[++i];
                } else if (parse[i].equals("-s")) {
                    state = parse[++i];
                } else if (parse[i].equals("-w")) {
                    work = parse[++i];
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
