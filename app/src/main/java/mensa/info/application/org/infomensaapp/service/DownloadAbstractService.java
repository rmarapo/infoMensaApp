package mensa.info.application.org.infomensaapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import mensa.info.application.org.infomensaapp.sql.model.Menu;

/**
 * Creato da Giuseppe Grosso in data 13/11/15.
 */
public abstract class DownloadAbstractService extends IntentService implements DownloadInterfaceService
{
    private static final String TAG = "DownloadAbstractService";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadAbstractService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url))
        {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try
            {
                List<Menu> results = (List<Menu>)downloadData(url);

                /* Sending result back to activity */
                if (null != results)
                {
                    bundle.putByteArray("result", object2Bytes(results));
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (Exception e)
            {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    /**
     * Converting objects to byte arrays
     */
    static public byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(o);
        return baos.toByteArray();
    }

    public Object downloadData(String requestUrl) throws IOException, DownloadException
    {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200)
        {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            Object results = parseResult(response);
            return results;
        } else
        {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    protected abstract Object parseResult(String response);

    protected abstract String convertInputStreamToString(InputStream inputStream) throws IOException;


    public class DownloadException extends Exception
    {

        public DownloadException(String message)
        {
            super(message);
        }

        public DownloadException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }
}
