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
import android.util.Log;

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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import edu.ou.cs.cacheprototypelibrary.querycache.exception.InvalidPredicateException;
import edu.ou.cs.cacheprototypelibrary.querycache.exception.TrivialPredicateException;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Predicate;
import edu.ou.cs.cacheprototypelibrary.querycache.query.Query;
import edu.ou.cs.cacheprototypelibrary.querycache.query.QuerySegment;
import edu.ou.cs.cacheprototypelibrary.querycache.query.XopYPredicate;

/**
 * Created by Ryan on 3/30/2016.
 */
public class QueryBuilder extends Activity {
    String IP;
    ArrayList<String> fields;
    Button executeQuery;
    EditText queryCondition;
    Query q;

    protected void onCreate(Bundle save)
    {
        super.onCreate(save);
        setContentView(R.layout.content_query_builder);
        System.out.println("In QueryBuilder constructor");

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            this.IP = extras.getString("IPAddress");
            Log.i("QueryBuilder", "Got IPAddress " + this.IP + " as extra data.");
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
            //String str_result = task.execute().get(); //Call to doInBackground
            task.execute().get();
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

        MOCCAD mApplication = (MOCCAD)getApplicationContext();
        if(((MOCCAD) this.getApplication()).getQueryCache() == null) {
            System.out.println("Creating cache");
            ((MOCCAD) this.getApplication()).setCacheManager();
            //XXX: I'm not sure what happens here if the user has selected "No Cache" from the settings menu...
        }
        final Cache<Query, QuerySegment> cache = ((MOCCAD) this.getApplication()).getQueryCache();

        queryCondition   = (EditText)findViewById(R.id.queryCondition);
        executeQuery = (Button)findViewById(R.id.executeQuery);
        executeQuery.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String selectField = (String)dropdown.getSelectedItem();
                        //String fromTable = selectField.substring(0, selectField.indexOf('.'));
                        //    selectField = selectField.substring(selectField.indexOf('.') + 1);
                        String conditionField = (String)dropdown2.getSelectedItem();
                        String condition = queryCondition.getText().toString();
                        String fromTable = (String)dropdown.getSelectedItem();

                        //String QUERY = "SELECT " + selectField + " FROM " + fromTable + " WHERE " + conditionField;
                        //String QUERY = "SELECT TOP 5 * FROM " + fromTable + " WHERE " + conditionField;
                        String QUERY = "SELECT * FROM " + fromTable + " LIMIT 5"; // + " WHERE " + conditionField;
                        if(isConditionText(condition))
                            QUERY += " = \"" + condition + "\""; //Adds quotation marks for string literals.
                        //else
                        //    QUERY += " = " + condition; //No quotation marks for numeric fields

                        q = new Query(selectField);
                        try{ q.addPredicate(new XopYPredicate(conditionField, "=", condition)); }
                        catch(InvalidPredicateException | TrivialPredicateException e){e.printStackTrace();}
                        QuerySegment qs = new QuerySegment();

                        BackgroundTask task = new BackgroundTask();
                        task.getDBInfo = true;
                        String cachedResults = null;

                        try
                        {
                            if(cache.containsKey(q))
                            {
                                //System.out.println("Running query from cache.");

                                Log.i("QueryBuilder", "Running query from cache.");

                                String template = "{\"server_response\":[{\"Field\":\"%s\"}]}";
                                List<List<String>> tuples = null;
                                tuples = cache.getCacheContentManager().get(q).getTuples();

                                System.out.println("TUPLES:");
                                Iterator<List<String>> iter1 = tuples.iterator();
                                while(iter1.hasNext())
                                {
                                    Iterator<String> iter2 = iter1.next().iterator();
                                    while(iter2.hasNext())
                                    {
                                        String fieldValue = iter2.next();
                                        System.out.println(fieldValue);
                                        cachedResults = String.format(template, fieldValue);

                                    }
                                }
                            }
                            else {
                                cache.add(q, qs.filter(q));
                            }

                            BackgroundTask runQuery = new BackgroundTask();
                            runQuery.getDBInfo = false;
                            runQuery.query = QUERY;
                            runQuery.cachedResults = cachedResults;
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
        String cachedResults = null;

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
                else if(this.cachedResults != null)
                {
                    return cachedResults.trim();
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
                intent.putExtra("queryRelation",q.getRelation());
                Set<Predicate> preds = q.getPredicates();
                Iterator<Predicate> predicateIterator = preds.iterator();
                if(predicateIterator.hasNext())
                    intent.putExtra("queryPredicate", predicateIterator.next().toString());
                else
                    intent.putExtra("queryPredicate", "");
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
                    //column = jo.getString("COLUMN_NAME");

                    //fields.add(table + "." + column);
                    fields.add(table);

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


    public void addPredicate(View v) {


    }

}
