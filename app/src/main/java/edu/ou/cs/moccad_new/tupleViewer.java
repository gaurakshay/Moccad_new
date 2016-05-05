package edu.ou.cs.moccad_new;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class tupleViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuple_viewer);

        String tuple = getIntent().getExtras().getString("tuple_string");

        TextView tupleText = (TextView) findViewById(R.id.tupleView);
        tupleText.setText(tuple);
    }
}
