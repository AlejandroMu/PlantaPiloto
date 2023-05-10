package icesi.plantapiloto.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Reference;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.cli.assetCLI.AssetCLI;

public class Main implements CommanLineInterface {
    public static void main(String[] args) throws Exception {

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(new DefaultParser())
                .build();

        lineReader.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION);

        lineReader.getKeyMaps().get(LineReader.MAIN).bind(new Reference(LineReader.CLEAR_SCREEN), KeyMap.ctrl('L'));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        Communicator com = Util.initialize(args);
        System.out.println("args: " + Arrays.toString(args));
        String host = "localhost";
        String port = "12345";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h")) {
                host = args[++i];
            } else if (args[i].equals("-p")) {
                port = args[++i];
            }

        }
        String proxy = "tcp -h " + host + " -p " + port;
        System.setProperty("asset", "AssetManager:" + proxy);
        CommanLineInterface asset = new AssetCLI(com);

        Main main = new Main(asset);
        String line = "help";
        while (!line.equals("exit")) {
            main.consoleIteractive(line, writer);
            line = lineReader.readLine(">> ");
        }
        writer.close();
        com.close();
    }

    private CommanLineInterface asset;

    public Main(CommanLineInterface asset) {
        this.asset = asset;
    }

    @Override
    public String usage() {
        StringBuilder builder = new StringBuilder();
        builder.append(asset.usage());
        return builder.toString();
    }

    @Override
    public void consoleIteractive(String command, BufferedWriter writer) throws IOException {
        try {

            if (command.equals("help")) {
                writer.write(usage());
            } else if (command.matches("asset.*")) {
                asset.consoleIteractive(command, writer);
            } else {
                writer.write("command not found: " + command);
                writer.write(usage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            writer.append("Error : \n");
        }
        writer.flush();

    }
}
