package icesi.plantapiloto.scheduleManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import icesi.plantapiloto.controlLayer.common.PluginI;
import icesi.plantapiloto.controlLayer.common.encoders.ObjectEncoder;
import icesi.plantapiloto.controlLayer.common.envents.PublisherI;

public class ScheduleManager {
    private PublisherI publisherI;
    private Properties properties;
    private Scheduler scheduler;
    private File propFile;

    public ScheduleManager() throws Exception {
        properties = new Properties();
        propFile = new File("schedule.properties");
        if (!propFile.exists()) {
            propFile.createNewFile();
        }
        InputStream stream = new FileInputStream(propFile);
        properties.load(stream);

        String pubClass = properties.getProperty("publisher.class").trim();
        String pubIp = properties.getProperty("publisher.ip").trim();
        String pubEncoder = properties.getProperty("publisher.encoder").trim();
        String pubName = properties.getProperty("publisher.name").trim();

        if (pubClass == null || pubIp == null || pubEncoder == null || pubName == null) {
            System.out.println("No publisher config");
            return;
        }
        ObjectEncoder encoder = (ObjectEncoder) Class.forName(pubEncoder).getDeclaredConstructor()
                .newInstance();
        publisherI = (PublisherI) Class.forName(pubClass).getDeclaredConstructor().newInstance();

        publisherI.setEncoder(encoder);
        publisherI.setHost(pubIp);
        publisherI.setName(pubName);

        scheduler = new Scheduler(publisherI);

        Iterator<?> keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.endsWith(".plugin")) {
                loadPlugin(key, properties.getProperty(key), false);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ScheduleManager manager = new ScheduleManager();
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String line = rd.readLine();
        while (!line.equals("exit")) {
            if (line.startsWith("addPlugin")) {
                String[] plu = line.split("=");
                manager.loadPlugin(plu[0], plu[1], true);
            }
        }

    }

    public void loadPlugin(String key, String path, boolean save) throws Exception {

        path = path.trim();
        key = key.trim();
        PluginI pluginI = (PluginI) Class.forName(path).getDeclaredConstructor()
                .newInstance();
        scheduler.addPlugin(pluginI);
        if (save) {
            BufferedWriter wr = new BufferedWriter(new FileWriter(propFile, true));
            wr.append(key + " = " + path);
            wr.newLine();
            wr.flush();
            wr.close();

        }
    }
}
