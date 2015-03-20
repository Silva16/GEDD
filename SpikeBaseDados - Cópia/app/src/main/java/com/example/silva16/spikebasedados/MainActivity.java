package com.example.silva16.spikebasedados;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    DBAdapter database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button btn_zone_1 = (Button) findViewById(R.id.btn_zone_1);
        Button btn_zone_2 = (Button) findViewById(R.id.btn_zone_2);
        Button btn_zone_3 = (Button) findViewById(R.id.btn_zone_3);
        Button btn_zone_4 = (Button) findViewById(R.id.btn_zone_4);
        Button btn_zone_5 = (Button) findViewById(R.id.btn_zone_4);
        Button btn_zone_6 = (Button) findViewById(R.id.btn_zone_6);
        Button btn_zone_7 = (Button) findViewById(R.id.btn_zone_7);
        Button btn_zone_8 = (Button) findViewById(R.id.btn_zone_8);




        View.OnTouchListener myTouchListener = new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((Button) v).setPressed(true);
                    //TODO: Add the code of your onClick-event here
                }
                return true;//Return true, so there will be no onClick-event
            }
        };

        btn_zone_1.setOnTouchListener(myTouchListener);
        btn_zone_2.setOnTouchListener(myTouchListener);
        btn_zone_3.setOnTouchListener(myTouchListener);
        btn_zone_4.setOnTouchListener(myTouchListener);
        btn_zone_5.setOnTouchListener(myTouchListener);
        btn_zone_6.setOnTouchListener(myTouchListener);
        btn_zone_7.setOnTouchListener(myTouchListener);
        btn_zone_8.setOnTouchListener(myTouchListener);

    }

    private void action() {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick_AddGoal(View v){

    }

}
