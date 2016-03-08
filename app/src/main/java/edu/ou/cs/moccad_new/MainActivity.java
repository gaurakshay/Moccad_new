package edu.ou.cs.moccad_new;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tview = (TextView) findViewById(R.id.txtview);
        tview.setText("Please click on button below to fetch data from table.");

/*       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    // User Defined functions
    //
    public void fetchJson(View v){
        new BackgroundTask().execute();
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String tempString;
        String strUrl = null;

        @Override
        protected void onPreExecute() {
            strUrl = "http://192.168.0.29:80/create_json.php";
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
                while((tempString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(tempString+"\n");
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
            //TextView tview = (TextView) findViewById(R.id.txtview);
            //tview.setText(result);
            json_string = result;
            if(json_string!=null) {
                Intent intent = new Intent(getApplicationContext(), QueryResults.class);
                intent.putExtra("json_string", json_string);
                startActivity(intent);
            }
        }
    }


/*    public void dispJson (View v) {
        Intent intent = new Intent(this, QueryResults.class);
        intent.putExtra("json_string", json_string);
        startActivity(intent);
    }*/

}
