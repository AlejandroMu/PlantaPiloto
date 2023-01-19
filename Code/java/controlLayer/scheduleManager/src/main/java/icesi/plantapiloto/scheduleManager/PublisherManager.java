package icesi.plantapiloto.scheduleManager;

import java.util.ArrayDeque;

import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.controlLayer.common.envents.PublisherI;

public class PublisherManager extends Thread {
    private PublisherI publisherI;
    private ArrayDeque<Message> messages;
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
            Message mesg = null;
            boolean sended = true;
            try {
                synchronized (messages) {
                    if (!messages.isEmpty()) {
                        publisherI.connect();
                        while (!messages.isEmpty()) {
                            mesg = messages.poll();
                            sended = false;
                            publisherI.setTopic(mesg.getTopic());
                            publisherI.publish(mesg);
                            sended = true;
                        }
                        publisherI.close();

                    } else {
                        Thread.yield();
                    }

                }

            } catch (Exception e) {
                if (!sended) {
                    synchronized (messages) {
                        messages.addFirst(mesg);
                    }
                    sended = true;
                }
                System.out.println("FAIL SEND MESSAGE");
            }

        }
    }

}
