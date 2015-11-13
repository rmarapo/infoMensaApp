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

        String[] composizioneMenu = null;

        try
        {
            JSONObject response = new JSONObject(result);
            JSONArray menu = response.optJSONArray("menu");
            composizioneMenu = new String[menu.length()];
            for (int i = 0; i < menu.length(); i++)
            {
                JSONObject post = menu.optJSONObject(i);
                String title = post.optString("descrizione");
                composizioneMenu[i] = title;

            }
            for (int i = 0; i < menu.length(); i++)
            {
                Log.d(TAG, "elemento : " + composizioneMenu[i]);

            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return composizioneMenu;
    }
}