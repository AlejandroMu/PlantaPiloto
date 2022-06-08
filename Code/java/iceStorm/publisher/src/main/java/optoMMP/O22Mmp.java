package optoMMP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class O22Mmp {

    private Socket socket;


    public O22Mmp(String host, int port){
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public String readSoocket(){
        String str = "";
        try {
            BufferedReader lec=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter es=new PrintWriter(socket.getOutputStream(),true);
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        return str;
    }
    
}
