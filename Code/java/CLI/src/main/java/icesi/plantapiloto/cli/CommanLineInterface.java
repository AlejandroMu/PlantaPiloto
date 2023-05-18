package icesi.plantapiloto.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import icesi.plantapiloto.common.encoders.JsonEncoder;

public interface CommanLineInterface {
    public JsonEncoder encoder = new JsonEncoder();

    public String usage(String pading);

    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException;

    public default <T> String encoderList(T[] elemenTs) {
        return encoder.encodePretty(elemenTs) + "\n";
    }

    public default Map<String, String> parseOptions(String command) {
        Map<String, String> map = new HashMap<>();
        String split[] = command.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith("-")) {
                String key = split[i];
                String value = split[++i];
                if (value.startsWith("'")) {

                    String v = value.length() <= 1 ? "" : value.substring(1);
                    if (!v.endsWith("'")) {
                        for (int j = i + 1; j < split.length; j++) {
                            if (!split[j].endsWith("'")) {
                                v += " " + split[j];
                            } else {
                                v += " " + split[j].substring(0, split[j].length() - 1);
                                i = j;
                                break;
                            }
                        }
                    } else {
                        v = v.length() <= 1 ? "" : v.substring(0, v.length() - 1);
                    }
                    value = v.trim();
                }
                map.put(key, value);
            }
        }
        return map;
    }

    public default String errorMessage() {
        return "failed request, one or more fields are wrongs\n" + usage(" ");

    }

    public default boolean anyNull(Object... objects) {
        boolean ret = false;
        for (int i = 0; i < objects.length; i++) {
            ret |= objects[i] == null;
        }
        return ret;
    }

}
