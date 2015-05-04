package pt.ipleiria.estg.GEDD;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;
import pt.ipleiria.estg.GEDD.R;

public class StatisticsTeam extends Activity {


    private Bundle extras;
    private LinkedList<Player> players;
    private LinkedList<Goalkeeper> gks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_team);

        final TextView zone1_stats = (TextView) findViewById(R.id.zone1_stats);
        final TextView zone2_stats = (TextView) findViewById(R.id.zone2_stats);
        final TextView zone3_stats = (TextView) findViewById(R.id.zone3_stats);
        final TextView zone4_stats = (TextView) findViewById(R.id.zone4_stats);
        final TextView zone5_stats = (TextView) findViewById(R.id.zone5_stats);
        final TextView zone6_stats = (TextView) findViewById(R.id.zone6_stats);
        final TextView zone7_stats = (TextView) findViewById(R.id.zone7_stats);
        final TextView zone8_stats = (TextView) findViewById(R.id.zone8_stats);
        final TextView zone9_stats = (TextView) findViewById(R.id.zone9_stats);
        final RelativeLayout teamPlayer = (RelativeLayout) findViewById(R.id.team);

        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            players = new LinkedList((List)(getIntent().getSerializableExtra("Players")));
            gks = new LinkedList((List)(getIntent().getSerializableExtra("Goalkeepers")));
        } else {
            players= (LinkedList) savedInstanceState.getSerializable("Players");
            gks= (LinkedList) savedInstanceState.getSerializable("Goalkeepers");
        }

        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getNumber() < p2.getNumber()) {
                    return -1;
                }
                if (p1.getNumber() > p2.getNumber()) {
                    return 1;
                }
                return 0;
            }
        });

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

        final ImageButton btn_players[] = new ImageButton[14];
        final ImageButton btn_gks[] = new ImageButton[2];
        int k = 1;
        for (int i = 0; i < 14; i++) {

            String id_btn = "player" + k + "_stats";
            btn_players[i] = (ImageButton) findViewById(getResources().getIdentifier(id_btn, "id", getPackageName()));
            k++;
        }

        btn_gks[0] = (ImageButton) findViewById(R.id.gk1_stats);
        btn_gks[1] = (ImageButton) findViewById(R.id.gk2_stats);


        for (int i = 0; i < 14; i++) {

            String numberShirt = "ic_shirt_" + Integer.toString((players.get(i).getNumber()));

            Resources resources = getResources();
            final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
            btn_players[i].setImageResource(resourceId);
        }

        for (int i = 0; i < 2; i++) {

            String numberShirt = "ic_shirt_" + Integer.toString((gks.get(i).getNumber()));

            Resources resources = getResources();
            final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
            btn_gks[i].setImageResource(resourceId);
        }





        final View.OnTouchListener playerTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!(v.isPressed())) {

                        ViewGroup container = (ViewGroup) v.getParent();
                        for (int i = 0; i < container.getChildCount(); i++) {
                            container.getChildAt(i).setPressed(false);
                        }
                        v.setPressed(true);
                        Player player;

                        //verifica se é botão de jogador, se for verdade atualiza os campos
                        //if (v.getParent() == (RelativeLayout) teamPlayer) {
                            Player tempPlayer;
                            if ((tempPlayer = getPlayerPressed(players, teamPlayer)) != null) {

                                refreshAttackStatistics(zone1_stats, zone2_stats, zone3_stats, zone4_stats, zone5_stats, zone6_stats, zone7_stats,zone8_stats, zone9_stats, tempPlayer);
                                //refreshDefensiveStatistics(btn_block_def, btn_disarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, tempPlayer);
                            }
                        //}

                    } else {
                        v.setPressed(false);
                    }
                }
                return true;//Return true, so there will be no onClick-event
            }
        };


        btn_players[0].setOnTouchListener(playerTouchListener);
        btn_players[1].setOnTouchListener(playerTouchListener);
        btn_players[2].setOnTouchListener(playerTouchListener);
        btn_players[3].setOnTouchListener(playerTouchListener);
        btn_players[4].setOnTouchListener(playerTouchListener);
        btn_players[5].setOnTouchListener(playerTouchListener);
        btn_players[6].setOnTouchListener(playerTouchListener);
        btn_players[7].setOnTouchListener(playerTouchListener);
        btn_players[8].setOnTouchListener(playerTouchListener);
        btn_players[9].setOnTouchListener(playerTouchListener);
        btn_players[10].setOnTouchListener(playerTouchListener);
        btn_players[11].setOnTouchListener(playerTouchListener);
        btn_players[12].setOnTouchListener(playerTouchListener);
        btn_players[13].setOnTouchListener(playerTouchListener);
        btn_gks[0].setOnTouchListener(playerTouchListener);
        btn_gks[1].setOnTouchListener(playerTouchListener);


        for (int i = 0; i < 14; i++){
            btn_players[i].setTag(players.get(i).getNumber());
        }

        btn_gks[0].setTag(gks.get(0).getNumber());
        btn_gks[1].setTag(gks.get(0).getNumber());

        RelativeLayout statisticsLayout = (RelativeLayout) findViewById(R.id.statisticsLayout);

        statisticsLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeRight() {
                callIntentToMain();
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics_team, menu);
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

    private Player getPlayerPressed(LinkedList<Player> players, RelativeLayout teamPlayer) {
        ImageButton btnPlayer;

        if ((btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null) {
            for (Player player : players) {
                if (player.getNumber() == Integer.valueOf(btnPlayer.getTag().toString())) {
                    return player;
                }
            }
        }
        return null;
    }

    private ImageButton isChildrenImgButtonPressed(RelativeLayout container){
        for (int i=0 ; i < container.getChildCount(); i++){
            if(container.getChildAt(i).isPressed() == true && container.getChildAt(i).getTag() != null)
                return (ImageButton) container.getChildAt(i);
        }
        return null;
    }


    public void callIntentToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void refreshAttackStatistics(TextView zone1_stats, TextView zone2_stats, TextView zone3_stats, TextView zone4_stats, TextView zone5_stats, TextView zone6_stats, TextView zone7_stats, TextView zone8_stats, TextView zone9_stats, Player player){

        if (player.getZoneShots(1)!=0){
            zone1_stats.setText(String.valueOf(Math.round(player.getZoneGoals(1)/(float)player.getZoneShots(1)*100))+ "%");
        }else{
            zone1_stats.setText("0 %");
        }

        if (player.getZoneShots(2)!=0){
            zone2_stats.setText(String.valueOf(Math.round(player.getZoneGoals(2)/(float)player.getZoneShots(2)*100))+ "%");
        }else{
            zone2_stats.setText("0 %");
        }

        if (player.getZoneShots(3)!=0){
            zone3_stats.setText(String.valueOf(Math.round((player.getZoneGoals(3)/(float)player.getZoneShots(3))*100)) + "%");
        }else{
            zone3_stats.setText("0 %");
        }

        if (player.getZoneShots(4)!=0){
            zone4_stats.setText(String.valueOf(Math.round((player.getZoneGoals(4)/(float)player.getZoneShots(4))*100)) + "%");
        }else{
            zone4_stats.setText("0 %");
        }

        if (player.getZoneShots(5)!=0){
            zone5_stats.setText(String.valueOf(Math.round((player.getZoneGoals(5)/(float)player.getZoneShots(5))*100)) + "%");
        }else{
            zone5_stats.setText("0 %");
        }

        if (player.getZoneShots(6)!=0){
            zone6_stats.setText(String.valueOf(Math.round((player.getZoneGoals(6)/(float)player.getZoneShots(6))*100)) + "%");
        }else{
            zone6_stats.setText("0 %");
        }

        if (player.getZoneShots(7)!=0){
            zone7_stats.setText(String.valueOf((Math.round(player.getZoneGoals(7)/(float)player.getZoneShots(7))*100)) + "%");
        }else{
            zone7_stats.setText("0 %");
        }

        if (player.getZoneShots(8)!=0){
            zone8_stats.setText(String.valueOf((Math.round(player.getZoneGoals(8)/(float)player.getZoneShots(8))*100)) + "%");
        }else{
            zone8_stats.setText("0 %");
        }

        if (player.getZoneShots(9)!=0){
            zone9_stats.setText(String.valueOf((Math.round(player.getZoneGoals(9)/(float)player.getZoneShots(9))*100)) + "%");
        }else{
            zone9_stats.setText("0 %");
        }





    }
}
