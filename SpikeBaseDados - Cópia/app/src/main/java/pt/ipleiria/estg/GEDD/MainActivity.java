package pt.ipleiria.estg.GEDD;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        Button btn_zone_5 = (Button) findViewById(R.id.btn_zone_5);
        Button btn_zone_6 = (Button) findViewById(R.id.btn_zone_6);
        Button btn_zone_7 = (Button) findViewById(R.id.btn_zone_7);
        Button btn_zone_8 = (Button) findViewById(R.id.btn_zone_8);
        Button btn_zone_9 = (Button) findViewById(R.id.btn_zone_9);

        ImageButton btn_player1 = (ImageButton) findViewById(R.id.imgbtn_player1);
        ImageButton btn_player2 = (ImageButton) findViewById(R.id.imgbtn_player2);
        ImageButton btn_player3 = (ImageButton) findViewById(R.id.imgbtn_player3);
        ImageButton btn_player4 = (ImageButton) findViewById(R.id.imgbtn_player4);
        ImageButton btn_player5 = (ImageButton) findViewById(R.id.imgbtn_player5);
        ImageButton btn_player6 = (ImageButton) findViewById(R.id.imgbtn_player6);

        Button btn_ca = (Button) findViewById(R.id.btn_ca);
        Button btn_6m = (Button) findViewById(R.id.btn_6m);
        Button btn_7m = (Button) findViewById(R.id.btn_7m);
        Button btn_9m = (Button) findViewById(R.id.btn_9m);

        Button btn_goal = (Button) findViewById(R.id.btn_goal);
        Button btn_goalpost = (Button) findViewById(R.id.btn_goalpost);
        Button btn_block_atk = (Button) findViewById(R.id.btn_block_atk);
        Button btn_out = (Button) findViewById(R.id.btn_out);
        Button btn_defense = (Button) findViewById(R.id.btn_defense);




        final View.OnTouchListener zoneTouchListener = new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(!(v.isPressed())) {
                        ViewGroup container = (ViewGroup) v.getParent();
                        for (int i=0 ; i < container.getChildCount(); i++){
                            container.getChildAt(i);
                            container.getChildAt(i).setPressed(false);
                        }
                        ((Button) v).setPressed(true);
                    }else{
                        ((Button) v).setPressed(false);
                    }


                }
                return true;//Return true, so there will be no onClick-event
            }
        };

        btn_zone_1.setOnTouchListener(zoneTouchListener);
        btn_zone_2.setOnTouchListener(zoneTouchListener);
        btn_zone_3.setOnTouchListener(zoneTouchListener);
        btn_zone_4.setOnTouchListener(zoneTouchListener);
        btn_zone_5.setOnTouchListener(zoneTouchListener);
        btn_zone_6.setOnTouchListener(zoneTouchListener);
        btn_zone_7.setOnTouchListener(zoneTouchListener);
        btn_zone_8.setOnTouchListener(zoneTouchListener);
        btn_zone_9.setOnTouchListener(zoneTouchListener);

        btn_player1.setOnTouchListener(zoneTouchListener);
        btn_player2.setOnTouchListener(zoneTouchListener);
        btn_player3.setOnTouchListener(zoneTouchListener);
        btn_player4.setOnTouchListener(zoneTouchListener);
        btn_player5.setOnTouchListener(zoneTouchListener);
        btn_player6.setOnTouchListener(zoneTouchListener);

        btn_ca.setOnTouchListener(zoneTouchListener);
        btn_6m.setOnTouchListener(zoneTouchListener);
        btn_7m.setOnTouchListener(zoneTouchListener);
        btn_9m.setOnTouchListener(zoneTouchListener);

        btn_goal.setOnTouchListener(zoneTouchListener);
        btn_goalpost.setOnTouchListener(zoneTouchListener);
        btn_out.setOnTouchListener(zoneTouchListener);
        btn_defense.setOnTouchListener(zoneTouchListener);
        btn_block_atk.setOnTouchListener(zoneTouchListener);



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
