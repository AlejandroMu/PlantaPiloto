package icesi.plantapiloto.scheduleManager;

import icesi.plantapiloto.controlLayer.common.PluginI;
import icesi.plantapiloto.controlLayer.common.PublisherI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    private PublisherI publisher;
    private List<TimerTask> plugins;
    private Timer timer;

    public Scheduler(PublisherI publisher) {
        this.publisher = publisher;
        this.plugins = new ArrayList<>();
        this.timer = new Timer();
    }

    public void addPlugin(PluginI pugI) {
        Calendar calendar = Calendar.getInstance();
        String[] startTime = pugI.getSettings().get("startHour").split(":");
        int hour = Integer.parseInt(startTime[0]);
        int minute = Integer.parseInt(startTime[1]);
        long lapse = Long.parseLong(pugI.getSettings().get("lapse"));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Task task = new Task(pugI, publisher);
        timer.schedule(task, 0, lapse);
        plugins.add(task);

    }
}
