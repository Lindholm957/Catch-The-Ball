package com.example.catchtheball;

import android.os.Looper;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestToBackend {


    public static void makePostJsonRequest(String jsonString)
    {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost postRequest = new HttpPost("192.168.1.107:8080");
            postRequest.setHeader("Content-type", "application/json");
            StringEntity entity = new StringEntity(jsonString);

            postRequest.setEntity(entity);

            long startTime = System.currentTimeMillis();
            HttpResponse response = httpClient.execute(postRequest);
            long elapsedTime = System.currentTimeMillis() - startTime;
            //System.out.println("Time taken : "+elapsedTime+"ms");

            InputStream is = response.getEntity().getContent();
            Reader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        builder.append(line);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //System.out.println(builder.toString());
            //System.out.println("****************");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
