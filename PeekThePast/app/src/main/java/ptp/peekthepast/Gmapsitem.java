package ptp.peekthepast;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Marius on 03.10.2015.
 */
public class Gmapsitem implements ClusterItem {
    private final LatLng mPosition;

    public Gmapsitem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
