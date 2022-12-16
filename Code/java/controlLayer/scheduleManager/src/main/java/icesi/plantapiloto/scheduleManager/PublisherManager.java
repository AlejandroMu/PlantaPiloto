package icesi.plantapiloto.scheduleManager;

import java.util.ArrayDeque;
import java.util.Queue;

import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.controlLayer.common.envents.PublisherI;

public class PublisherManager extends Thread {
    private PublisherI publisherI;
    private Queue<Message> messages;
    private boolean stop;

    /**
     * @param publisherI
     */
    public PublisherManager(PublisherI publisherI) {
        this.publisherI = publisherI;
        messages = new ArrayDeque<>();
    }

    public void addMessage(Message message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    public void stopTask(boolean c) {
        stop = c;
    }

    public void run() {
        while (!stop) {
            try {
                if (!messages.isEmpty()) {

                    Queue<Message> temp = messages;
                    synchronized (messages) {
                        messages = new ArrayDeque<>();
                    }
                    publisherI.connect();
                    while (!temp.isEmpty()) {
                        Message mesg = temp.poll();
                        publisherI.setTopic(mesg.getTopic());
                        publisherI.publish(mesg);
                    }
                    publisherI.close();

                } else {
                    Thread.yield();
                }

            } catch (Exception e) {
                System.out.println("FAIL SEND MESSAGE");
            }

        }
    }

}
