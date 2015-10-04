package ptp.peekthepast;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ptp.peekthepast.NewVideoForm.OnVideodataEnteredListener} interface
 * to handle interaction events.
 * Use the {@link NewVideoForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewVideoForm extends Fragment implements  GPSPosition.PositionAvailableListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "videoFile";
    private static final String ARG_PARAM2 = "thumbnailFile";

    // TODO: Rename and change types of parameters
    private String videoFile;
    private String thumbnailFile;

    private OnVideodataEnteredListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param videoFile Parameter 1.
     * @return A new instance of fragment NewVideoForm.
     */
    // TODO: Rename and change types and number of parameters
    public static NewVideoForm newInstance(String videoFile, String thumbnailFile) {
        NewVideoForm fragment = new NewVideoForm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, videoFile);
        args.putString(ARG_PARAM2, thumbnailFile);
        fragment.setArguments(args);
        return fragment;
    }

    public NewVideoForm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoFile = getArguments().getString(ARG_PARAM1);
            thumbnailFile = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_video_form, container, false);

        view.findViewById(R.id.button_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUploadMessage();
                mListener.onVideodataEntered(String.valueOf(getTitleTextView().getText()),
                        lat, lng);
                Log.e("ptp", "Create now");
            }
        });

        view.findViewById(R.id.text_moment_title).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                checkCanUpload();
                return false;
            }
        });

        GPSPosition.getPosition((GPSPosition.PositionAvailableListener) this);

        return view;
    }


    private void displayUploadMessage() {
        getSubmitbutton().setEnabled(false);
        getProgressbar().setVisibility(View.VISIBLE);
        getSubmitbutton().setText("Uploading ...");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onVideodataEntered("", 0, 0);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnVideodataEnteredListener) activity;
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

    private float lat;
    private float lng;

    @Override
    public void onPositionAvailable(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
        TextView t = (TextView) getView().findViewById(R.id.text_gps_pos);
        t.setText(String.valueOf(lat) + ", " + String.valueOf(lng));
        checkCanUpload();
    }

    private TextView getTitleTextView() {
        return (TextView) getView().findViewById(R.id.text_moment_title);
    }

    private Button getSubmitbutton() {
        return (Button) getView().findViewById(R.id.button_upload);
    }
    private ProgressBar getProgressbar() {
        return (ProgressBar) getView().findViewById(R.id.upload_progressbar);
    }

    public void checkCanUpload() {
        if (lat != 0f && lng != 0f
                && getTitleTextView().getText() != ""
                && videoFile != "") {
            getSubmitbutton().setEnabled(true);
        } else {
            getSubmitbutton().setEnabled(false);
        }
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
    public interface OnVideodataEnteredListener {
        // TODO: Update argument type and name
        public void onVideodataEntered(String title, float lat, float lng);
    }

}
