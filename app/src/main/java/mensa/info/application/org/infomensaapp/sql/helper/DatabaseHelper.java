package mensa.info.application.org.infomensaapp.sql.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mensa.info.application.org.infomensaapp.sql.model.Menu;

/**
 * Creato da Giuseppe Grosso in data 16/11/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mensaApp";

    // Table Names
    private static final String TABLE_MENU = "menu";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // campi della tabella Menu
    private static final String DATA_MENU = "data";
    private static final String CF_MENU = "cf";
    private static final String DESC_MENU = "desc";
    private static final String CONSISTENTE_MENU = "consistente";


    // Creazione tabella
    // Menu statement di creazione
    private static final String CREATE_TABLE_MENU = "CREATE TABLE "
            + TABLE_MENU + "(" + KEY_ID + " INTEGER PRIMARY KEY," + DATA_MENU
            + " DATE," + CF_MENU + " TEXT," + DESC_MENU + " TEXT," + CONSISTENTE_MENU + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        // creazione delle tabelle
        db.execSQL(CREATE_TABLE_MENU);
        // inserisci le altre tabelle qui sotto...
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        // inserisci le altre tabelle qui sotto...

        // creazione del database ...
        onCreate(db);
    }

    /************************************************************************************************
     * Metodi per la creazione e il funzionamento
     ************************************************************************************************/

    // ------------------------ "menu" metodi della tabella ----------------//


    /*
     * Creazione della tabella Menu
     */
    public long createMenu(Menu menu)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // insert row
        long menu_id = db.insert(TABLE_MENU, null, getContentFromMenu(menu));

        return menu_id;
    }

    private ContentValues getContentFromMenu(Menu menu)
    {
        ContentValues values = new ContentValues();
        values.put(DATA_MENU, menu.getData());
        values.put(CF_MENU, menu.getCf());
        values.put(DESC_MENU, menu.getDescrizione());
        values.put(CONSISTENTE_MENU, menu.isConsistente());
        values.put(KEY_CREATED_AT, getDateTime());

        return values;
    }

    /**
     * ottengo l'elemento di menu
     */
    public Menu getMenu(long menu_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MENU + " WHERE "
                + KEY_ID + " = " + menu_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return getMenuFromCursor(c);
    }

    private Menu getMenuFromCursor(Cursor c)
    {
        Menu mn = new Menu();
        mn.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        mn.setData(c.getString(c.getColumnIndex(DATA_MENU)));
        mn.setCf((c.getString(c.getColumnIndex(CF_MENU))));
        mn.setDescrizione((c.getString(c.getColumnIndex(DESC_MENU))));
        mn.setConsistente((c.getInt(c.getColumnIndex(CONSISTENTE_MENU))));
        mn.setCreatedat(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return mn;
    }

    /**
     * ottengo tutto il menu normale del giorno voluto.
     */
    public List<Menu> getPastoNormaleByDate(String data)
    {
        return getPastoPersonaleByDate(data, Menu.PASTO_NORMALE);
    }

    /**
     * ottengo tutto il menu normale del giorno voluto.
     */
    public List<Menu> getPastoPersonaleByDate(String data, String cf)
    {
        List<Menu> menus = new ArrayList<Menu>();
        String selectQuery = "SELECT  * FROM " + TABLE_MENU + " mn WHERE"
                + " mn." + DATA_MENU + " = '" + data + "' AND mn." + CF_MENU
                + " = '" + cf + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst())
        {
            do
            {
                // aggiungo il menu alla lista.
                menus.add(getMenuFromCursor(c));
            } while (c.moveToNext());
        }

        return menus;
    }

    /**
     * numero di menu
     */
    public int getMenuCount()
    {
        return getMenuPersonaleCountByDate(null, null);
    }

    /**
     * numero di menu
     */
    public int getMenuPastoNormaleCountByDate(String data)
    {
        return getMenuPersonaleCountByDate(data, Menu.PASTO_NORMALE);
    }

    /**
     * numero di menu
     */
    public int getMenuPersonaleCountByDate(String data, String cf)
    {
        String countQuery = "SELECT  * FROM " + TABLE_MENU + " mn";
        if (data != null || cf != null)
        {
            countQuery += " WHERE";

            if (data != null)
                countQuery += " mn." + DATA_MENU + " = '" + data;
            if (cf != null)
                countQuery += " AND mn." + CF_MENU + " = '" + cf;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a todo
     */
    public int updateMenu(Menu menu)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // updating row
        return db.update(TABLE_MENU, getContentFromMenu(menu), KEY_ID + " = ?",
                new String[]{String.valueOf(menu.getId())});
    }

    /**
     * Deleting a Menu
     */
    public void deleteMenu(long menu_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU, KEY_ID + " = ?",
                new String[]{String.valueOf(menu_id)});
    }

    // cancellazione per cf e data.

    /************************************************************************************************
     * Metodi di comodo.
     ************************************************************************************************/

    private String getDate()
    {
        return getDate(null);
    }

    private String getDate(Calendar date)
    {
        return getDateTime(date, "yyyy-MM-dd");
    }

    /**
     * ritorno la stringa nel formato date time.
     */
    private String getDateTime()
    {
        return getDateTime(null, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * ritorno la stringa nel formato date time.
     */
    private String getDateTime(Calendar mydate, String pattern)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        if (mydate == null)
            mydate = Calendar.getInstance();
        return dateFormat.format(mydate.getTime());
    }
}
