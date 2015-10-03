package ptp.peekthepast;

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
import java.util.Collection;

/**
 * Created by Marius on 03.10.2015.
 */
public class AsyncHttp extends AsyncTask<HttpRequest, String, String> {

    private HttpRequest.HttpRequestListener pointer;
    AsyncHttp(HttpRequest.HttpRequestListener pointer){
        this.pointer = pointer;
    }




    @Override
        protected String doInBackground(HttpRequest... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            String fullurl="http://ptpbackend.cloudapp.net/list";//+uri[0].lat
            try {
                response = httpclient.execute(new HttpGet(fullurl));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result!="") {
                jsonparser parser = new jsonparser();
                Collection momentlist = parser.parse(result);

                pointer.momentsAvailable(momentlist);
            }
            else
            {
                pointer.failure(1);
            }


        }

}
