package ptp.peekthepast;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoList#newInstance} factory method to
 * create an instance of this fragment.
 */


public class VideoList extends Fragment implements HttpRequest.HttpRequestListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoList.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoList newInstance(String param1, String param2) {
        VideoList fragment = new VideoList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public VideoList() {
        // Required empty public constructor
    }


    public void vote(int id,boolean up)
    {
        ContentClassForListAdapterAndVideoList aStruct;
        // search fitting.
        for(int i = 0; i < myList().size(); i++) {
           aStruct =  myList().get(i);
            if(aStruct.id_of_video == id)
            {
                if(up)
                {
                    aStruct.points++;
                    myList().set(i,aStruct);
                    i=10000;
                }
                else
                {
                    aStruct.points--;
                    myList().set(i,aStruct);
                    i=10000;
                }
            }
        }

        // get data from the table by the ListAdapter
        ListAdapter customAdapter = new ListAdapterForVideoList(getActivity(), R.layout.view_element_prototype, myList());
        yourListView.setAdapter(customAdapter);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    public ListView yourListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_video_list, container, false);

        Spinner mySpinner = (Spinner) view.findViewById(R.id.spinner_sort);
        ArrayAdapter<CharSequence> spinneradapter;
        spinneradapter = ArrayAdapter.createFromResource(getActivity(), R.array.sorting_arrays, R.layout.spinner_layout);
        spinneradapter.setDropDownViewResource(R.layout.spinner_layout);
        mySpinner.setAdapter(spinneradapter);


        //---------------
        //Start of dynamic elemente creation
        //--------------

       /*LinearLayout theViewer = (LinearLayout) view.findViewById(R.id.linear_layout_viewer);
        //new
        LinearLayout s1 = new LinearLayout(getActivity());
        s1.setOrientation(LinearLayout.VERTICAL);
        s1.setBackgroundColor(0x40973B);
        s1.setPadding(0, 0, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
        LinearLayout.LayoutParams paramslin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        s1.setLayoutParams(paramslin);*/


       // ListView yourListView = (ListView) findViewById(R.id.itemListView);

// get data from the table by the ListAdapter
      //  ListAdapter customAdapter = new ListAdapter(this, R.layout.itemlistrow, List<yourItem>);
      //
      // yourListView .setAdapter(customAdapter);


        HttpRequest req = new HttpRequest(this);
        req.getMoments(1,2,3); // TODO Zahlen iwann umändern wenn implementiert


        //------------------------------Bestücken



        //-------------End Bestücken


        //-----------------
        // End dymn ele crea
        //-----------------

        return view;

    }

    public void b_upvote(View view)
    {
        view.getId();

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

    private MainMenu gMM(){
        return (MainMenu) getActivity();
    }

    private ArrayList<ContentClassForListAdapterAndVideoList> myList() {
        return gMM().myList;
    }

    @Override
    public void momentsAvailable(ArrayList<oneMoment> Moments) {
        yourListView = (ListView) getActivity().findViewById(R.id.theListView);

        // get data from the table by the ListAdapter
        ContentClassForListAdapterAndVideoList aListItem;
        gMM().setMyList(new ArrayList<ContentClassForListAdapterAndVideoList>());

        for(int i=0; i < Moments.size(); i++) {
            aListItem = new ContentClassForListAdapterAndVideoList();
            aListItem.description = Moments.get(i).name;
            aListItem.id_of_video = Moments.get(i).id;
            aListItem.points = Moments.get(i).ranking;
            aListItem.url_to_video = Uri.parse(Moments.get(i).url);
            aListItem.url_toThumbnail = Moments.get(i).thumb;
            aListItem.lat = Moments.get(i).lat;
            aListItem.lng = Moments.get(i).lng;


             if(Moments.get(i).added.length() >= 9) {
                 aListItem.timeAndDate = Moments.get(i).added.substring(8, 10) + "." +
                         Moments.get(i).added.substring(5, 7) + "." +
                         Moments.get(i).added.substring(0, 5);
             }

            //---Image -- NOCH DEBUG!
          /*  String uri = "drawable/test_image";
            int imageResource = getResources().getIdentifier(uri, null, MainMenu.PACKAGE_NAME);
            Drawable res = getResources().getDrawable(imageResource);
            aListItem.thumbnail = res;*/



            /*
            try {
                URL url = new URL(Moments.get(i).thumb);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Drawable d = new BitmapDrawable(getResources(), bmp);
                aListItem.thumbnail = d;
            }catch(Exception e){}*/



          /*  try {
                InputStream is = (InputStream) new URL(Moments.get(i).thumb).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                aListItem.thumbnail =  d;
            } catch (Exception e) {
                String uri = "drawable/test_image";
                int imageResource = getResources().getIdentifier(uri, null, MainMenu.PACKAGE_NAME);
                Drawable res = getResources().getDrawable(imageResource);
                aListItem.thumbnail = res;
            }*/

            //---/Image

            myList().add(aListItem);
        }
        ListAdapter customAdapter = new ListAdapterForVideoList(getActivity(), R.layout.view_element_prototype, myList());
        yourListView.setAdapter(customAdapter);
        HttpRequest req = new HttpRequest(this);
        for(int i = 0; i < myList.size(); i++) {
            req.getImage(myList.get(i).url_toThumbnail,myList.get(i).id_of_video);
        }
    }

    @Override
    public void drawAbleAvailable(Drawable thumb, int thumbid) {
        for(int i = 0; i < myList.size(); i++) {
            if (myList.get(i).id_of_video == thumbid) {
                myList.get(i).thumbnail = thumb;
            }
        }
        ListAdapter customAdapter = new ListAdapterForVideoList(getActivity(), R.layout.view_element_prototype, myList);
        yourListView.setAdapter(customAdapter);
    }

    @Override
    public void drawAbleAvailable(Drawable thumb, int thumbid) {

    }

    @Override
    public void failure(int nummer) {

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
