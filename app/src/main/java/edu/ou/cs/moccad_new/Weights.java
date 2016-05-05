package edu.ou.cs.moccad_new;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Weights extends AppCompatActivity {
    int time = 0,
        money = 0,
        power = 0;
    Button submitButton,
           emergencyProfileButton,
           moneySaverProfileButton,
           powerSaverProfileButton;
    private SeekBar timeBar = null,
                    moneyBar = null,
                    powerBar = null;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        timeBar = (SeekBar)findViewById(R.id.timeSeekBar);
        moneyBar = (SeekBar)findViewById(R.id.moneySeekBar);
        powerBar = (SeekBar)findViewById(R.id.powerSeekBar);


        MOCCAD mAppl = (MOCCAD) getApplication();
        timeBar.setProgress(mAppl.getTime());
        moneyBar.setProgress(mAppl.getMoney());
        powerBar.setProgress(mAppl.getPower());
        //timeBar.setOnSeekBarChangeListener(this);
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTime(progress);
                TextView timeLabel = (TextView) findViewById(R.id.timeNumber);
                if(timeLabel != null) {
                    String prog = "" + progress;
                    timeLabel.setText(prog);
                }
            }
        });

        moneyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setMoney(progress);
                TextView moneyLabel = (TextView) findViewById(R.id.moneyNumber);
                if(moneyLabel != null) {
                    String prog = "" + progress;
                    moneyLabel.setText(prog);
                }

            }
        });

        powerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPower(progress);
                TextView powerLabel = (TextView) findViewById(R.id.energyNumber);
                if(powerLabel != null) {
                    String prog = "" + progress;
                    powerLabel.setText(prog);
                }

            }
        });

        submitButton = (Button)findViewById(R.id.submit_weights);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        /*
                        Intent i = new Intent(getApplicationContext(), edu.ou.cs.moccad_new.MainActivity.class);
                        */
                        setTime(timeBar.getProgress());
                        setMoney(moneyBar.getProgress());
                        setPower(powerBar.getProgress());
                        /*
                        String t = "" + getTime();
                        String m = "" + getMoney();
                        String p = "" + getPower();

                        i.putExtra("time", t);
                        i.putExtra("money", m);
                        i.putExtra("power", p);

                        setResult(1, i);
                        */
                        ((MOCCAD) getApplication()).setWeights(getTime(), getMoney(), getPower());

                        finish();
                    }
                });

        emergencyProfileButton = (Button) findViewById(R.id.emergencyProfile);
        emergencyProfileButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        timeBar.setProgress(90);
                        moneyBar.setProgress(5);
                        powerBar.setProgress(20);
                    }
                }
        );

        moneySaverProfileButton = (Button) findViewById(R.id.moneySaverButton);
        moneySaverProfileButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        timeBar.setProgress(5);
                        moneyBar.setProgress(90);
                        powerBar.setProgress(20);
                    }
                }
        );

        powerSaverProfileButton = (Button) findViewById(R.id.lowPowerProfile);
        powerSaverProfileButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        timeBar.setProgress(5);
                        moneyBar.setProgress(20);
                        powerBar.setProgress(90);
                    }
                }
        );
    }
}
