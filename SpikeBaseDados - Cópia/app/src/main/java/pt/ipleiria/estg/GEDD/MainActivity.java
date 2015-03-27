package pt.ipleiria.estg.GEDD;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silva16.GEDD.R;

import pt.ipleiria.estg.GEDD.Models.Player;


public class MainActivity extends ActionBarActivity {

    DBAdapter database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActionBar Test
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("My Own Title");

        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton10);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        //------------------------//

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


            Player player1 = new Player(1);

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
