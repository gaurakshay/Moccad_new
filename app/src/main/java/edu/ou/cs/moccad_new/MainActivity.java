package edu.ou.cs.moccad_new;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
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

    EditText ipAddress; //Text field for IP Address.
    Button queryButton;
    String ip; // String for IP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView ipText = (TextView) findViewById(R.id.ip_text);
        ipText.setText("Enter the IP address of the database server");

        ipAddress   = (EditText)findViewById(R.id.ip_address);
        ipAddress.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        queryButton = (Button)findViewById(R.id.queryBuilderButton);
        queryButton.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    ip = ipAddress.getText().toString();
                    if(!ip.equals(""))
                    {
                        //I put this here in order to prevent the app from crashing if you press "Build Query" without entering an ip address.
                        Intent i = new Intent(getApplicationContext(), edu.ou.cs.moccad_new.QueryBuilder.class);
                        i.putExtra("IPAddress", ip);
                        startActivity(i);
                    }
                    else
                    {
                        //Try to get the IP address from the preferences.
                        //TODO: It doesn't seem to be working..
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        ip = sharedPref.getString(SettingsActivity.KEY_PREF_IP_ADDRESS, "192.168.122.1");
                        Intent i = new Intent(getApplicationContext(), edu.ou.cs.moccad_new.QueryBuilder.class);
                        i.putExtra("IPAddress", ip);
                        startActivity(i);
                    }
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item
                Intent display_settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(display_settings);
                return true;
/*
            case R.id.action_enter_weights: //When WEIGHTS is clicked on the menu.
                Weights weights = new Weights();
                Intent i = new Intent(getApplicationContext(), weights.getClass());
                setResult(Activity.RESULT_OK, i);
                startActivityForResult(i, 1); //Starts the Weight activity.
                return true;
*/
            case R.id.action_display_cache:
                Intent display_cache = new Intent(getApplicationContext(), edu.ou.cs.moccad_new.Display_cache.class);
                startActivity(display_cache);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) //Gets the results of activities started with startActivityForResult()
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) //GET WEIGHTS
        {
            System.out.println("main activity time: " + data.getStringExtra("time"));
            System.out.println("main activity money: " + data.getStringExtra("money"));
            System.out.println("main activity power: " + data.getStringExtra("power"));
        }
    }

}
