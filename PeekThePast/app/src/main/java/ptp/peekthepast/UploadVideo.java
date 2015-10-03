package ptp.peekthepast;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 03.10.15.
 */
public class UploadVideo {
    private String videoFile;
    private String title;
    private float lat;
    private float lng;

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

    private void sendVideoMetainfos() {
        Log.e("ptp", getOutputFromUrl("http://ptpbackend.cloudapp.net/list", null, GET));

        HashMap<String, String> kv = new HashMap<>();
        kv.put("lng", String.valueOf(lng));
        kv.put("lat", String.valueOf(lat));
        kv.put("name", enc(maxlen(title, 140)));

        Log.e("ptp", getOutputFromUrl("http://ptpbackend.cloudapp.net/newvideo", kv, POST));


    }

    private  void  sendVideoFile() {

    }

    private  void deleteVideoFileFromSd() {

    }

    static final int POST = 0;
    static final int GET = 1;

    private String getOutputFromUrl(String url, HashMap<String, String> kv, int type) {
        StringBuffer output = new StringBuffer("");
        try {
            InputStream stream = getHttpConnection(url, kv, type);
            if ( stream != null) {
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return output.toString();
    }

    // Makes HttpURLConnection and returns InputStream
    private InputStream getHttpConnection(String urlString, HashMap<String, String> kv, int type)
            throws IOException {
        InputStream stream = null;

        Uri.Builder builder = new Uri.Builder();
        if ( kv != null) {
            for(Map.Entry<String, String> entry : kv.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        String query = builder.build().getEncodedQuery();

        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setReadTimeout(10000);
            httpConnection.setConnectTimeout(15000);
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            if(type == POST) {
                httpConnection.setRequestMethod("POST");
                OutputStream os = httpConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            } else {
                httpConnection.setRequestMethod("GET");
            }
            httpConnection.connect();

            int RC = httpConnection.getResponseCode();
            if (RC == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

}
