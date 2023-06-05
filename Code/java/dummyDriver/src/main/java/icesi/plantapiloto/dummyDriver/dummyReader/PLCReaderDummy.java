package icesi.plantapiloto.dummyDriver.dummyReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import icesi.plantapiloto.common.consts.AssetState;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class PLCReaderDummy implements DriverAssetConcrete {

    public static final String[] TYPE_NAMES = { "PLC_TAG", "PLC" };
    public static final String PLC_IP_PROP = "plc.ip";
    public static final String PLC_SLOT_PROP = "plc.slot";

    public PLCReaderDummy() {

    }

    @Override
    public List<MeasurementDTO> readAsset(List<AssetDTO> asset, int execId) {
        try {
            List<MeasurementDTO> values = new ArrayList<>();
            long timeStamp = new Date().getTime();
            for (AssetDTO assetDTO : asset) {
                if (assetDTO.typeName.equals(TYPE_NAMES[0])) {
                    MeasurementDTO value = readTag(assetDTO);
                    value.execId = execId;
                    value.timeStamp = timeStamp;
                    values.add(value);
                } else if (assetDTO.typeName.equals(TYPE_NAMES[1])) {
                    List<MeasurementDTO> tags = readPLC(assetDTO, timeStamp, execId);
                    values.addAll(tags);
                }
            }
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void setPointAsset(AssetDTO asset, double value) {
        try {
            boolean inActive = asset.state.equals(AssetState.INACTIVE.getValue());
            boolean isTag = asset.typeName.equals(TYPE_NAMES[0]);
            if (inActive || !isTag) {
                return;
            }
            Map<String, String> config = asset.parent.props;
            System.out
                    .println("Set Point method: " + asset.name + "Value: " + value + " configs: " + config.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }

    }

    public MeasurementDTO readTag(AssetDTO tag) throws Exception {
        MeasurementDTO dto = new MeasurementDTO();
        dto.assetId = tag.assetId;
        dto.assetName = tag.name;
        Random r = new Random();
        dto.value = r.nextDouble();
        return dto;
    }

    public List<MeasurementDTO> readPLC(AssetDTO plc, long timestamp, int execId) {

        try {
            Map<String, String> config = plc.props;
            System.out.println("Read Plc: " + plc.name + " Config: " + config.toString());
            AssetDTO[] tags = plc.childrens;
            List<MeasurementDTO> values = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
                if (!tags[i].state.equals(AssetState.INACTIVE.getValue())) {
                    MeasurementDTO dto = readTag(tags[i]);
                    dto.execId = execId;
                    dto.timeStamp = timestamp;
                    values.add(dto);
                }
            }
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
