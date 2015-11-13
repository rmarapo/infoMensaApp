package mensa.info.application.org.infomensaapp;

import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import mensa.info.application.org.infomensaapp.service.MenuDelGiornoService;
import mensa.info.application.org.infomensaapp.service.DownloadAbstractService;
import mensa.info.application.org.infomensaapp.service.DownloadResultReceiver;

public class MensaMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DownloadResultReceiver.Receiver
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                emailIntent();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
//          eseguo la chiamata al server.
            makeToast("menu del giorno ", false);
//            final TextView mTextView = (TextView) findViewById(R.id.text);
//            TextView mcorpoText = (TextView) findViewById(R.id.corpo_text);
//            mcorpoText.setText("Menu Del Griono");
//            // Instantiate the RequestQueue.
//                            RequestQueue queue = Volley.newRequestQueue(this);
//                        String url ="http://www.google.com";
//
//            // Request a string response from the provided URL.
//                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        // Display the first 500 characters of the response string.
//                                        makeToast("Response is: " + response.substring(0, 500));
//                                    }
//                                }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                makeToast("That didn't work!");
////                                mTextView.setText("That didn't work!");
//                            }
//                        });
//            // Add the request to the RequestQueue.
//                        queue.add(stringRequest);


//            MenuDelGiornoBean bean = new MenuDelGiornoBean("prova", 0d, 0d);
//
//            startService(createCallingIntent(bean));

//            GetConnectionTask task = new GetConnectionTask();
//            try
//            {
//                task.execute(new URL("http://www.google.it"));
//            } catch (MalformedURLException e)
//            {
//                e.printStackTrace();
//            }
            startIntent();

        } else if (id == R.id.nav_presenze)
        {
            makeToast("presenze a mensa", false);

        } else if (id == R.id.nav_conto)
        {
            makeToast("estratto conto", false);
        } else if (id == R.id.nav_settings)
        {
            makeToast("settings", false);

        } else if (id == R.id.nav_email)
        {
            makeToast("email", false);
            emailIntent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void startIntent()
    {
        /* Starting Download Service */
        DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, MenuDelGiornoService.class);

        /* Send optional extras to Download IntentService */
        intent.putExtra("url", "http://www.google.it");
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {
        switch (resultCode)
        {
            case DownloadAbstractService.STATUS_RUNNING:
                setProgressBarIndeterminateVisibility(true);
                break;
            case DownloadAbstractService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                setProgressBarIndeterminateVisibility(false);
                String[] results = resultData.getStringArray("result");

                final ListView mListView = (ListView) findViewById(R.id.pastoList);
                final TextView mTextView = (TextView) findViewById(R.id.textList);
                mTextView.setVisibility(TextView.VISIBLE);

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, results);
                mListView.setAdapter(adapter);

                break;
            case DownloadAbstractService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
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

        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
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

    /**
     * metodi di servizio
     */

    public void emailIntent()
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