package ptp.peekthepast;


import java.util.ArrayList;


/**
 * Created by Marius on 03.10.2015.
 */
public class HttpRequest {

    private HttpRequestListener pointer;
    public float lat;
    public float lng;
    public float range;


    HttpRequest(HttpRequestListener pointer){
        this.pointer = pointer;
    }
    void getMoments(float lat, float lng, float range){

        this.lat = lat;
        this.lng = lng;
        this.range = range;
        new AsyncHttp(pointer).execute(this);


    }



    interface HttpRequestListener {
        void momentsAvailable(ArrayList<oneMoment> Moments);
        void failure(int nummer);
    }
}
