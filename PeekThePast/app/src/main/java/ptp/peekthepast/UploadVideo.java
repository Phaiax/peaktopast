package ptp.peekthepast;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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


    private void sendVideoMetainfos() {
        Log.e("ptp", getOutputFromUrl("http://ptpbackend.cloudapp.net/list"));
    }

    private  void  sendVideoFile() {

    }

    private  void deleteVideoFileFromSd() {

    }

    private String getOutputFromUrl(String url) {
        StringBuffer output = new StringBuffer("");
        try {
            InputStream stream = getHttpConnection(url);
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
    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
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
