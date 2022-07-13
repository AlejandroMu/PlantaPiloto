import java.util.*;


import icesi.plantapiloto.busEvent.MessageImp;
import icesi.plantapiloto.busEvent.PublisherI;
import icesi.plantapiloto.busEvent.SubscriberI;
import icesi.plantapiloto.icestorm.Publisher;
import icesi.plantapiloto.icestorm.Subscriber;
import opto22.mmp.Opto22MMP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Client {
    private static boolean closed;
    public static void main(String[] args)throws java.lang.Exception {
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
        String channel = prop.getProperty("channel");
        String module = prop.getProperty("module");
        String epic = prop.getProperty("epic");
        String type = prop.getProperty("type");
        String sleep = prop.getProperty("sleep");




        System.out.println(id);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("=> Suscriber? Y/n: ");
        String line = br.readLine();
        if(line!=null && line.equals("n")){
            PublisherI pub = new Publisher(host);
            System.out.print("=> topic?: ");
            String topic = br.readLine();
            pub.setTopic(topic);
            closed = false;
            new Thread(()->{
                Opto22MMP opto = new Opto22MMP(epic);
                int m = Integer.parseInt(module);
                int c = Integer.parseInt(channel);
                try {
                    while (!closed) {
                        double val = 0;
                        if(type.equals("analog")){
                            val= opto.getAnalogPointValue(m, c);
                        }else if(type.equals("digital")){
                            val= opto.getDigitalPointState(m, c);
                        }
                        
                        pub.publish(new MessageImp("value: "+val));
                        Thread.sleep(Integer.parseInt(sleep));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }).start();
            System.out.print("=> msg?: ");
            line = br.readLine();
            while(!line.equals("exit")){
                pub.publish(new MessageImp(id+": "+line));
                System.out.print("=> msg?: ");
                line = br.readLine();
            }
            closed = true;
            pub.close();
        }else{
            SubscriberI subscriber = new Subscriber(id,host);
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
