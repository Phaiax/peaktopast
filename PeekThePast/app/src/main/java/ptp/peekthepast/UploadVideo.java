package ptp.peekthepast;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.loopj.android.http.*;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by daniel on 03.10.15.
 */
public class UploadVideo {
    private String videoFile;
    private String title;
    private float lat;
    private float lng;

    private long id;
    private String url;

    public UploadVideo(String videoFile, String title, float lat, float lng) {
        this.videoFile = videoFile;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        Log.e("ptp", "Req");


        DownloadFilesTask d = new DownloadFilesTask();
        d.execute(this);
    }


    private class DownloadFilesTask extends AsyncTask<UploadVideo, Integer, Integer> {
        protected Integer doInBackground(UploadVideo... uploadvideoinstances) {
            UploadVideo uploadvideoinstance = uploadvideoinstances[0];

            publishProgress(0);
            uploadvideoinstance.sendVideoMetainfos();
            publishProgress(30);
            if (isCancelled()) return 1;
            uploadvideoinstance.sendVideoFile();
            publishProgress(60);
            if (isCancelled()) return 1;
            uploadvideoinstance.deleteVideoFileFromSd();
            publishProgress(90);

            return 1;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
            Log.e("ptp", "Background Progress: " + String.valueOf(progress[0]));
        }

        protected void onPostExecute(Integer result) {
            Log.e("ptp", "Background Task finished");
        }
    }

    private String enc(String unsafe) {
        return URLEncoder.encode(unsafe);
    }

    private String maxlen(String longer, int maxlen) {
        if (longer.length() > maxlen) {
            return  longer.substring(0, maxlen);
        }
        return longer;
    }

    // {"upload_to": "upload", "refid": 4}
    class MetainfoResponse {
        String upload_to;
        int refid;
    }

    private void sendVideoMetainfos() {
        //Log.e("ptp", getOutputFromUrl("http://ptpbackend.cloudapp.net/list", null, GET, null));

        RequestParams params = new RequestParams();
        params.put("lng", String.valueOf(lng));
        params.put("lat", String.valueOf(lat));
        params.put("name", enc(maxlen(title, 140)));

        SyncHttpClient client = new SyncHttpClient();
        client.post("http://ptpbackend.cloudapp.net/newvideo", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String json = "";
                try {
                    json = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("ptp", json);


                Gson gson = new Gson();
                MetainfoResponse R = gson.fromJson(json, MetainfoResponse.class);
                id = R.refid;

                Log.e("ptp", String.valueOf(id));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });



    }

    private  void  sendVideoFile() {

        RequestParams params = new RequestParams();
        params.put("refid", String.valueOf(id));
        File myFile = new File(videoFile);
        try {
            params.put("file", myFile);
        } catch(FileNotFoundException e) {
            return;
        }

        SyncHttpClient client = new SyncHttpClient();
        client.post("http://ptpbackend.cloudapp.net/upload", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                try {
                    url = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("ptp", url);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String S = null;
                try {
                    S = new String(errorResponse, "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                Log.e("ptp", S);

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }



    private  void deleteVideoFileFromSd() {

    }



}
