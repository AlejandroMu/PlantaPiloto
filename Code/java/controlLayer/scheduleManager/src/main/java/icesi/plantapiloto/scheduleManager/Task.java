package icesi.plantapiloto.scheduleManager;

import java.util.List;
import java.util.TimerTask;

import icesi.plantapiloto.controlLayer.common.Message;
import icesi.plantapiloto.controlLayer.common.PluginI;
import icesi.plantapiloto.controlLayer.common.PublisherI;

public class Task extends TimerTask {
    private PluginI plugin;
    private PublisherI publisher;

    public Task(PluginI p, PublisherI pi) {
        plugin = p;
        publisher = pi;
        String channel = plugin.getSettings().get("topic");
        publisher.setTopic(channel);
    }

    @Override
    public void run() {

        List<Message> messages = plugin.getValues();
        for (Message messages2 : messages) {
            publisher.publish(messages2);
        }
    }

}
