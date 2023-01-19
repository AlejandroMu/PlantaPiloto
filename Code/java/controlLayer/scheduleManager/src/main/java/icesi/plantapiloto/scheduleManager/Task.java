package icesi.plantapiloto.scheduleManager;

import java.util.TimerTask;

import icesi.plantapiloto.controlLayer.common.PluginI;
import icesi.plantapiloto.controlLayer.common.entities.Message;

public class Task extends TimerTask {
    private PluginI plugin;
    private PublisherManager publisher;

    public Task(PluginI p, PublisherManager pi) {
        plugin = p;
        publisher = pi;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        Message messages = plugin.getMessage();
        publisher.addMessage(messages);
        System.out.println("tiempo de la tarea: " + (System.currentTimeMillis() - time) + " ms");
    }

}
