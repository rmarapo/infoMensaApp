package mensa.info.application.org.infomensaapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import mensa.info.application.org.infomensaapp.R;

public class CalendarViewActivity extends AppCompatActivity
{

    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setEnabled(false);









        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view,
                                            int year, int month, int dayOfMonth)
            {
                calendar.setDate(new GregorianCalendar(year, month, dayOfMonth).getTimeInMillis());
                Toast.makeText(getApplicationContext(),
                        dayOfMonth + "/" + (month+1) + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
    }

}
