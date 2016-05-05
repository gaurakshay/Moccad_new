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
import java.util.ArrayList;

import edu.ou.cs.cacheprototypelibrary.core.cache.Cache;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.InvalidPredicateException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.TrivialPredicateException;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;
import edu.ou.cs.cacheprototypelibrary.querycache.query.QuerySegment;
import edu.ou.cs.cacheprototypelibrary.querycache.query.XopYPredicate;

public class QueryResults extends AppCompatActivity {

    String JSON_STRING,
            queryRelation,
            queryPredicate;
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
        queryRelation = getIntent().getExtras().getString("queryRelation");
        queryPredicate = getIntent().getExtras().getString("queryPredicate");

        queryAdapter = new QueryAdapter(this, R.layout.query_list_layout);

        listView = (ListView) findViewById(R.id.queryList);
        listView.setAdapter(queryAdapter);
        try {
            //JSON_STRING = sanitizeString(JSON_STRING);
            jsonObject = new JSONObject(JSON_STRING);
            jsonArray = jsonObject.getJSONArray("server_response");

            int count = 0;
            String field;

            MOCCAD mApplication = (MOCCAD)getApplicationContext();
            if(((MOCCAD) this.getApplication()).getQueryCache() == null) {
                System.out.println("Creating cache");
                ((MOCCAD) this.getApplication()).setCacheManager();
                //XXX: I'm not sure what happens here if the user has selected "No Cache" from the settings menu...
            }
            final Cache<Query, QuerySegment> cache = ((MOCCAD) this.getApplication()).getQueryCache();
            Query query = new Query(this.queryRelation);
            String[] predicate = queryPredicate.split(" ");
            try{ query.addPredicate(new XopYPredicate(predicate[0], predicate[1], predicate[2])); }
            catch(InvalidPredicateException | TrivialPredicateException e){e.printStackTrace();}

            System.out.println("JSON: " + JSON_STRING);
            System.out.println("Before loop.");
            ArrayList<String> tuple = new ArrayList<String>();
            while(count<jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                field = jo.getString("Field");

                QueryDetail queryDetail = new QueryDetail(field);
                queryAdapter.add(queryDetail);

                tuple.add(field);
                cache.getCacheContentManager().get(query).addTuple(tuple);

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

    public String sanitizeString(String str)
    {
        while(str.charAt(0) != '{')
        {
            str = str.substring(1); //Cut off the first character while it is not '{'
        }

        int lastIndex = str.length() - 1;
        while(str.charAt(lastIndex) != '}')
        {
            str = str.substring(lastIndex - 1);
            lastIndex = str.length() - 1;
        }
        return str;
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
