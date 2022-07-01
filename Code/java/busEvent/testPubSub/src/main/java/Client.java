import java.util.*;

import icesi.plantapiloto.busEvent.MessageImp;
import icesi.plantapiloto.busEvent.PublisherI;
import icesi.plantapiloto.busEvent.SubscriberI;
import icesi.plantapiloto.icestorm.Publisher;
import icesi.plantapiloto.icestorm.Subscriber;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args)throws java.lang.Exception {
        String storm ="DemoIceStorm/TopicManager:default -h 10.147.19.165 -p 10000";
        Properties prop=new Properties();
        InputStream stream = new Client().getClass().getResourceAsStream("client.cfg");
        if(stream!=null){
            prop.load(stream);  
        }
        if(args.length>0){
            for (String string : args) {
                String[] p = string.split("=");
                prop.put(p[0],p[1]);
            }
        }else if(stream==null){
            throw new Exception("Falta configuración");   
        }
        String host = prop.getProperty("host");
        String id = prop.getProperty("id");
        System.out.println(id);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("=> Suscriber? Y/n: ");
        String line = br.readLine();
        if(line!=null && line.equals("n")){
            PublisherI pub = new Publisher(storm);
            System.out.print("=> topic?: ");
            String topic = br.readLine();
            pub.setTopic(topic);
            System.out.print("=> msg?: ");
            line = br.readLine();
            while(!line.equals("exit")){
                pub.publish(new MessageImp(id+": "+line));
                System.out.print("=> msg?: ");
                line = br.readLine();
            }
            pub.close();
        }else{
            SubscriberI subscriber = new Subscriber("tcp -h 10.147.19.165 -p 9099",storm);
            System.out.print("=> topic?: ");
            line = br.readLine();
            
            subscriber.subscribe(line,publish -> {
                System.out.println("Received message on topic " + publish.getValue());
            });

            br.readLine();
            subscriber.close();

        }
        br.close();
    }
}
