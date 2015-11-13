package mensa.info.application.org.infomensaapp.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Creato da Giuseppe Grosso in data 13/11/15.
 */
public class MenuDelGiornoService extends DownloadAbstractService
{

    private static final String TAG = "MenuDelGiornoService";

    public MenuDelGiornoService()
    {
        super(MenuDelGiornoService.class.getName());
    }


    protected String convertInputStreamToString(InputStream inputStream) throws IOException
    {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null)
        {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream)
        {
            inputStream.close();
        }

        return result;
    }

    protected String[] parseResult(String result)
    {

        String[] blogTitles = null;

        blogTitles = new String[4];

        blogTitles[0] = "Pasta al ragu di manzo";
        blogTitles[1] = "Stracchino";
        blogTitles[2] = "Insalata mista";
        blogTitles[3] = "budino";

//        try
//        {
//            JSONObject response = new JSONObject(result);
//            JSONArray posts = response.optJSONArray("posts");
//            blogTitles = new String[posts.length()];
//
//            for (int i = 0; i < posts.length(); i++)
//            {
//                JSONObject post = posts.optJSONObject(i);
//                String title = post.optString("title");
//                blogTitles[i] = title;
//            }


//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
        return blogTitles;
    }
}