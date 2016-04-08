package edu.ou.cs.moccad_new;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button executeQuery;
    EditText queryCondition;

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
            BackgroundTask task = new BackgroundTask();
            task.getDBInfo = true;
            String str_result = task.execute().get(); //Call to doInBackground
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        final Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[fields.size()];
        fields.toArray(items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        final Spinner dropdown2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);

        queryCondition   = (EditText)findViewById(R.id.queryCondition);
        executeQuery = (Button)findViewById(R.id.executeQuery);
        executeQuery.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String selectField = (String)dropdown.getSelectedItem();
                        String conditionField = (String)dropdown2.getSelectedItem();
                        String condition = queryCondition.getText().toString();

                        System.out.println(selectField + " " + conditionField + " " + condition);

                        BackgroundTask task = new BackgroundTask();
                        task.getDBInfo = true;

                        try
                        {
                            BackgroundTask runQuery = new BackgroundTask();
                            runQuery.getDBInfo = false;
                            runQuery.query = "SELECT " + selectField + " WHERE " + conditionField + " = \"" + condition + "\"";
                            String str_result = runQuery.execute().get(); //Call to doInBackground
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        catch (ExecutionException e)
                        {
                            e.printStackTrace();
                        }


                    }
                });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        boolean getDBInfo = false;
        String query = null;

        @Override
        protected String doInBackground(Void... params) {
            String tempString = null;
            try
            {
                String server = "http://" + IP + "/";
                URL url;
                if(this.getDBInfo)
                    url = new URL(server + "getDBInfo.php");
                else
                {
                    if (query != null)
                        url = new URL(server + "create_json.php?query=" + query);
                    else
                        return "Error: Query is null.";
                }
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

                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
