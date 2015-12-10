package mensa.info.application.org.infomensaapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import mensa.info.application.org.infomensaapp.sql.helper.DatabaseHelper;
import mensa.info.application.org.infomensaapp.sql.model.Login;

/**
 * Creato da Giuseppe Grosso in data 10/12/15.
 */

public class AbstractActivity extends AppCompatActivity
{
    private Login lg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.lg = getLoginDefault();


    }

    private Login getLoginDefault()
    {
        // dalle banca dati vado a prendere i dati se esistono
        // non propongo la login altrimenti si.
        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());

        return db.getLoginDefault();

    }

    public Login getLogin()
    {
        return this.lg;
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
