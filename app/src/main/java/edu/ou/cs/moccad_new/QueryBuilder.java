package edu.ou.cs.moccad_new;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.net.URLEncoder;

import edu.ou.cs.cacheprototypelibrary.LRUCacheReplacementManager;
import edu.ou.cs.cacheprototypelibrary.SemanticQueryCacheContentManager;
import edu.ou.cs.cacheprototypelibrary.SemanticQueryCacheResolutionManager;
import edu.ou.cs.cacheprototypelibrary.connection.DataAccessProvider;
import edu.ou.cs.cacheprototypelibrary.core.cache.Cache;
import edu.ou.cs.cacheprototypelibrary.core.cache.CacheBuilder;
import edu.ou.cs.cacheprototypelibrary.core.cachemanagers.DataLoader;
import edu.ou.cs.cacheprototypelibrary.core.cachemanagers.SemanticCacheDataLoader;
import edu.ou.cs.cacheprototypelibrary.estimationcache.Estimation;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;
import edu.ou.cs.cacheprototypelibrary.querycache.query.QuerySegment;

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

        setSemanticCacheDataLoader();

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
                        String fromTable = selectField.substring(0, selectField.indexOf('.'));
                            selectField = selectField.substring(selectField.indexOf('.') + 1);
                        String conditionField = (String)dropdown2.getSelectedItem();
                        String condition = queryCondition.getText().toString();

                        String QUERY = "SELECT " + selectField + " FROM " + fromTable + " WHERE " + conditionField;
                        if(isConditionText(condition))
                            QUERY += " = \"" + condition + "\""; //Adds quotation marks for string literals.
                        else
                            QUERY += " = " + condition; //No quotation marks for numeric fields

                        BackgroundTask task = new BackgroundTask();
                        task.getDBInfo = true;

                        try
                        {
                            BackgroundTask runQuery = new BackgroundTask();
                            runQuery.getDBInfo = false;
                            runQuery.query = QUERY;
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
                {
                    url = new URL(server + "getDBInfo.php");
                }
                else
                {
                    if (query != null)
                    {
                        String encodedURL = URLEncoder.encode(query);
                        url = new URL(server + "create_json.php?query=" + encodedURL);
                    }
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
                if(this.getDBInfo)
                {
                    buildList(stringBuilder.toString().trim());
                    return null;
                }
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

        @Override
        protected void onPostExecute(String result) {
            if(result != null) {
                Intent intent = new Intent(getApplicationContext(), QueryResults.class);
                intent.putExtra("json_string", result);
                startActivity(intent);
            }
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

    boolean isConditionText(String condition)
    {
        for(char c : condition.toCharArray())
        {
            if(Character.isLetter(c))
                return true; //Return true if the condition contains a single letter.
        }
        return false;
    }

    private void setSemanticCacheDataLoader()
    {
        Cache<Query, Estimation> mMobileEstimationCache = null;
        Cache<Query, Estimation> mCloudEstimationCache = null;
        Cache<Query, QuerySegment> mQueryCache = null;
        DataLoader mDataLoader = null;
        DataAccessProvider mDataAccessProvider = null;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //TODO: Implement getting these values
        /*int maxQueryCacheSize = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_MAX_QUERY_CACHE_SIZE, "100000000"));
        int maxQueryCacheSegments = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_MAX_QUERY_CACHE_NUMBER_SEGMENT, "0"));
        boolean useReplacement = sharedPref.getBoolean(SettingsActivity.KEY_PREF_USE_REPLACEMENT,true);*/

        int maxQueryCacheSize = 100000000;
        int maxQueryCacheSegments = 0;
        boolean useReplacement = true;

		/*build semantic cache manager*/
        mMobileEstimationCache=null;
        mCloudEstimationCache=null;
        // build managers
        SemanticQueryCacheContentManager contentManager = new SemanticQueryCacheContentManager();
        SemanticQueryCacheResolutionManager resolutionManager = new SemanticQueryCacheResolutionManager();
        LRUCacheReplacementManager lruCacheManager = new LRUCacheReplacementManager();

        // build query cache
        CacheBuilder<Query,QuerySegment> builder = CacheBuilder.<Query,QuerySegment>newBuilder();
        builder.setCacheContentManager(contentManager);
        builder.setCacheResolutionManager(resolutionManager);
        builder.setCacheReplacementManager(lruCacheManager);
        builder.setMaxSize(maxQueryCacheSize);
        if (maxQueryCacheSegments > 0)//not default
        {
            builder.setMaxSegment(maxQueryCacheSegments);
        }
        mQueryCache = builder.build();

        //build cache manager
        mDataLoader = new SemanticCacheDataLoader(this, mDataAccessProvider, mQueryCache, useReplacement);
    }
}
