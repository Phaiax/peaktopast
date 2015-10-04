package ptp.peekthepast;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , Recorder.OnRecordingFinishedListener, VideoList.OnFragmentInteractionListener, Mariusu.OnFragmentInteractionListener, NewVideoForm.OnVideodataEnteredListener {


    public static String PACKAGE_NAME;

    public String lastVideoFile;
    private String thumbnailFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PACKAGE_NAME = getApplicationContext().getPackageName();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            if (findViewById(R.id.fragment_container) != null) {
                VideoList myFragment = VideoList.newInstance("","");

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myFragment)
                        .addToBackStack(null)
                        .commit();
                getSupportActionBar().show();
            }
        }
       /* else{
            super.onBackPressed();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ContentClassForListAdapterAndVideoList getCCbyMomentid(int momentid) {
        for (int i = 0 ; i < myList.size(); i++) {
            if(myList.get(i).id_of_video == momentid)
                return myList.get(i);
        }
        return null;
    }

    public void openAmoment(int momentid)
    {
        ContentClassForListAdapterAndVideoList moment = getCCbyMomentid(momentid);

        if (findViewById(R.id.fragment_container) != null) {
            Mariusu myFragment = Mariusu.newInstance(this , moment.url_to_video.toString(), "");

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, myFragment)
                    .addToBackStack(null)
                    .commit();
            //hideTitle();
            getSupportActionBar().hide();

        }
    }


    // Hide Status Bar
    public void hideTitle() {
        try {
            ((View) findViewById(android.R.id.title).getParent())
                    .setVisibility(View.GONE);
        } catch (Exception e) {
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
    public void showTitle() {
        try {
            ((View) findViewById(android.R.id.title).getParent())
                    .setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public ArrayList<ContentClassForListAdapterAndVideoList> myList;

    public void setMyList(ArrayList<ContentClassForListAdapterAndVideoList> _myList) {
        myList = _myList;
    }

    public void openVideoList()
    {
        if (findViewById(R.id.fragment_container) != null) {
            VideoList myFragment = VideoList.newInstance("","");

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, myFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //   FragmentManager fm = getFragmentManager();


        if (id == R.id.nav_explore) {

            startActivity(new Intent(this, ExploreWorld.class));


        } else if (id == R.id.nav_view) {


            if (findViewById(R.id.fragment_container) != null) {
                VideoList myFragment = VideoList.newInstance("","");

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myFragment)
                        .addToBackStack(null)
                        .commit();
            }
        } else if (id == R.id.nav_create) {

            // Check that the activity is using the layout version with
            // the fragment_container FrameLayout
            if (findViewById(R.id.fragment_container) != null) {

                // However, if we're being restored from a previous state,
                // then we don't need to do anything and should return or else
                // we could end up with overlapping fragments.
               /* if (savedInstanceState != null) {
                    return;
                }*/

                // Create a new Fragment to be placed in the activity layout
                Recorder myRecorder = Recorder.newInstance("","");

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                //myRecorder.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, myRecorder)
                                    .addToBackStack(null)
                                    .commit();

            }


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void b_onImageViewClick(View view)
    {
        int welchesElement = (int)view.getTag();
        openAmoment(welchesElement);
    }
    public void b_downvote_click(View view)
    {
        int welchesElement = (int)view.getTag();
        // find fitting element of element list.
        VideoList fragment = (VideoList) getFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.vote(welchesElement, false);

    }
    public void b_upvote_click(View view)
    {
        int welchesElement = (int)view.getTag();
        VideoList fragment = (VideoList) getFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.vote(welchesElement,true);
    }

    public void vidiview(View view)
    {
        getSupportActionBar().hide();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRecordingFinished(String videoFile, String thumbnailFile) {
        this.lastVideoFile = videoFile;
        this.thumbnailFile = thumbnailFile;


        NewVideoForm myFragment = NewVideoForm.newInstance(lastVideoFile, thumbnailFile);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, myFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onVideodataEntered(String title, float lat, float lng) {
        UploadVideo U = new UploadVideo(lastVideoFile, thumbnailFile, title, lat, lng, this);
    }
}
