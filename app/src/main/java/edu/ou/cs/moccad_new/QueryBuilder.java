package edu.ou.cs.moccad_new;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ryan on 3/30/2016.
 */
public class QueryBuilder extends Activity {
    String IP;
    ArrayList<String> fields;

    protected void onCreate(Bundle save)
    {
        super.onCreate(save);
        setContentView(R.layout.content_query_builder);
        System.out.println("In QueryBuilder constructor");

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            this.IP = extras.getString("IPAddress");
        }
        else
        {
            return;
            //Probably need an error message in here.
        }

        try
        {
            String str_result = new BackgroundTask().execute().get(); //Call to doInBackground
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[fields.size()];
        fields.toArray(items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        Spinner dropdown2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String tempString = null;
            try
            {
                System.out.println("In doInBackground");
                System.out.println("IP: " + IP);
                //URL url = new URL(strUrl);
                URL url = new URL("http://" + IP + "/getDBInfo.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream             = httpURLConnection.getInputStream();
                BufferedReader bufferedReader       = new BufferedReader(
                        new InputStreamReader(
                                inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((tempString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(tempString+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                buildList(stringBuilder.toString().trim());
                return stringBuilder.toString().trim();
            }
            catch (MalformedURLException e)
            {
                System.out.println("Threw MalformedURLException");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                System.out.println("Threw IOException");
                e.printStackTrace();
            }
            return null;
        }

        protected void buildList(String result)
        {
            if(result == null)
            {
                return;
                //Probably need an error message here.
            }

            try {
                System.out.println("JSON String: " + result);
                JSONObject jsonObject = new JSONObject(result);
                System.out.println("After new JSONObject.");
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                String table, column;
                fields = new ArrayList<String>();

                while(count<jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    table = jo.getString("TABLE_NAME");
                    column = jo.getString("COLUMN_NAME");

                    fields.add(table + "." + column);

                    System.out.println("table: " + table);
                    System.out.println("column: " + column);

                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
