package icesi.plantapiloto.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import icesi.plantapiloto.common.encoders.JsonEncoder;

public interface CommanLineInterface {
    public JsonEncoder encoder = new JsonEncoder();

    public String usage(String pading);

    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException;

    public default <T> String encoderList(T[] elemenTs) {
        return Arrays.asList(elemenTs).stream().map(v -> ">> " + encoder.encode(v) + "\n")
                .reduce("", (a, c) -> a + c).toString();
    }

    public default Map<String, String> parseOptions(String command) {
        Map<String, String> map = new HashMap<>();
        String split[] = command.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith("-")) {
                map.put(split[i], split[++i]);
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
