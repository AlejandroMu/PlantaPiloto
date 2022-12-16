package icesi.plantapiloto.scheduleManager;

import icesi.plantapiloto.controlLayer.common.PluginI;
import icesi.plantapiloto.controlLayer.common.envents.PublisherI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    private PublisherI publisher;
    private PublisherManager publusherManager;
    private List<TimerTask> plugins;
    private Timer timer;

    public Scheduler(PublisherI publisher) {
        this.publisher = publisher;
        this.plugins = new ArrayList<>();
        this.timer = new Timer();
        this.publusherManager = new PublisherManager(this.publisher);
        this.publusherManager.start();
    }

    public void addPlugin(PluginI pugI) {
        String startHour = pugI.getSettings().get("startHour");
        long lapse = Long.parseLong(pugI.getSettings().get("lapse"));
        if (startHour == null) {
            Task task = new Task(pugI, publusherManager);
            timer.schedule(task, 0, lapse);
            plugins.add(task);
            return;
        }
        String[] startTime = startHour.split(":");
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(startTime[0]);
        int minute = Integer.parseInt(startTime[1]);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Task task = new Task(pugI, publusherManager);
        timer.schedule(task, calendar.getTime(), lapse);
        plugins.add(task);

    }
}
