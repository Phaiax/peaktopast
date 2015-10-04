package ptp.peekthepast;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Mariusu imports

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mariusu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mariusu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mariusu extends Fragment implements HttpRequest.HttpRequestListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "url";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String url;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context context = null; //TODO MAKE SETTER

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mariusu.
     */
    // TODO: Rename and change types and number of parameters
    public static Mariusu newInstance(Context context, String url, String param2) {
        Mariusu fragment = new Mariusu();
        fragment.setContext(context);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, url);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Mariusu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }


    }






    public void momentsAvailable(ArrayList<oneMoment> Moments) {
        int a=0;
    }

    @Override
    public void drawAbleAvailable(Drawable thumb, int thumbid) {

    }

    public void failure(int nummer) {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //mariusu

        //initialise video view
        View view = inflater.inflate(R.layout.fragment_mariusu, container, false);
        VideoView vidView = (VideoView) view.findViewById(R.id.myVideo);

        //set url -> from database
        String vidAddress = url;
        Log.e("ptp", "Play: " + vidAddress);
        Uri vidUri = Uri.parse(vidAddress);







        HttpRequest test = new HttpRequest(this);
                test.getMoments(1, 1, (float)0.1);













        boolean useVLC=true;

        if(useVLC) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setPackage("org.videolan.vlc.betav7neon");
            i.setDataAndType(Uri.parse(url), "video/mp4");
            startActivity(i);

        } else {

            //load video => TODO as a second task / async
            vidView.setVideoURI(vidUri);

            //add mediacontroller --> die kontrolleinheit unten
            MediaController vidControl = new MediaController(getActivity());
            vidControl.setAnchorView(vidView);
            vidView.setMediaController(vidControl);

            //"autostart"
            vidView.start();
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


            //console output
            vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("ptp", "MPrepared: " + String.valueOf(mp.getDuration()));

                }
            });
            vidView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("ptp", "Error: " + String.valueOf(what) + "  " + String.valueOf(extra));
                    return false;
                }
            });
            vidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    VideoList frag = VideoList.newInstance("", "");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, frag);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();


                }
            });
            Log.e("ptp", "play!");
        }
        return view;//inflater.inflate(R.layout.fragment_mariusu, container, false);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setContext(Context c) {
        this.context = c;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
