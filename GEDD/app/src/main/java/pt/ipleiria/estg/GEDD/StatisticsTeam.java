package pt.ipleiria.estg.GEDD;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import pt.ipleiria.estg.GEDD.Models.Game;
import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;

public class StatisticsTeam extends Activity {


    private Bundle extras;
    private LinkedList<Player> players;
    private LinkedList<Goalkeeper> gks;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_team);

        final RelativeLayout teamPlayer = (RelativeLayout) findViewById(R.id.team);
        final RelativeLayout filters = (RelativeLayout) findViewById(R.id.filters);

        final Button atk_ca_filter = (Button) findViewById(R.id.atk_ca_filter);
        final Button atk_filter = (Button) findViewById(R.id.atk_filter);
        final Button ca_filter = (Button) findViewById(R.id.ca_filter);
        final Button def_filter = (Button) findViewById(R.id.def_filter);

        final TextView assist_stats = (TextView) findViewById(R.id.assist_stats);
        final TextView ftec_stats = (TextView) findViewById(R.id.ftec_stats);
        final TextView ftec_adv_stats = (TextView) findViewById(R.id.ftec_adv_stats);


        extras = getIntent().getExtras();
        players = new LinkedList((List)(getIntent().getSerializableExtra("Players")));
        gks = new LinkedList((List)(getIntent().getSerializableExtra("Goalkeepers")));
        game = (Game) getIntent().getSerializableExtra("Game");


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

        final TextView zone_stats[] = new TextView[9];
        int j = 1;
        for (int i = 0; i < 9; i++) {

            String id_txt = "zone" + j + "_stats";
            zone_stats[i] = (TextView) findViewById(getResources().getIdentifier(id_txt, "id", getPackageName()));
            j++;
        }

        refreshAssistStatistics(assist_stats, players);
        refreshTecFailStatistics(ftec_stats, players);
        ftec_adv_stats.setText("Falhas Técnicas Advers. " + String.valueOf(game.getTechnicalFailAdv()));

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
                    Player tempPlayer;
                    if (!(v.isPressed())) {

                        ViewGroup container = (ViewGroup) v.getParent();
                        for (int i = 0; i < container.getChildCount(); i++) {
                            container.getChildAt(i).setPressed(false);
                        }
                        v.setPressed(true);

                        //verifica se é botão de jogador, se for verdade atualiza os campos
                        //if (v.getParent() == (RelativeLayout) teamPlayer) {

                            if ((tempPlayer = getPlayerPressed(players, teamPlayer)) != null && v.getParent() == (RelativeLayout) filters) {

                                assist_stats.setText("Assist: " + tempPlayer.getAssistance());
                                ftec_stats.setText("Falhas Técnicas " + tempPlayer.getTechnicalFailure());

                                switch (v.getId()){
                                    case R.id.atk_ca_filter:
                                        refreshAllAttackPlayerStatistics(zone_stats, tempPlayer);
                                        return true;
                                    case R.id.ca_filter:
                                        refreshCAPlayerStatistics(zone_stats, tempPlayer);
                                        return true;
                                    case R.id.atk_filter:
                                        refreshAttackPlayerStatistics(zone_stats, tempPlayer);
                                        return true;
                                    default:
                                        return false;
                                }

                            }


                            if (v.getId() == R.id.def_filter){
                                refreshDefensePlayerStatistics(zone_stats, gks);
                            }

                        } else {

                        v.setPressed(false);

                        if ((tempPlayer = getPlayerPressed(players, teamPlayer)) == null && v.getParent() == (RelativeLayout) filters) {

                            refreshAssistStatistics(assist_stats, players);
                            refreshTecFailStatistics(ftec_stats, players);
                            ftec_adv_stats.setText("Falhas Técnicas Advers. " + String.valueOf(game.getTechnicalFailAdv()));

                            switch (v.getId()){
                                case R.id.atk_ca_filter:
                                    refreshAllAttackStatistics(zone_stats, players);
                                    return true;
                                case R.id.ca_filter:
                                    refreshCAStatistics(zone_stats, players);
                                    return true;
                                case R.id.atk_filter:
                                    refreshAttackStatistics(zone_stats, players);
                                    return true;
                                default:
                                    return false;
                            }
                        }


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

        atk_ca_filter.setOnTouchListener(playerTouchListener);
        atk_filter.setOnTouchListener(playerTouchListener);
        ca_filter.setOnTouchListener(playerTouchListener);
        def_filter.setOnTouchListener(playerTouchListener);


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

    private void refreshAssistStatistics(TextView assist_stats, LinkedList<Player> players){

        int assist = 0;

        for (Player player : players){

            assist += player.getAssistance();
        }

        assist_stats.setText("Assist. " + String.valueOf(assist));
    }

    private void refreshTecFailStatistics(TextView assist_stats, LinkedList<Player> players){

        int tecFail = 0;

        for (Player player : players){

            tecFail += player.getTechnicalFailure();
        }

        assist_stats.setText("Falhas Técnicas " + String.valueOf(tecFail));
    }

    private void refreshAllAttackPlayerStatistics(TextView zone_stats[], Player player){

        for (int i = 0; i < 9; i++){

            if (player.getZoneAllShots(i + 1) !=0){
                zone_stats[i].setText(String.valueOf(Math.round(player.getZoneAllGoals(i + 1)/(float)player.getZoneAllShots(i + 1)*100))+ "%");
            }else{
                zone_stats[i].setText("0 %");
            }
        }
    }

    private void refreshAttackPlayerStatistics(TextView zone_stats[], Player player){

        for (int i = 0; i < 9; i++){

            if (player.getZoneAtkShots(i + 1) !=0){
                zone_stats[i].setText(String.valueOf(Math.round((player.getZoneAtkGoals(i + 1)/(float)player.getZoneAtkShots(i+1)*100))+ "%"));
            }else{
                zone_stats[i].setText("0 %");
            }

        }
    }

    private void refreshCAPlayerStatistics(TextView zone_stats[], Player player){

        for (int i = 0; i < 9; i++){

            if (player.getZoneCAShots(i + 1) !=0){
                zone_stats[i].setText(String.valueOf(Math.round((player.getZoneCAGoals(i + 1)/(float)player.getZoneCAShots(i + 1)*100))+ "%"));
            }else{
                zone_stats[i].setText("0 %");
            }

        }
    }

    private void refreshDefensePlayerStatistics(TextView zone_stats[], LinkedList<Goalkeeper> gks){

        int opponent_zoneGoals[] = new int[9];
        int opponent_zoneShots[] = new int[9];

        for (Goalkeeper goalkeeper : gks){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    opponent_zoneGoals[i] += goalkeeper.getZoneAllGoals(i + 1, j + 1);
                    opponent_zoneShots[i] += goalkeeper.getZoneAllShots(i + 1, j + 1);
                }

                if (opponent_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(Math.round(opponent_zoneGoals[i] / (float) opponent_zoneShots[i] * 100)) + "%");
                }else{
                    zone_stats[i].setText("0 %");
                }
            }
        }
    }


    private void refreshAllAttackStatistics(TextView zone_stats[], LinkedList<Player> players){

        int team_zoneGoals[] = new int[9];
        int team_zoneShots[] = new int[9];

        for (Player player : players){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += player.getZoneAllGoals(i + 1);
                team_zoneShots[i] += player.getZoneAllShots(i + 1);

                if (team_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(Math.round(team_zoneGoals[i] / (float) team_zoneShots[i] * 100)) + "%");
                }else{
                    zone_stats[i].setText("0 %");
                }

            }
        }
    }

    private void refreshAttackStatistics(TextView zone_stats[], LinkedList<Player> players){

        int team_zoneGoals[] = new int[9];
        int team_zoneShots[] = new int[9];

        for (Player player : players){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += player.getZoneAtkGoals(i + 1);
                team_zoneShots[i] += player.getZoneAtkShots(i + 1);

                if (team_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(Math.round(team_zoneGoals[i] / (float) team_zoneShots[i] * 100)) + "%");
                }else{
                    zone_stats[i].setText("0 %");
                }

            }
        }
    }

    private void refreshCAStatistics(TextView zone_stats[], LinkedList<Player> players){

        int team_zoneGoals[] = new int[9];
        int team_zoneShots[] = new int[9];

        for (Player player : players){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += player.getZoneCAGoals(i + 1);
                team_zoneShots[i] += player.getZoneCAShots(i + 1);

                if (team_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(Math.round(team_zoneGoals[i] / (float) team_zoneShots[i] * 100)) + "%");
                }else{
                    zone_stats[i].setText("0 %");
                }

            }
        }
    }


}
