package mensa.info.application.org.infomensaapp.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mensa.info.application.org.infomensaapp.R;
import mensa.info.application.org.infomensaapp.service.MenuDelGiornoService;
import mensa.info.application.org.infomensaapp.service.interfaces.DownloadAbstractService;
import mensa.info.application.org.infomensaapp.service.interfaces.DownloadResultReceiver;
import mensa.info.application.org.infomensaapp.sql.helper.DatabaseHelper;

public class MensaMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DownloadResultReceiver.Receiver
{

    private static final String URL_SERVER = "http://10.2.2.10:8080/help/"; //Giuseppe.
    private DrawerLayout drawer = null;

    private Date menu_date = Calendar.getInstance().getTime();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfHuman = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mensa_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!isLoginActivated())
            startActivity(new Intent(this, LoginActivity.class));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startEmailIntent();
            }
        });

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private boolean isLoginActivated()
    {
        // dalle banca dati vado a prendere i dati se esistono
        // non propongo la login altrimenti si.
        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());

        return db.isLogin();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mensa_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menugiorno)
        {
//            startMenuManager();/**/
            startActivity(new Intent(this, MenudelgiornoActivity.class));
        } else if (id == R.id.nav_login)
        {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_presenze)
        {
            startActivity(new Intent(this, CalendarViewActivity.class));
        } else if (id == R.id.nav_conto)
        {
            startNotifiche();
//            startActivity(new Intent(this, ContoActivity.class));
        } else if (id == R.id.nav_settings)
        {
//            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_email)
        {
            startEmailIntent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startMenuManager()
    {
        final ListView mListView = (ListView) findViewById(R.id.pastoList);
        final TextView mTextView = (TextView) findViewById(R.id.textList);

        mTextView.setText(R.string.header_list);
        mTextView.append(" " + sdfHuman.format(menu_date));
        mTextView.setVisibility(TextView.VISIBLE);

        startIntent();
    }

    public void startIntent()
    {
        /* Starting Download Service */
        DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, MenuDelGiornoService.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("url", URL_SERVER + "mensa?step=getMenuGiorno&data=" + sdf.format(menu_date) + "&cf=GRSGPP76D12G999F");
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {
        final TextView mTextView = (TextView) findViewById(R.id.textList);

        switch (resultCode)
        {
            case DownloadAbstractService.STATUS_RUNNING:
//                setProgressBarIndeterminateVisibility(true);
                mTextView.setText("sto elaborando...");
                mTextView.setVisibility(TextView.VISIBLE);
                break;
            case DownloadAbstractService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                setProgressBarIndeterminateVisibility(false);
                mTextView.setText("sto recuperando i dati...");
                Object objallMenu = null;
                try
                {
                    objallMenu = bytes2Object((byte[]) resultData.get("result"));
                } catch (IOException e)
                {
                    e.printStackTrace();
                } catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                List<mensa.info.application.org.infomensaapp.sql.model.Menu> allMenu = (List<mensa.info.application.org.infomensaapp.sql.model.Menu>) objallMenu;


                // TODO da capire come fare per fare un adapter.
                final ListView mListView = (ListView) findViewById(R.id.pastoList);
                mTextView.setText(R.string.header_list);
                mTextView.append(" " + sdfHuman.format(menu_date));
                mTextView.setVisibility(TextView.VISIBLE);

                String[] results = new String[allMenu.size()];

                for (int i = 0; i < allMenu.size(); i++)
                    results[i] = allMenu.get(i).getDescrizione();


                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);
                mListView.setAdapter(adapter);


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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }

        if (this.drawer.isDrawerOpen(GravityCompat.START))
        {
            this.drawer.closeDrawer(GravityCompat.START);
        } else
        {
            this.doubleBackToExitPressedOnce = true;
            makeToast("Premi ancora Esc per uscire...");
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }


    }

    /**
     * metodi di servizio
     */


    // notifiche
    public void startNotifiche()
    {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(MensaMainActivity.this, DownloadResultReceiver.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MensaMainActivity.this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.typeb_calendar_today)
                .setLargeIcon(BitmapFactory.decodeResource(res,R.drawable.typeb_calendar_today))
                .setTicker(res.getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setSound(soundUri)
                .setContentTitle("Aggiornamento xxxx")
                .setContentText("ci sono nuovi aggiornamenti per te!");

        Notification n = builder.build();

        nm.notify(11223, n);

    }

    public void startEmailIntent()
    {
        MailTo mt = MailTo.parse("mailto:giuseppe.ing.grosso@gmail.com");
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mt.getTo()});
        sendIntent.putExtra(Intent.EXTRA_TEXT, "");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "oggetto: ");
        sendIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(sendIntent, "Invia un messaggio:"));
    }

    public void makeToast(String text, boolean b)
    {
        Toast.makeText(getApplicationContext(), text, (b ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)).show();
    }

    public void makeToast(String text)
    {
        makeToast(text, true);
    }
}