package icesi.plantapiloto.plcDriver.plcReader;

import java.util.Date;
import java.util.Map;

import etherip.EtherNetIP;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;
import icesi.plantapiloto.modelManager.controllers.AssetDTO;
import icesi.plantapiloto.modelManager.controllers.MeasurementDTO;

public class PLCReader implements DriverAssetConcrete {

    public static final String TYPE_NAME = "PLC_TAG";
    public static final String PLC_IP_PROP = "plc.ip";
    public static final String PLC_SLOT_PROP = "plc.slot";
    public static final String STATE_ENABLE_ID = "A";

    public PLCReader() {

    }

    @Override
    public MeasurementDTO[] readAsset(AssetDTO[] asset, Map<String, String> config) {
        try {
            EtherNetIP plc = connectPlc(config);
            MeasurementDTO[] ret = new MeasurementDTO[asset.length];
            long time = new Date().getTime();
            for (int i = 0; i < ret.length; i++) {
                if (asset[i].typeName.equals(TYPE_NAME) && asset[i].state.equals(STATE_ENABLE_ID)) {

                    CIPData data = plc.readTag(asset[i].name, (short) 1);
                    String value = data.toString().split("\\[")[1].split("\\]")[0];
                    MeasurementDTO dto = new MeasurementDTO(asset[i].assetId, Double.parseDouble(value),
                            time);
                    ret[i] = dto;
                }

            }
            plc.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void setPointAsset(AssetDTO asset, double value, Map<String, String> config) {
        try {
            boolean active = asset.state.equals(STATE_ENABLE_ID);
            if (!active) {
                return;
            }
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

}
