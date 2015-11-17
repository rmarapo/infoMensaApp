package mensa.info.application.org.infomensaapp.service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mensa.info.application.org.infomensaapp.sql.model.Menu;

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

    protected Object parseResult(String result)
    {

        List<Menu> menuList = new ArrayList<>();

        try
        {
            JSONObject response = new JSONObject(result);

            JSONArray dieta = response.optJSONArray("menu");

            for (int i = 0; i < dieta.length()-1; i++)
            {
                JSONObject sdieta = dieta.optJSONObject(i);

                menuList.add(new Menu(sdieta.getString("codice_fiscale"), sdieta.getString("descrizione")));
                Log.d(TAG, "elemento : " + sdieta.getString("codice_fiscale") + " " + sdieta.getString("descrizione"));
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return menuList;
    }
}