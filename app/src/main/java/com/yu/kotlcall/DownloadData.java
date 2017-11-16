/*package com.yu.kotlcall;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
*/
/**
 * Created by Yu on 2016/12/1.
 */
/*
public class DownloadData extends AsyncTask<String , Void, String > {

    HttpURLConnection httpURLConnection =null;
    URL url;


    String resultString="";


    @Override
    protected String doInBackground(String[] urls) {


        try {
            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            int data = isr.read();
            while(data != -1){
                char ch = (char) data;
                resultString += ch;
                data = isr.read();

            }
            return resultString;



        } catch (MalformedUR
                LException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }*/