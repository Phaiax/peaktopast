package ptp.peekthepast;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link ptp.peekthepast.Recorder.OnRecordingFinishedListener} interface
 * to handle interaction events.
 * Use the {@link Recorder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recorder extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Camera mCamera;
    private String filename;

    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;

    private OnRecordingFinishedListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recorder.
     */
    // TODO: Rename and change types and number of parameters
    public static Recorder newInstance(String param1, String param2) {
        Recorder fragment = new Recorder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Recorder() {
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
        View view = inflater.inflate(R.layout.fragment_recorder, container, false);

        // Add a listener to the Capture button
        Button captureButton = (Button) view.findViewById(R.id.button_record);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        if(!isRecording) {
                            record();
                        } else {
                            stopRecording();
                        }
                    }
                }
        );

        Button stopButton = (Button) view.findViewById(R.id.button_stop);

        stopButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        // mCamera.takePicture(null, null, mPicture);
                    }
                }
        );


        return view;
    }

    private void activatePreview() {
        if(mCamera == null) {
            Log.e("ptp", "Create Camera instance");
            mCamera = getCameraInstance(getActivity());
            configureCamera();
        }
        if(mCamera != null) {
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(getActivity(), mCamera);
            FrameLayout preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        } else {
            Log.e("ptp", "activatePreview: no Camera");
        }

    }


    private void record() {
        if(!isRecording && prepareRecording()) {
            Log.e("ptp", "start recording");
            mMediaRecorder.start();
            isRecording = true;
        } else {
            Log.e("ptp", "Error @ record");
        }
    }


    private void stopRecording() {
        if(isRecording) {
            Log.e("ptp", "stop recording");
            mMediaRecorder.stop();
            mCamera.lock();
            mCamera.stopPreview();
            releaseMediaRecorder();
            releaseCamera();
            isRecording = false;
            mListener.onRecordingFinished(filename);
            return;
        } else {
            Log.e("ptp", "Error @ Stop Record");
        }
        activatePreview();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopPreview();
            mPreview.releaseCamera();
            mPreview = null;
            FrameLayout preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
            preview.removeAllViews();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private boolean prepareRecording() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                Log.e("ptp", "MRE: " + String.valueOf(what) + " " + String.valueOf(extra));
            }
        });

        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                Log.e("ptp", "MRINFO: " + String.valueOf(what) + " " + String.valueOf(extra));
            }
        });

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        // Step 4: Set output file
        filename = TmpFiles.getOutputMediaFile(TmpFiles.MEDIA_TYPE_VIDEO).toString();
        Log.e("ptp", "Filename: " + filename);
        mMediaRecorder.setOutputFile(filename);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("ptp", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("ptp", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void configureCamera() {
        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();
        // set the focus mode
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        // set Camera parameters
        mCamera.setParameters(params);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRecordingFinishedListener) activity;
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

    @Override
    public void onResume() {
        super.onResume();
        activatePreview();
    }

    @Override
    public void onPause() {
        stopRecording();
        releaseCamera();
        super.onPause();
    }



    /** Check if this device has a camera */
    private static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    private static Camera getCameraInstance(Context context){
        Camera c = null;
        if (checkCameraHardware(context)) {
            try {
                c = Camera.open(); // attempt to get a Camera instance
            } catch (Exception e) {
                // Camera is not available (in use or does not exist)
            }
        }
        return c; // returns null if camera is unavailable

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
    public interface OnRecordingFinishedListener {
        // TODO: Update argument type and name
        public void onRecordingFinished(String videoFile);
    }

}
