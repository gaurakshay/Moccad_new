package edu.ou.cs.moccad_new;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText; //Import for the IP Address text field.
import android.widget.Button; //Import for the connect button.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String json_string;
    EditText ipAddress; //Text field for IP Address.
    Button button,
            queryButton;
    String ip; // String for IP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tview = (TextView) findViewById(R.id.txtview);
        tview.setText("Please click on button below to fetch data from table.");

        TextView ipText = (TextView) findViewById(R.id.ip_text);
        ipText.setText("Enter the IP address of the database server");

        button = (Button)findViewById(R.id.fetchJson);
        ipAddress   = (EditText)findViewById(R.id.ip_address);
        ipAddress.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        queryButton = (Button)findViewById(R.id.queryBuilderButton);
        queryButton.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    ip = ipAddress.getText().toString();
                    Intent i = new Intent(getApplicationContext(), edu.ou.cs.moccad_new.QueryBuilder.class);
                    i.putExtra("IPAddress", ip);
                    startActivity(i);
                }
            });


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
        ip = ipAddress.getText().toString();
        new BackgroundTask().execute();
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String tempString;
        String strUrl = null;

        @Override
        protected void onPreExecute() {
            strUrl = "http://192.168.0.12:80/create_json.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                System.out.println("In doInBackground");
                System.out.println("IP: " + ip);
                //URL url = new URL(strUrl);
                URL url = new URL("http://" + ip + "/create_json.php");
                System.out.println("URL: " + strUrl);
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
                System.out.println("Threw MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Threw IOException");
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
