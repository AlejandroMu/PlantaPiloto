package icesi.plantapiloto.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import icesi.plantapiloto.common.encoders.JsonEncoder;

public interface CommanLineInterface {
    public JsonEncoder encoder = new JsonEncoder();

    public String usage(String pading);

    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException;

    public default <T> String encoderList(T[] elemenTs) {
        return Arrays.asList(elemenTs).stream().map(v -> ">> " + encoder.encode(v) + "\n")
                .reduce("", (a, c) -> a + c).toString();
    }

}
