package com.example.catchtheball;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendToBackend extends AsyncTask<String, String, Void> {
    int score, orange, pink, touches;
    long time;

    HttpURLConnection urlConnection = null;
    BufferedOutputStream bos;
    private static final String POST_URL = "http://192.168.1.107:8080";

    public SendToBackend(int s, long t, int o, int p, int to) {
        this.score = s;
        this.time = t;
        this.orange = o;
        this.pink = p;
        this.touches = to;
        this.doInBackground();
    }

        @Override
        protected Void doInBackground (String...params) {
            try {
                URL url = new URL(POST_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                JSONObject jo = new JSONObject();

                jo.put("score", score);
                jo.put("time", time);
                jo.put("orange", orange);
                jo.put("pink", pink);
                jo.put("touches", touches);

                bos = new BufferedOutputStream(urlConnection.getOutputStream());
                bos.write(jo.toString().getBytes());

                String result = urlConnection.getResponseMessage();
                Log.d("", "server response: " + result); //проверить, что вернет сервер

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
                try {
                    if(bos != null)
                    bos.close();
                } catch (IOException e) {
                    Log.e("READER.CLOSE()", e.toString());
                }
            }
            return null;
        }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
