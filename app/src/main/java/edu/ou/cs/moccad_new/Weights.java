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

public class Weights extends AppCompatActivity {
    int time = 0,
        money = 0,
        power = 0;
    Button submitButton;
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

        //timeBar.setOnSeekBarChangeListener(this);
        /*timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
            }
        });*/

        submitButton = (Button)findViewById(R.id.submit_weights);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        System.out.println("Submit clicked");
                        Intent i = new Intent(getApplicationContext(), edu.ou.cs.moccad_new.MainActivity.class);
                        setTime(timeBar.getProgress());
                        setMoney(moneyBar.getProgress());
                        setPower(powerBar.getProgress());

                        i.putExtra("time", getTime());
                        i.putExtra("money", getMoney());
                        i.putExtra("power", getPower());

                        System.out.println("TIME: " + getTime());
                        System.out.println("MONEY: " + getMoney());
                        System.out.println("POWER: " + getPower());

                        setResult(1, i);
                        finish();
                    }
                });
    }
}
