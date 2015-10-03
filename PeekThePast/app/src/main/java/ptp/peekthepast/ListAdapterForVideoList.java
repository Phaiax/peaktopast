package ptp.peekthepast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Simon on 03.10.2015.
 */
public class ListAdapterForVideoList extends ArrayAdapter<ContentClassForListAdapterAndVideoList> {

    public ListAdapterForVideoList(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapterForVideoList(Context context, int resource, List<ContentClassForListAdapterAndVideoList> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext()); //evtl mit nur context versuchen von übergebenenen
            v = vi.inflate(R.layout.view_element_prototype, null);
        }

        ContentClassForListAdapterAndVideoList p = getItem(position);

        if (p != null) {
            ImageView thumbn = (ImageView) v.findViewById(R.id.imageView);
            TextView timeAndDate = (TextView) v.findViewById(R.id.textViewTimeAndDate);
            TextView textViewScore = (TextView) v.findViewById(R.id.textViewScore);
            TextView textViewDescribtion = (TextView) v.findViewById(R.id.textViewDescribtion1);

            if (timeAndDate != null) {
                timeAndDate.setText(p.timeAndDate);
            }

            if (thumbn != null) {
                thumbn.setImageDrawable(p.thumbnail);
            }

            if (textViewScore != null) {
                textViewScore.setText(String.valueOf(p.points));
            }
            if (textViewDescribtion != null) {
                textViewDescribtion.setText(p.description);
            }
            if (thumbn != null) {
                thumbn.setImageDrawable(p.thumbnail);
                thumbn.setTag(p.id_of_video);
            }


        }

        return v;
    }

}
