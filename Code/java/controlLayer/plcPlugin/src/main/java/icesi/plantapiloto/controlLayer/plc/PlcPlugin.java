package icesi.plantapiloto.controlLayer.plc;

import icesi.plantapiloto.controlLayer.common.PluginI;
import icesi.plantapiloto.controlLayer.common.Message;

import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
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

    public PlcPlugin() {
        try {
            tags = new ArrayList<>();
            loadTags();
            plc = new EtherNetIP(ip, 0);
            plc.connectTcp();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTags() {
        try {

            InputStream stream = this.getClass().getResourceAsStream("./plugin.conf");
            BufferedReader red = new BufferedReader(new InputStreamReader(stream));
            String line = red.readLine().trim();
            while (line != null && !line.equals("")) {
                String prop[] = line.split("=");
                if (prop[0].equals("tagName")) {
                    tags.add(prop[1]);

                } else if (prop[0].equals("PLC_IP")) {
                    ip = prop[1];
                } else if (prop[0].equals("PLC_NAME")) {
                    name = prop[1];
                }
                line = red.readLine().trim();
            }
            red.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getValues() {
        List<Message> messages = new ArrayList<>();
        try {
            for (String tag : tags) {
                CIPData data = plc.readTag(tag, (short) 1);
                String value = data.toString().split("\\[")[1].split("\\]")[0];
                Message msg = new Message();
                msg.setSourceData(name + " " + ip)
                        .setType(data.getType().toString())
                        .setTime(Calendar.getInstance().getTime())
                        .setValue(value)
                        .setName(tag);

                System.out.println(data);
                messages.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }

    public HashMap<String, String> getSettings() {
        HashMap<String, String> map = new HashMap<>();
        map.put("startHour", "21:30");
        map.put("lapse", "30000");
        map.put("topic", name);

        return map;
    }
}
