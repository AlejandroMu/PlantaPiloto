import java.util.*;
import com.zeroc.Ice.*;
import MQTT.Mqtt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args)throws java.lang.Exception {
        List<String> extraArgs = new ArrayList<>();
        Communicator communicator = Util.initialize(args,"client.cfg",extraArgs);
        String host = communicator.getProperties().getProperty("host");
        String topic = communicator.getProperties().getProperty("topic");
        String id = communicator.getProperties().getProperty("id");

        Mqtt mqtt = new Mqtt(id,host);
     
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("=> Suscriber? Y/n: ");
        String line = br.readLine();
        if(line.equals("n")){
            System.out.print("=> topic?: ");
            topic = br.readLine();
            while(!line.equals("exit")){
                mqtt.publish(topic,id+": "+line);
            }
        }else{

            System.out.print("=> topic?: ");
            line = br.readLine();
            mqtt.suscribe(line,publish -> {
                System.out.println("Received message on topic " + publish.getTopic() + ": " +
                        new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8));
            });

        }
        br.close();
    }
}
