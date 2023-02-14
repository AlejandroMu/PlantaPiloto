package icesi.plantapiloto.scheduleManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayDeque;

import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.controlLayer.common.envents.PublisherI;

public class PublisherManager extends Thread {
    public static final String FILE_PATH = "data";
    public static final int MAX_SIZE = 10;
    private PublisherI publisherI;
    private ArrayDeque<Message> messages;
    private boolean stop;
    private File file;

    /**
     * @param publisherI
     */
    public PublisherManager(PublisherI publisherI) {
        this.publisherI = publisherI;
        file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Error al cargar el archivo");
            }
        }
        messages = new ArrayDeque<>();
    }

    public void addMessage(Message message) {
        Thread thread = new Thread(() -> {
            synchronized (messages) {
                if (messages.size() < MAX_SIZE) {
                    messages.add(message);
                } else {
                    writeMessage(message);
                }

            }
        });
        thread.start();
    }

    private void writeMessage(Message message) {
        try {
            synchronized (file) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.append(publisherI.getEncoder().encode(message));
                writer.newLine();
                writer.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
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
                        readFile();
                        Thread.yield();
                    }

                }

            } catch (Exception e) {
                if (!sended) {
                    addMessage(mesg);
                }
                System.out.println("FAIL SEND MESSAGE");
            }

        }
    }

    private void readFile() {
        try {
            synchronized (file) {
                StringBuilder builder = new StringBuilder();

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null && !line.equals("")) {
                    if (messages.size() < MAX_SIZE) {
                        Message message = publisherI.getEncoder().decode(line, Message.class);
                        messages.add(message);
                    } else {
                        builder.append(line);
                        builder.append("\n");
                    }
                    line = reader.readLine();
                }
                reader.close();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.append(builder.toString());
                bw.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
