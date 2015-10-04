package ptp.peekthepast;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Marius on 03.10.2015.
 */
public class AsyncHttp extends AsyncTask<HttpRequest, String, String> {

    private HttpRequest.HttpRequestListener pointer;
    AsyncHttp(HttpRequest.HttpRequestListener pointer){
        this.pointer = pointer;
    }

    private Drawable thumbimg;
    private int thumbid;
    private int whatreq;



    @Override
        protected String doInBackground(HttpRequest... uri) {
        whatreq=uri[0].whatreq;
        if(uri[0].whatreq==1) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            String fullurl = "http://ptpbackend.cloudapp.net/list";//+uri[0].lat
            try {
                response = httpclient.execute(new HttpGet(fullurl));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }
        else if(uri[0].whatreq==2) {
            thumbid=uri[0].thumbid;
                try {
                    InputStream is = (InputStream) new URL(uri[0].thumburl).getContent();
                    thumbimg = Drawable.createFromStream(is, "src name");
                    return "suc";
                } catch (Exception e) {
                    return null;
                }

        }
        return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result!="" && whatreq==1) {
                jsonparser parser = new jsonparser();
                ArrayList<oneMoment> momentlist = parser.parse(result);

                pointer.momentsAvailable(momentlist);
            }
            else if(whatreq==1)
            {
                pointer.failure(1);
            }
            else if(whatreq==2 && result=="suc")
            {
                pointer.drawAbleAvailable(thumbimg,thumbid);
            }


        }

}
