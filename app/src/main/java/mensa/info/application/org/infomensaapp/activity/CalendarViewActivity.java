package mensa.info.application.org.infomensaapp.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.decorators.EventDecorator;
import com.prolificinteractive.materialcalendarview.decorators.TextDecorator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import mensa.info.application.org.infomensaapp.R;
import mensa.info.application.org.infomensaapp.service.MenuDelGiornoService;
import mensa.info.application.org.infomensaapp.service.PresenzeMensiliService;
import mensa.info.application.org.infomensaapp.service.interfaces.DownloadAbstractService;
import mensa.info.application.org.infomensaapp.service.interfaces.DownloadResultReceiver;
import mensa.info.application.org.infomensaapp.sql.helper.DatabaseHelper;
import mensa.info.application.org.infomensaapp.sql.model.Login;
import mensa.info.application.org.infomensaapp.sql.model.Menu;

public class CalendarViewActivity extends AppCompatActivity implements DownloadResultReceiver.Receiver
{

    private static final String URL_SERVER = "http://10.2.2.10:8080/help/"; //Giuseppe.

    private Date menu_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfHuman = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdfDbase = new SimpleDateFormat("yyyy-MM-dd");

    private DownloadResultReceiver mReceiver = null;

    private DatabaseHelper db = null;


    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    int currentTileSize;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        // se ci sono prendo i dati dalla banca dati
        this.db = new DatabaseHelper(this.getApplicationContext());

        ButterKnife.bind(this);

        /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        currentTileSize = MaterialCalendarView.DEFAULT_TILE_SIZE_DP;

        int color = Color.parseColor("#CE93D8");
        widget.setSelectionColor(color);
        widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setDateTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        // richiamo il server per il recupero delle presenze.
//        new PresenzeMensa().executeOnExecutor(Executors.newSingleThreadExecutor());

        startIntent(this.getApplicationContext());

    }


    /**
     * chiamata al server per il recupero delle presenze.
     */
    private class PresenzeMensa extends AsyncTask<Void, Void, List<Calendar>>
    {

        @Override
        protected List<Calendar> doInBackground(@NonNull Void... voids)
        {
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Calendar calendar = null;
            List<Calendar> dates = new ArrayList<>();

            for (int i = 0; i < 30; i++)
            {
                calendar = new GregorianCalendar(2015, 10, i);
                dates.add(calendar);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<Calendar> calendarDays)
        {
            super.onPostExecute(calendarDays);

            if (isFinishing())
            {
                return;
            }

            for (int i = 0; i < calendarDays.size(); i++)
            {
                widget.setDateSelected(calendarDays.get(i), true);
            }
        }
    }

    public void startIntent(Context context)
    {
        // recupero il codice fiscale dalla login.
        String cf = Menu.PASTO_NORMALE;
        List<Login> lLogin = db.getLoginDefault();
        if (lLogin != null && lLogin.size() > 0)
            cf = lLogin.get(0).getCf();
        /* Starting Download Service */
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, PresenzeMensiliService.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("data", sdfDbase.format(Calendar.getInstance().getTime()));
        intent.putExtra("cf", cf);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);
        intent.putExtra("url", URL_SERVER + "mensa?step=getPresenzeMensili&data=" + sdf.format(Calendar.getInstance().getTime()) + "&cf=" + intent.getStringExtra("cf"));

        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {
        final TextView mTextView = (TextView) findViewById(R.id.textList);

        switch (resultCode)
        {
            case DownloadAbstractService.STATUS_RUNNING:
                setProgressBarIndeterminateVisibility(true);
                break;
            case DownloadAbstractService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                setProgressBarIndeterminateVisibility(false);

                Calendar calendar = null;
                List<Calendar> dates = new ArrayList<>();

                for (int i = 0; i < 30; i++)
                {
                    calendar = new GregorianCalendar(2015, 10, i);
                    dates.add(calendar);
                }
                for (int i = 0; i < dates.size(); i++)
                {
                    widget.setDateSelected(dates.get(i), true);
                }

                break;
            case DownloadAbstractService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Converting byte arrays to objects
     */
    static public Object bytes2Object(byte raw[])
            throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        return o;
    }
}
