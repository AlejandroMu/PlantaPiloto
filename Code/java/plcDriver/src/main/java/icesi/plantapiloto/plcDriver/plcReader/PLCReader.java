package icesi.plantapiloto.plcDriver.plcReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import etherip.EtherNetIP;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class PLCReader implements DriverAssetConcrete {

    public static final String[] TYPE_NAMES = { "PLC_TAG", "PLC" };
    public static final String PLC_IP_PROP = "plc.ip";
    public static final String PLC_SLOT_PROP = "plc.slot";
    public static final String STATE_ENABLE_ID = "A";

    public PLCReader() {

    }

    @Override
    public MeasurementDTO[] readAsset(AssetDTO[] asset, int execId) {
        try {
            List<MeasurementDTO> values = new ArrayList<>();
            long timeStamp = new Date().getTime();
            for (AssetDTO assetDTO : asset) {
                if (assetDTO.typeName.equals(TYPE_NAMES[0])) {
                    EtherNetIP connection = connectPlc(assetDTO.parent.props);
                    MeasurementDTO value = readTag(assetDTO, connection);
                    value.exeId = execId;
                    value.timeStamp = timeStamp;
                    connection.close();
                    values.add(value);
                } else if (assetDTO.typeName.equals(TYPE_NAMES[1])) {
                    List<MeasurementDTO> tags = readPLC(assetDTO, timeStamp, execId);
                    values.addAll(tags);
                }
            }
            MeasurementDTO[] ret = new MeasurementDTO[values.size()];
            return values.toArray(ret);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void setPointAsset(AssetDTO asset, double value) {
        try {
            boolean active = asset.state.equals(STATE_ENABLE_ID);
            boolean isTag = asset.typeName.equals(TYPE_NAMES[0]);
            if (!active || !isTag) {
                return;
            }
            Map<String, String> config = asset.parent.props;
            EtherNetIP plc = connectPlc(config);
            CIPData data = new CIPData(Type.REAL, 1);
            data.set(0, value);
            plc.writeTag(asset.name, data);
            plc.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();

        }

    }

    private EtherNetIP connectPlc(Map<String, String> config) throws Exception {
        String plcIp = config.get(PLC_IP_PROP);
        String plcSlot = config.get(PLC_SLOT_PROP);
        EtherNetIP plc = new EtherNetIP(plcIp, Integer.parseInt(plcSlot));
        plc.connectTcp();
        return plc;
    }

    public MeasurementDTO readTag(AssetDTO tag, EtherNetIP plc) throws Exception {
        CIPData data = plc.readTag(tag.name, (short) 1);
        String value = data.toString().split("\\[")[1].split("\\]")[0];
        MeasurementDTO dto = new MeasurementDTO();
        dto.assetId = tag.assetId;
        dto.assetName = tag.name;
        dto.value = Double.parseDouble(value);
        return dto;
    }

    public List<MeasurementDTO> readPLC(AssetDTO plc, long timestamp, int execId) {

        try {
            Map<String, String> config = plc.props;
            EtherNetIP connection = connectPlc(config);
            AssetDTO[] tags = plc.childrens;
            List<MeasurementDTO> values = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
                if (tags[i].state.equals(STATE_ENABLE_ID)) {
                    MeasurementDTO dto = readTag(tags[i], connection);
                    dto.exeId = execId;
                    dto.timeStamp = timestamp;
                    values.add(dto);
                }
            }
            connection.close();
            return values;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
