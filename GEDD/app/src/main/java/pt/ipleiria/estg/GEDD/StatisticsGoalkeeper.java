package pt.ipleiria.estg.GEDD;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import pt.ipleiria.estg.GEDD.Models.Game;
import pt.ipleiria.estg.GEDD.Models.Goalkeeper;


public class StatisticsGoalkeeper extends Activity {

    private LinkedList<Goalkeeper> gks;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_goalkeeper);

        RelativeLayout statisticsGKLayout = (RelativeLayout) findViewById(R.id.statisticsGKLayout);

        gks = new LinkedList((List)(getIntent().getSerializableExtra("Goalkeepers")));
        game = (Game) getIntent().getSerializableExtra("Game");

        Collections.sort(gks, new Comparator<Goalkeeper>() {
            @Override
            public int compare(Goalkeeper g1, Goalkeeper g2) {
                if (g1.getNumber() < g2.getNumber()) {
                    return -1;
                }
                if (g1.getNumber() > g2.getNumber()) {
                    return 1;
                }
                return 0;
            }
        });

        final TextView zone_stats[] = new TextView[9];
        int j = 1;
        for (int i = 0; i < 9; i++) {

            String id_txt = "zone" + j + "_stats";
            zone_stats[i] = (TextView) findViewById(getResources().getIdentifier(id_txt, "id", getPackageName()));
            j++;
        }

        final TextView zone_goals[] = new TextView[9];
        int p = 1;
        for (int i = 0; i < 9; i++) {

            String id_txt = "zone" + p + "_goals";
            zone_goals[i] = (TextView) findViewById(getResources().getIdentifier(id_txt, "id", getPackageName()));
            p++;
        }

        final ImageButton btn_gks[] = new ImageButton[2];

        btn_gks[0] = (ImageButton) findViewById(R.id.gk1_stats);
        btn_gks[1] = (ImageButton) findViewById(R.id.gk2_stats);

        for (int i = 0; i < gks.size(); i++) {

            String numberShirt = "ic_shirt_" + Integer.toString((gks.get(i).getNumber()));

            Resources resources = getResources();
            final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
            btn_gks[i].setImageResource(resourceId);
        }

        if (gks.size() == 1){
            btn_gks[1].setVisibility(View.INVISIBLE);
        }

        final View.OnTouchListener gkTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (!(v.isPressed())) {

                        ViewGroup container = (ViewGroup) v.getParent();
                        for (int i = 0; i < container.getChildCount(); i++) {
                            container.getChildAt(i).setPressed(false);
                        }
                        v.setPressed(true);

                        if (v.getId() == R.id.gk1_stats) {
                            refreshGKStatistics(zone_stats, zone_goals, gks.get(0));

                        } else{

                            refreshGKStatistics(zone_stats, zone_goals, gks.get(1));
                        }

                    } else {

                        v.setPressed(false);
                    }
                }
                return true;//Return true, so there will be no onClick-event
            }
        };

        btn_gks[0].setOnTouchListener(gkTouchListener);
        btn_gks[1].setOnTouchListener(gkTouchListener);

        statisticsGKLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeRight() {
               // callIntentToStatisticsTeam();
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statistics_goalkeeper, menu);
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

    public void callIntentToStatisticsTeam(){
        Intent intent = new Intent(this, StatisticsTeam.class);
        setResult(RESULT_CANCELED, intent);
        startActivity(intent);
    }

    private void refreshGKStatistics(TextView zone_stats[], TextView zone_goals[], Goalkeeper goalkeeper){

        int opponent_Goals[] = new int[9];
        int opponent_Missed[] = new int[9];
        int effectiveness = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                opponent_Goals[i] += goalkeeper.getZoneAllGoals(j + 1, i + 1);
                opponent_Missed[i] += goalkeeper.getZoneAllDefended(j + 1, i + 1);
            }

            if (opponent_Missed[i] != 0 || opponent_Goals[i] != 0){
                effectiveness = 100 - Math.round((opponent_Goals[i] / ((float) (opponent_Missed[i]) + opponent_Goals[i])) * 100);
                zone_stats[i].setText(String.valueOf(effectiveness) + "%");

                if (effectiveness < 25){
                    zone_stats[i].setBackgroundColor(Color.parseColor("#cc0000"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#cc0000"));
                } else if (effectiveness < 50){
                    zone_stats[i].setBackgroundColor(Color.parseColor("#ff9705"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#ff9705"));
                } else if (effectiveness < 75){
                    zone_stats[i].setBackgroundColor(Color.parseColor("#fffcff00"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#fffcff00"));
                } else if (effectiveness < 101){
                    zone_stats[i].setBackgroundColor(Color.parseColor("#ff01ff00"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#ff01ff00"));
                }

            } else {
                zone_stats[i].setText("---");
                zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
            }

            zone_goals[i].setText(opponent_Goals[i] +  "/" + (opponent_Missed[i] + opponent_Goals[i]));







        }
    }
}
