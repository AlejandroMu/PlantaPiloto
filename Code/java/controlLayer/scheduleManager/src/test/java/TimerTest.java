import java.net.Inet4Address;
import java.net.InetAddress;

import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

    private Timer timer = new Timer();

    private class Task extends TimerTask {
        public void run() {

            Date date = Calendar.getInstance().getTime();

            System.out.println("date: " + date.toString());
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
            }

        }

    }

    public void schedule() {
        TimerTask task = new Task();
        long per = 1000;
        long time = System.currentTimeMillis();

        timer.scheduleAtFixedRate(task, new Date(time + 2000), 2 * per);
        System.out.println("prog2");
        timer.scheduleAtFixedRate(new Task(), new Date(time + 2000 + per), per);

    }

    public void adressTest() throws Exception {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        InetAddress i1 = InetAddress.getByName("10.147.19.50");
        boolean isv4 = false;
        if (i1 instanceof Inet4Address) {
            isv4 = true;
        }

        while (nets.hasMoreElements()) {
            NetworkInterface net = nets.nextElement();
            boolean reach = i1.isReachable(net, 0, 100);
            if (reach) {
                System.out.println("Network: " + net.getName());
                Enumeration<InetAddress> adres = net.getInetAddresses();
                while (adres.hasMoreElements()) {
                    InetAddress same = adres.nextElement();
                    boolean sisv4 = false;
                    if (same instanceof Inet4Address) {
                        sisv4 = true;
                    }
                    if ((sisv4 && isv4) || (!sisv4 && !isv4)) {
                        System.out.println("    Adress: " + same.getHostAddress());

                    }

                }

            }
        }
        System.out.println("End");
    }

    public static void main(String[] args) {
        try {

            TimerTest test = new TimerTest();
            test.adressTest();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
