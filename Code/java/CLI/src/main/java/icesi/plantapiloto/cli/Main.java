package icesi.plantapiloto.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import icesi.plantapiloto.cli.measureCLI.MeasureCLI;
import icesi.plantapiloto.cli.workSpaceCLI.WorkSpapceCLI;

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
        System.setProperty("measure", "MeasureManager:" + proxy);
        System.setProperty("work", "WorkSpaceManager:" + proxy);

        CommanLineInterface asset = new AssetCLI(com);
        CommanLineInterface measur = new MeasureCLI(com);
        CommanLineInterface work = new WorkSpapceCLI(com);

        Main main = new Main();
        main.addCommandLine(asset);
        main.addCommandLine(measur);
        main.addCommandLine(work);

        String line = "help";
        while (!line.equals("exit")) {
            main.consoleIteractive(line, writer);
            line = lineReader.readLine(">> ");
        }
        writer.close();
        com.close();
    }

    private List<CommanLineInterface> commands;

    public Main() {
        this.commands = new ArrayList<>();
    }

    public void addCommandLine(CommanLineInterface commanLineInterface) {
        commands.add(commanLineInterface);
    }

    @Override
    public String usage(String pad) {
        StringBuilder builder = new StringBuilder();
        for (CommanLineInterface commanLineInterface : commands) {
            builder.append(commanLineInterface.usage(pad + "    "));
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public boolean consoleIteractive(String command, BufferedWriter writer) throws IOException {
        try {

            if (command.equals("help")) {
                writer.write(usage(""));
            } else {
                boolean valid = false;
                for (CommanLineInterface commanLineInterface : commands) {
                    valid = commanLineInterface.consoleIteractive(command, writer);
                    if (valid) {
                        break;
                    }
                }
                if (!valid) {
                    writer.append("Command not found: try again with \n");
                    writer.append(usage(""));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            writer.append("Error : \n");
            return false;
        }
        writer.flush();
        return true;

    }
}
