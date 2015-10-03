package ptp.peekthepast;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

//Mariusu imports

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mariusu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mariusu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mariusu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
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
    public static Mariusu newInstance(Context context, String param1, String param2) {
        Mariusu fragment = new Mariusu();
        fragment.setContext(context);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }












    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //mariusu

        //initialise video view
        View view = inflater.inflate(R.layout.fragment_mariusu, container, false);
        VideoView vidView = (VideoView) view.findViewById(R.id.myVideo);

        //set url -> from database
        String vidAddress = "https://mediasvcwz09mqf0j8nqs.blob.core.windows.net/asset-90c93a5d-1500-80c4-5eef-f1e56973305c/ksnn_compilation_master_the_internet_512kb.mp4?sv=2012-02-12&sr=c&si=8b4b4d83-f7d1-4714-8ca3-8f20295dd2ec&sig=I8BN5FNzpVt7f0ECj7oA2Rx3D0vl2O4NChXoCCjshUQ%3D&st=2015-10-03T02%3A04%3A37Z&se=2115-09-09T02%3A04%3A37Z";
        Uri vidUri = Uri.parse(vidAddress);

        //load video => TODO as a second task / async
        vidView.setVideoURI(vidUri);

        //add mediacontroller
        MediaController vidControl = new MediaController(getActivity());
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);

        //"autostart"
        vidView.start();


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
        Log.e("ptp", "play!");

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
