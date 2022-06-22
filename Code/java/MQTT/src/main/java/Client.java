import java.util.*;
import MQTT.Mqtt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import opto22.mmp.Opto22MMP;

public class Client {
    public static void main(String[] args)throws java.lang.Exception {
        System.out.println(Arrays.toString(args));
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

        Mqtt mqtt = new Mqtt(id,host);
        Opto22MMP opto= new Opto22MMP("epic1");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("=> Suscriber? Y/n: ");
        String line = br.readLine();
        if(line!=null && line.equals("n")){
            System.out.print("=> topic?: ");
            String topic = br.readLine();
            System.out.print("=> msg?: ");
            line = br.readLine();
            while(!line.equals("exit")){
                mqtt.publish(topic,id+": "+line);
                System.out.print("=> msg?: ");
                line = br.readLine();
            }
        }else{

            System.out.print("=> topic?: ");
            line = br.readLine();
            mqtt.suscribe(line,publish -> {
                System.out.println("Received message on topic " + publish.getTopic() + ": " +
                        new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8));
            });
            br.readLine();

        }
        br.close();
        mqtt.diconnect();
    }
}
