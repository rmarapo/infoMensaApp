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

import mensa.info.application.org.infomensaapp.sql.model.Login;
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
    private static final String TABLE_LOGIN = "login";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // campi della tabella Menu
    private static final String DATA_MENU = "data";
    private static final String CF_MENU = "cf";
    private static final String DESC_MENU = "desc";
    private static final String CONSISTENTE_MENU = "consistente";

    private static final String DATA_LOGIN = "data";
    private static final String CF_LOGIN = "cf";
    private static final String CI_LOGIN = "ci";


    // Creazione tabella
    // Menu statement di creazione
    private static final String CREATE_TABLE_MENU = "CREATE TABLE "
            + TABLE_MENU + "(" + KEY_ID + " INTEGER PRIMARY KEY," + DATA_MENU
            + " DATE," + CF_MENU + " TEXT," + DESC_MENU + " TEXT," + CONSISTENTE_MENU + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE "
            + TABLE_LOGIN + "(" + KEY_ID + " INTEGER PRIMARY KEY," + DATA_LOGIN
            + " DATE," + CF_LOGIN + " TEXT," + CI_LOGIN + " TEXT," + KEY_CREATED_AT
            + " DATETIME" + ")";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void caricaPasti_Dati2015_2016(SQLiteDatabase db)
    {
        // SETTEMBRE
        createMenu(db, "2015-09-21", new String[]{"PASTA AL POMODORO BIO", "FRITTATA BIO", "PISELLINI ALL'OLIO BIO", "FRUTTA FRESCA BIO"});
        createMenu(db, "2015-09-22", new String[]{"PASSATO DI VERDURE CON PASTA", "PASTA PROSCIUTTO COTTO", "INSALATA VERDE CON MAIS", "BANANA"});
        createMenu(db, "2015-09-23", new String[]{"TORTELLINI ROSE'", "PETTO DI POLLO AL LATTE", "CAROTE ALL'OLIO", "FRUTTA FRESCA"});
        createMenu(db, "2015-09-24", new String[]{"RISO ALL'OLIO", "POLPETTE DI MANZO AL POMODORO", "PATATE LESSE", "FRUTTA FRESCA"});
        createMenu(db, "2015-09-25", new String[]{"PASTA AL PESTO", "PARMIGIANO REGGIANO", "INSALATA DI POMODORI", "SUCCO DI FRUTTA"});

        createMenu(db, "2015-09-28", new String[]{"PASTA AL BURRO", "TONNO SOTT'OLIO / HALIBUT GRATINATO", "POMODORI INSALATATRI", "FRUTTA FRESCA"});
        createMenu(db, "2015-09-29", new String[]{"RISO AL POMODORO", "RICOTTA FILIERA CORTA", "INSALATA MISTA", "FRUTTA FRESCA"});
        createMenu(db, "2015-09-30", new String[]{"FUSILLI CON ZUCCHINE E PINOLI", "COSCE DI POLLO ARROSTO", "CAROTE FILANGE'", "BANANA"});
        // OTTOBRE

        // NOVEMBRE
        createMenu(db, "2015-11-23", new String[]{"PASSATO DI VERDURA CON RISO", "MERLUZZO AROMATIZZATO CON MAIONESE", "CAROTE ALL'OLIO", "FRUTTA FRESCA"});
        createMenu(db, "2015-11-24", new String[]{"LASAGNE AL RAGU'", "PARMIGIANO REGGIANO", "INSALATA VERDE", "FRUTTA FRESCA"});
        createMenu(db, "2015-11-25", new String[]{"PASTA AL TONNO", "PROSCIUTTO COTTO", "SPINACI SALTATI", "BANANA"});
        createMenu(db, "2015-11-26", new String[]{"PASTA AL BURRO", "HAMBURGER DI MANZO CON POMODORO", "PATATE LESSE", "FRUTTA FRESCA"});
        createMenu(db, "2015-11-27", new String[]{"CREMA DI PATATE E CAROTE CON RISO", "PETTO DI POLLO ALLA SALVIA", "PISELLI ALL'OLIO", "FRUTTA FRESCA"});
        createMenu(db, "2015-11-28", new String[]{"NON PREVISTO"});
        createMenu(db, "2015-11-29", new String[]{"NON PREVISTO"});
        createMenu(db, "2015-11-30", new String[]{"PASTA ALL'AMATRICIANA", "PECORINO DOP", "FAGIOLINI ALL'OLIO", "FRUTTA FRESCA"});

        // DICEMBRE
        createMenu(db, "2015-12-01", new String[]{"PASTINA IN BRODO/PASTA AL POMODORO**", "ROLLATA DI TACCHINO CON MORTADELLA", "BIETOLA SALTATA", "BANANA"});
        createMenu(db, "2015-12-02", new String[]{"RISOTTO DI ZUCCA", "BOCCONCINI DI POLLO FRITTO", "INSALATA VERDE CON MAIS", "FRUTTA FRESCA"});
        createMenu(db, "2015-12-03", new String[]{"TAGLIATELLE AL RAGU'", "POLPETTE VEGETALI", "CAROTE ALL'OLIO", "FRUTTA FRESCA"});
        createMenu(db, "2015-12-04", new String[]{"PASS. DI VERDURA CON ORZO'", "PROSCIUTTO CRUDO/PROSCIUTTO COTTO*", "PATATE ARROSTO", "TORTINA"});

        createMenu(db, "2015-12-05", new String[]{"NON PREVISTO"});
        createMenu(db, "2015-12-06", new String[]{"NON PREVISTO"});
        createMenu(db, "2015-12-07", new String[]{"NON PREVISTO"});
        createMenu(db, "2015-12-08", new String[]{"NON PREVISTO"});

        createMenu(db, "2015-12-09", new String[]{"CAPPELLINI IN BRODO", "FRITTATA", "TRIS DI VERDURE AL VAPORE", "BANANA"});
        createMenu(db, "2015-12-10", new String[]{"PASSATO DI VERDURA CON CROSTINI/RISO**", "COSCE DI POLLO ARROSTO", "FINOCCHI CRUDITE'", "FRUTTA FRESCA"});
        createMenu(db, "2015-12-11", new String[]{"PASTA AL RAGU' DI MANZO", "STRACCHINO", "INSALATA MISTA", "BUDINO"});
        createMenu(db, "2015-12-12", new String[]{"NON PREVISTO"});
        createMenu(db, "2015-12-13", new String[]{"NON PREVISTO"});
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        // creazione delle tabelle
        db.execSQL(CREATE_TABLE_MENU);
        caricaPasti_Dati2015_2016(db);
        db.execSQL(CREATE_TABLE_LOGIN);

        // inserisci le altre tabelle qui sotto...
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
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
    public long createMenu(SQLiteDatabase db, String data, String[] menu)
    {
        for (int i = 0; i < menu.length; i++)
        {
            createMenu(db, new Menu(data, Menu.PASTO_NORMALE, menu[i]));
        }

        return menu.length;
    }

    /*
     * Creazione della tabella Menu
     */
    public long createMenu(Menu menu)
    {
        return createMenu(null, menu);
    }
    /*
     * Creazione della tabella Menu
     */
    public long createMenu(SQLiteDatabase db, Menu menu)
    {
        if (db == null)
            db = this.getWritableDatabase();

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


    // ------------------------ "login" metodi della tabella ----------------//


    /*
     * Creazione della tabella Menu
     */
    public long createLogin(String cf, String ci)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // insert row
        long login_id = db.insert(TABLE_LOGIN, null, getContentFromLogin(cf, ci));

        return login_id;
    }

    private ContentValues getContentFromLogin(String cf, String ci)
    {
        ContentValues values = new ContentValues();
        values.put(DATA_LOGIN, getDate());
        values.put(CF_LOGIN, cf);
        values.put(CI_LOGIN, ci);
        values.put(KEY_CREATED_AT, getDateTime());

        return values;
    }

    /**
     * ottengo l'elemento di Login
     */
    public Login getLogin(long login_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = getLoginQuery(login_id, null, null);

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return getLoginFromCursor(c);
    }

    private Login getLoginFromCursor(Cursor c)
    {
        Login mn = new Login();
        mn.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        mn.setData(c.getString(c.getColumnIndex(DATA_LOGIN)));
        mn.setCf((c.getString(c.getColumnIndex(CF_LOGIN))));
        mn.setCi((c.getString(c.getColumnIndex(CI_LOGIN))));
        mn.setCreatedat(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return mn;
    }

    private String getLoginQuery(Long login_id, String cf, String ci)
    {
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN + " l";
        if (cf != null || ci != null || login_id != null)
            selectQuery += " WHERE 1=1";
        if (cf != null)
            selectQuery += " AND l." + CF_LOGIN + " "
                    + " = '" + cf + "'";
        if (ci != null)
            selectQuery += " AND l." + CI_LOGIN + " "
                    + " = '" + ci + "'";
        if (login_id != null)
            selectQuery += " AND l." + KEY_ID + " "
                    + " = " + login_id;

        return selectQuery;
    }

    /**
     * ottengo tutto l'oggetto login in base all'unica riga presente.
     */
    public List<Login> getLoginDefault()
    {
        return getLoginByCFCI(null, null);
    }

    /**
     * ottengo tutto l'oggetto login
     */
    public List<Login> getLoginByCFCI(String cf, String ci)
    {
        List<Login> logins = new ArrayList<Login>();
        String selectQuery = getLoginQuery(null, cf, ci);

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst())
        {
            do
            {
                // aggiungo il menu alla lista.
                logins.add(getLoginFromCursor(c));
            } while (c.moveToNext());
        }

        return logins;
    }

    /**
     * ritorna true se esiste un record di login
     */
    public boolean isLogin()
    {
        return getLoginCount() > 0;
    }

    /**
     * numero di login
     */
    public int getLoginCount()
    {
        return getLoginCountByCFCI(null, null);
    }

    /**
     * numero di login
     */
    public int getLoginCountByCFCI(String cf, String ci)
    {
        String countQuery = getLoginQuery(null, cf, ci);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Deleting a Login
     */
    public int updateLogin(Login login)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CF_LOGIN, login.getCf());
        values.put(CI_LOGIN, login.getCi());
        values.put(DATA_LOGIN, getDateTime());

        // updating row
        return db.update(TABLE_LOGIN, values, KEY_ID + " = ?",
                new String[]{String.valueOf(login.getId())});
    }

    /**
     * Deleting a Login
     */
    public void deleteLogin(long login_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, KEY_ID + " = ?",
                new String[]{String.valueOf(login_id)});
    }

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
