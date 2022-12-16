package icesi.plantapiloto.controlLayer.plc;

import icesi.plantapiloto.controlLayer.common.PluginI;
import icesi.plantapiloto.controlLayer.common.entities.Measure;
import icesi.plantapiloto.controlLayer.common.entities.Message;

import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import etherip.EtherNetIP;
import etherip.types.CIPData;

public class PlcPlugin implements PluginI {
    private EtherNetIP plc;
    private String ip;
    private String name;
    private List<String> tags;
    private HashMap<String, String> props;

    public PlcPlugin() {
        try {
            tags = new ArrayList<>();
            props = new HashMap<>();
            loadTags();
            // plc = new EtherNetIP(ip, 0);
            // plc.connectTcp();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTags() {
        try {
            File propFile = new File("plugin.conf");

            InputStream stream = new FileInputStream(propFile);
            BufferedReader red = new BufferedReader(new InputStreamReader(stream));
            String line = red.readLine();
            while (line != null && !line.equals("")) {
                String prop[] = line.trim().split("=");
                if (prop[0].contains("tagName")) {
                    tags.add(prop[1].trim());

                } else if (prop[0].contains("PLC_IP")) {
                    ip = prop[1].trim();
                } else if (prop[0].contains("PLC_NAME")) {
                    name = prop[1].trim();
                    props.put("topic", name);
                } else {
                    props.put(prop[0].trim(), prop[1].trim());
                }
                line = red.readLine();
            }
            red.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message getMessage() {
        Message msg = new Message();
        try {
            msg.setSourceData(name + " " + ip)
                    .setTime(Calendar.getInstance().getTime())
                    .setTopic(props.get("topic"));
            for (String tag : tags) {

                // CIPData data = plc.readTag(tag, (short) 1);
                // String value = data.toString().split("\\[")[1].split("\\]")[0];
                String value = "" + Math.random();
                Measure measure = new Measure();
                measure.setName(tag);
                measure.setValue(value);
                msg.addMeasure(measure);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }

    public HashMap<String, String> getSettings() {
        return props;
    }
}
