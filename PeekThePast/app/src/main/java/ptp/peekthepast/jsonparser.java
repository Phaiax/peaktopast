package ptp.peekthepast;


import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Marius on 03.10.2015.
 */
public class jsonparser {


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public  ArrayList<oneMoment> parse(String json) {
        Gson gson = new Gson();
        ArrayList<oneMoment> collection = new ArrayList();
        JsonParser parser = new JsonParser();
        if(json!="") {
            JsonArray array = parser.parse(json).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                oneMoment entry = gson.fromJson(array.get(i), oneMoment.class);
                collection.add(entry);
            }
            return collection;
        }
        else
        {
            return collection;
        }

    }



}
