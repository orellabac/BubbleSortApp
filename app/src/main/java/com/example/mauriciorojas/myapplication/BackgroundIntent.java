package com.example.mauriciorojas.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class BackgroundIntent extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "BackgroundIntent";

    public BackgroundIntent() {
        super(BackgroundIntent.class.getName());
    }
    private void bubbleSort() {
        int min;
        for (int i = 0; i < data.length - 1; i++){
            min = i;
            for (int  j = i + 1; j < data.length; j++){
                if (data[j] < data[min]){
                    min = j;
                }
            }
            if (i != min){
                int tmp = data[i];
                data[i] = data[min];
                data[min] = tmp;
            }
        }
    }

    static final int MAX_VALUE = 100000;
    int data[] = new int[MAX_VALUE];


    public void generarNuevoArreglo() {
        Random random = new Random();

        for (int i = 0; i < data.length; i++){
            data[i] = random.nextInt(MAX_VALUE);
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();

        /* Service Started */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        try {

            long start = System.nanoTime();
            generarNuevoArreglo();
            bubbleSort();
            long end = System.nanoTime();

            long elapsedTime = end - start;
            String res = "trascurrieron: " + elapsedTime + "nano seconds\n" +
            "lo cual es " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime) + " segundos";

            bundle.putString("exectime",res);
        }
        catch (Error err)
        {
            bundle.putString(Intent.EXTRA_TEXT, err.getMessage());
            receiver.send(STATUS_ERROR, bundle);
        }

        /* Status Finished */
        receiver.send(STATUS_FINISHED, bundle);
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }
}
