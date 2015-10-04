package ptp.peekthepast;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import static ptp.peekthepast.R.layout.activity_explore_world;

public class ExploreWorld extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private class PersonRenderer extends DefaultClusterRenderer<Gmapsitem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;
    public PersonRenderer() {
        super(getApplicationContext(), mMap, mClusterManager);

        //View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
        View multiProfile = getLayoutInflater().inflate(R.layout.activity_explore_world,null);
        mClusterIconGenerator.setContentView(multiProfile);
        mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

        mImageView = new ImageView(getApplicationContext());
        //mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
        mDimension=200;
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        //int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
        int padding =3;
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);

    }

    @Override
    protected void onBeforeClusterItemRendered(Gmapsitem person, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
        mImageView.setImageResource(1);
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title("unnamed");
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Gmapsitem> cluster, MarkerOptions markerOptions) {
        // Draw multiple people.
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
        List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
        int width = mDimension;
        int height = mDimension;

        for (Gmapsitem p : cluster.getItems()) {
            // Draw 4 at most.
            if (profilePhotos.size() == 4) break;
            Drawable drawable = getResources().getDrawable(1);
            drawable.setBounds(0, 0, width, height);
            profilePhotos.add(drawable);
        }
        MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
        multiDrawable.setBounds(0, 0, width, height);

        mClusterImageView.setImageDrawable(multiDrawable);
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_explore_world);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
    //    LatLng sydney = new LatLng(-34, 151);
    //    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        setUpClusterer();
    }






    private ClusterManager<Gmapsitem> mClusterManager;
    private void setUpClusterer() {
        // Declare a variable for the cluster manager.


        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Gmapsitem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();

    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 47.389929;
        double lng = 8.515212;
        Gmapsitem offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);


        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 47.389959;
        lng = 8.510211;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 47.389919;
        lng = 8.513209;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 47.389039;
        lng = 8.513250;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 47.389922;
        lng = 8.515233;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 47.389245;
        lng = 8.515233;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 47.389467;
        lng = 8.515714;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 45.832634;
        lng = 6.865187;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        lat = 45.832234;
        lng = 6.865187;
        offsetItem = new Gmapsitem(lat, lng);
        mClusterManager.addItem(offsetItem);

        mMap.setMyLocationEnabled(true); // Simon 623

        }


    private void updateOwnLocation()
    {

    }

}
