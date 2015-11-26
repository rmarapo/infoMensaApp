package mensa.info.application.org.infomensaapp.service;

import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import mensa.info.application.org.infomensaapp.service.interfaces.DownloadAbstractService;
import mensa.info.application.org.infomensaapp.sql.helper.DatabaseHelper;
import mensa.info.application.org.infomensaapp.sql.model.Menu;

/**
 * Creato da Giuseppe Grosso in data 26/11/15.
 */
public class PresenzeMensiliService extends DownloadAbstractService
{

    private static final String TAG = "PresenzeMensiliService";

    public PresenzeMensiliService()
    {
        super(PresenzeMensiliService.class.getName());
    }

    /**
     * eseguo il parsing dei risultati e li restituisco nel bundle.
     *
     * @param result
     * @return
     */
    protected Object parseResult(String result)
    {

        List<Calendar> dayList = new ArrayList<>();

        try
        {
            JSONObject response = new JSONObject(result);

            JSONArray presenze = response.optJSONArray("presenze");
            Calendar day = null;
            for (int i = 0; i < presenze.length() - 1; i++)
            {
                JSONObject spresenza = presenze.optJSONObject(i);

                day = new GregorianCalendar(2015, 10, i);

                dayList.add(day);

                Log.w("prova", "trovata presenza " + spresenza.get("presenze"));
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return dayList;
    }

    @Override
    protected Object retriveDataFromDbase(Intent intent)
    {
        String data = intent.getStringExtra("data");
        String cf = intent.getStringExtra("cf");

//        // prendo i dati del menu del giorno.
//        MenuDelGiornoManager manager = new MenuDelGiornoManager();
//        List<Menu> allMenu = manager.getMenuDelGiorno(this.getApplicationContext(), data, cf);
//
//        // non so come fare!!!
//        if (allMenu != null && allMenu.size() > 0)
//        {
//            return allMenu;
//        }
//
//        // se non ho trovato i dati di menu personali prendo la dieta normale.
//        allMenu = manager.getMenuDelGiorno(this.getApplicationContext(), data, Menu.PASTO_NORMALE);
//        if (allMenu != null && allMenu.size() > 0)
//        {
//            return allMenu;
//        }

        return null;
    }

    /**
     * metodo per lo store dei dati.
     *
     * @param data
     * @return
     */
    @Override
    protected void storeData(Object data)
    {
//        List<Menu> lmenu = (List<Menu>) data;
//        Log.w(this.getClass().getName(), "SCRIVO IL MENU su database: " + lmenu.size());
//
//        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
//        Menu mn = null;
//        for (int i = 0; i < lmenu.size(); i++)
//        {
//            mn = lmenu.get(i);
//            Log.w(this.getClass().getName(), "SCRIVO IL MENU su database: " + mn.getDescrizione() + " " + mn.getData());
//            db.createMenu(mn);
//        }
    }
}