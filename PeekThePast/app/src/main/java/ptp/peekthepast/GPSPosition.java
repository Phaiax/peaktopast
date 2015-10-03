package ptp.peekthepast;

import android.os.Handler;
import android.os.Message;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by daniel on 03.10.15.
 */
public class GPSPosition {

    static float lat;
    static float lng;

    static PositionAvailableListener L;

    public static void getPosition(PositionAvailableListener L) {
        Random r = new Random();
        GPSPosition.L = L;
        lat = 47.401727f + r.nextFloat();
        lng = 8.508642f + r.nextFloat();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(new Message());
            }
        }, 3);
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            L.onPositionAvailable(lat, lng);
        }
    };

    public interface PositionAvailableListener {
        void onPositionAvailable(float lat, float lng);
    }
}
