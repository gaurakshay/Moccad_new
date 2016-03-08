package edu.ou.cs.moccad_new;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

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

public class QueryResults extends AppCompatActivity {

    String JSON_STRING;
    JSONObject jsonObject;
    JSONArray jsonArray;
    QueryAdapter queryAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JSON_STRING = getIntent().getExtras().getString("json_string");

        queryAdapter = new QueryAdapter(this, R.layout.query_list_layout);

        listView = (ListView) findViewById(R.id.queryList);
        listView.setAdapter(queryAdapter);
        try {
            jsonObject = new JSONObject(JSON_STRING);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String logid, patid, docid, date, diag;

            while(count<jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                logid = jo.getString("log_id");
                patid = jo.getString("pat_id");
                docid = jo.getString("doc_id");
                date = jo.getString("log_date");
                diag = jo.getString("diagnosis");

                QueryDetail queryDetail = new QueryDetail(logid, patid, docid, date, diag);

                queryAdapter.add(queryDetail);

                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

/*    class JsonRead extends AsyncTask<Void, Void, String> {

        String string;
        String strUrl = null;

        @Override
        protected void onPreExecute() {
            strUrl = "http://192.168.122.1/create_json.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(strUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream             = httpURLConnection.getInputStream();
                BufferedReader bufferedReader       = new BufferedReader(
                                                            new InputStreamReader(
                                                                inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(string+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            JSON_STRING = result;
        }
    }*/

}