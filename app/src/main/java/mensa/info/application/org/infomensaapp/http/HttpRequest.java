package mensa.info.application.org.infomensaapp.http;

import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import mensa.info.application.org.infomensaapp.R;

/**
 * Created by giuseppegrosso on 11/11/15.
 */
public class HttpRequest
{

    public String text_return = "";


//    final TextView mTextView = (TextView) findViewById(R.id.text);

    public String httpRequest(Context context)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        text_return = ("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                text_return = ("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return text_return;
    }
}
