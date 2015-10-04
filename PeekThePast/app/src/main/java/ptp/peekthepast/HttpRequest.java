package ptp.peekthepast;


import android.graphics.drawable.Drawable;

import java.util.ArrayList;


/**
 * Created by Marius on 03.10.2015.
 */
public class HttpRequest {

    private HttpRequestListener pointer;
    public float lat;
    public float lng;
    public float range;
    public String thumburl;
    public int thumbid;
    public int whatreq;


    HttpRequest(HttpRequestListener pointer){
        this.pointer = pointer;
    }
    void getMoments(float lat, float lng, float range){

        this.lat = lat;
        this.lng = lng;
        this.range = range;
        whatreq=1;
        new AsyncHttp(pointer).execute(this);
    }
    void getImage(String thumburl, int thumbid){

        this.thumburl = thumburl;
        this.thumbid = thumbid;
        whatreq=2;
        new AsyncHttp(pointer).execute(this);
    }




    interface HttpRequestListener {
        void momentsAvailable(ArrayList<oneMoment> Moments);
        void drawAbleAvailable(Drawable thumb, int thumbid);
        void failure(int nummer);
    }
}
