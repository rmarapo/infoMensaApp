package mensa.info.application.org.infomensaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import mensa.info.application.org.infomensaapp.sql.model.Login;
import mensa.info.application.org.infomensaapp.sql.model.Menu;

public class MenudelgiornoActivity extends AppCompatActivity implements DownloadResultReceiver.Receiver
{

    private DownloadResultReceiver mReceiver;
    private static final String URL_SERVER = "http://10.2.2.10:8080/help/"; //Giuseppe.

    private Calendar menu_date = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfDbase = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfHuman = new SimpleDateFormat("dd/MM/yyyy");

    private DatabaseHelper db = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menudelgiorno);

        this.mReceiver = new DownloadResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);

        // se ci sono prendo i dati dalla banca dati
        this.db = new DatabaseHelper(this.getApplicationContext());

        startMenuManager();

        // aggancio gli handler.
        TextView avanti = (TextView) findViewById(R.id.next);
        TextView indietro = (TextView) findViewById(R.id.previous);
        avanti.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getMenu_date().add(Calendar.DATE, 1);
                startMenuManager();
            }
        });
        indietro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getMenu_date().add(Calendar.DATE, -1);
                startMenuManager();
            }
        });

    }


    private void startMenuManager()
    {
        final ListView mListView = (ListView) findViewById(R.id.pastoList);
        final TextView mTextView = (TextView) findViewById(R.id.textList);

        mTextView.append(sdfHuman.format(menu_date.getTime()));
        mTextView.setVisibility(TextView.VISIBLE);

        startIntent(this.getApplicationContext());
    }

    public void startIntent(Context context)
    {
        // recupero il codice fiscale dalla login.
        String cf = Menu.PASTO_NORMALE;
        List<Login> lLogin = db.getLoginDefault();
        if (lLogin!= null && lLogin.size()>0)
            cf = lLogin.get(0).getCf();
        /* Starting Download Service */
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, MenuDelGiornoService.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("data", sdfDbase.format(menu_date.getTime()));
        intent.putExtra("cf", cf);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);
        intent.putExtra("url", URL_SERVER + "mensa?step=getMenuGiorno&data=" + sdf.format(menu_date.getTime()) + "&cf=" + intent.getStringExtra("cf"));

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
                List<Menu> allMenu = (List<mensa.info.application.org.infomensaapp.sql.model.Menu>) objallMenu;


                // TODO da capire come fare per fare un adapter.
                final ListView mListView = (ListView) findViewById(R.id.pastoList);
                mTextView.setText(sdfHuman.format(menu_date.getTime()));
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

    public Calendar getMenu_date()
    {
        return menu_date;
    }

    public void setMenu_date(Calendar menu_date)
    {
        this.menu_date = menu_date;
    }


}
