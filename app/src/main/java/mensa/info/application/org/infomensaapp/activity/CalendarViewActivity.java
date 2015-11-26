package mensa.info.application.org.infomensaapp.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.decorators.EventDecorator;
import com.prolificinteractive.materialcalendarview.decorators.TextDecorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import mensa.info.application.org.infomensaapp.R;

public class CalendarViewActivity extends AppCompatActivity
{


    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    int currentTileSize;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        ButterKnife.bind(this);

        currentTileSize = MaterialCalendarView.DEFAULT_TILE_SIZE_DP;


        int color = Color.HSVToColor(new float[]{0f, 0f, 0.9f});
        widget.setSelectionColor(color);
        widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setDateTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        // richiamo il server per il recupero delle presenze.
        new PresenzeMensa().executeOnExecutor(Executors.newSingleThreadExecutor());

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
}
