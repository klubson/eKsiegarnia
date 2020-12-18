package models;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Current_date {
    private static String time;
    private static boolean status = true;
    private Timer timer= new Timer();
    private void setTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        Current_date.time = formatter.format(date);
    }
    public void clock(JLabel label){
        status = true;
        int delay = 1000, period = 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                label.setText(getTime());
                if(status == false){
                    timer.cancel();
                }
            }
        }, delay, period);

    }
    public String getTime(){
        setTime();
        return time;
    }

    public void stopClock(){
        status = false;
    }
}
