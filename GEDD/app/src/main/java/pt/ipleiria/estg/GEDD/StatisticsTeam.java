package pt.ipleiria.estg.GEDD;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    private LinkedList<Player> players;
    private LinkedList<Goalkeeper> gks;
    private Game game;


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if(resultCode == RESULT_OK){

            }
            if (resultCode == RESULT_CANCELED) {
                onResume();
            }
            if(resultCode == 2){
                finish();
            }
            if(resultCode == 3){
                setResult(3);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        players = new LinkedList((List)(getIntent().getSerializableExtra("Players")));
        gks = new LinkedList((List)(getIntent().getSerializableExtra("Goalkeepers")));
        game = (Game) getIntent().getSerializableExtra("Game");

        setContentView(R.layout.statistics_team);

        final RelativeLayout teamPlayer = (RelativeLayout) findViewById(R.id.team);
        final RelativeLayout filters = (RelativeLayout) findViewById(R.id.filters);

        final Button atk_ca_filter = (Button) findViewById(R.id.atk_ca_filter);
        final Button atk_filter = (Button) findViewById(R.id.atk_filter);
        final Button ca_filter = (Button) findViewById(R.id.ca_filter);
        final Button def_goal_filter = (Button) findViewById(R.id.def_goal_filter);
        final Button def_all_filter = (Button) findViewById(R.id.def_all_filter);

        final TextView assist_stats = (TextView) findViewById(R.id.assist_stats);
        final TextView ftec_stats = (TextView) findViewById(R.id.ftec_stats);
        final TextView ftec_adv_stats = (TextView) findViewById(R.id.ftec_adv_stats);




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

        final TextView zone_goals[] = new TextView[9];
        int p = 1;
        for (int i = 0; i < 9; i++) {

            String id_txt = "zone" + p + "_goals";
            zone_goals[i] = (TextView) findViewById(getResources().getIdentifier(id_txt, "id", getPackageName()));
            p++;
        }

        refreshAssistStatistics(assist_stats, players, gks);
        refreshTecFailStatistics(ftec_stats, players, gks);
        ftec_adv_stats.setText("Falhas Técnicas Advers. " + String.valueOf(game.getTechnicalFailAdv()));

        final ImageButton btn_players[] = new ImageButton[14];
        final ImageButton btn_gks[] = new ImageButton[2];
        int k = 1;
        for (int i = 0; i < 14; i++) {

            String id_btn = "player" + k + "_stats";
            btn_players[i] = (ImageButton) findViewById(getResources().getIdentifier(id_btn, "id", getPackageName()));
            if (i >= players.size()){
                btn_players[i].setVisibility(View.INVISIBLE);
            }
            k++;
        }

        btn_gks[0] = (ImageButton) findViewById(R.id.gk1_stats);
        btn_gks[1] = (ImageButton) findViewById(R.id.gk2_stats);

        if (gks.size() == 1){
            btn_gks[1].setVisibility(View.INVISIBLE);
        }


        for (int i = 0; i < players.size(); i++) {

            String numberShirt;

            if(players.get(i).isYellowCard()) {
                numberShirt = "ic_shirt_" + Integer.toString((players.get(i).getNumber())) + "_yellowcard";
            }else if(players.get(i).isRedCard()){
                numberShirt = "ic_shirt_" + Integer.toString((players.get(i).getNumber())) + "_redcard";
            } else{
                numberShirt = "ic_shirt_" + Integer.toString((players.get(i).getNumber()));
            }

            Resources resources = getResources();
            final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
            btn_players[i].setImageResource(resourceId);
        }

        for (int i = 0; i < gks.size(); i++) {

            String numberShirt = "ic_shirt_" + Integer.toString((gks.get(i).getNumber()));

            Resources resources = getResources();
            final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
            btn_gks[i].setImageResource(resourceId);
        }

        final View.OnTouchListener playerTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Player tempPlayer;
                    Goalkeeper tempGk;
                    LinkedList list;
                    Button filter;
                    ImageButton btnPressed;
                    if (!(v.isPressed())) {

                        ViewGroup container = (ViewGroup) v.getParent();
                        for (int i = 0; i < container.getChildCount(); i++) {
                            container.getChildAt(i).setPressed(false);
                        }
                        v.setPressed(true);

                        if ((btnPressed = isChildrenImgButtonPressed(teamPlayer)) != null){
                            if (btnPressed.getId() == R.id.gk1_stats || btnPressed.getId() == R.id.gk2_stats){
                                list = gks;
                            } else {
                                list = players;
                            }
                        } else {
                            list = players;
                        }

                            if (((tempPlayer = getPlayerPressed(list, teamPlayer)) != null && (filter = isChildrenButtonPressed(filters)) != null)) {

                                assist_stats.setText("Assist: " + tempPlayer.getAssistance());
                                ftec_stats.setText("Falhas Técnicas " + tempPlayer.getTechnicalFailure());

                                switch (filter.getId()){
                                    case R.id.atk_ca_filter:
                                        for (int i = 0; i < players.size(); i++) {
                                            btn_players[i].setVisibility(View.VISIBLE);
                                        }
                                        refreshAllAttackPlayerStatistics(zone_stats, zone_goals, tempPlayer);
                                        return true;
                                    case R.id.ca_filter:
                                        for (int i = 0; i < players.size(); i++) {
                                            btn_players[i].setVisibility(View.VISIBLE);
                                        }
                                        refreshCAPlayerStatistics(zone_stats, zone_goals, tempPlayer);
                                        return true;
                                    case R.id.atk_filter:
                                        for (int i = 0; i < players.size(); i++) {
                                            btn_players[i].setVisibility(View.VISIBLE);
                                        }
                                        refreshAttackPlayerStatistics(zone_stats, zone_goals, tempPlayer);
                                        return true;
                                    case R.id.def_goal_filter:
                                        for (int i = 0; i < players.size(); i++) {
                                            btn_players[i].setPressed(false);
                                            btn_players[i].setVisibility(View.INVISIBLE);
                                        }
                                        if (btn_gks[0].isPressed()){
                                            refreshDefensePlayerStatistics(zone_stats, zone_goals, gks.get(0));
                                        } else if (btn_gks[1].isPressed()) {
                                            refreshDefensePlayerStatistics(zone_stats, zone_goals, gks.get(1));
                                        } else {
                                            refreshDefenseStatistics(zone_stats, zone_goals, gks);
                                        }
                                        return true;
                                    case R.id.def_all_filter:
                                        for (int i = 0; i < players.size(); i++) {
                                            btn_players[i].setPressed(false);
                                            btn_players[i].setVisibility(View.INVISIBLE);
                                        }
                                        if (btn_gks[0].isPressed()){
                                            refreshDefenseGlobalPlayerStatistics(zone_stats, zone_goals, gks.get(0));
                                        } else if (btn_gks[1].isPressed()) {
                                            refreshDefenseGlobalPlayerStatistics(zone_stats, zone_goals, gks.get(1));
                                        } else {
                                            refreshDefenseGlobalStatistics(zone_stats, zone_goals, gks);
                                        }
                                        return true;
                                }

                            }

                        if ((getPlayerPressed(list, teamPlayer) == null) && (filter = isChildrenButtonPressed(filters)) != null) {

                            for (int i = 0; i < players.size(); i++) {
                                btn_players[i].setVisibility(View.VISIBLE);
                            }

                            refreshAssistStatistics(assist_stats, players, gks);
                            refreshTecFailStatistics(ftec_stats, players, gks);
                            ftec_adv_stats.setText("Falhas Técnicas Advers. " + String.valueOf(game.getTechnicalFailAdv()));

                            switch (filter.getId()) {
                                case R.id.atk_ca_filter:
                                    refreshAllAttackStatistics(zone_stats, zone_goals, players, gks);
                                    return true;
                                case R.id.ca_filter:
                                    refreshCAStatistics(zone_stats, zone_goals, players, gks);
                                    return true;
                                case R.id.atk_filter:
                                    refreshAttackStatistics(zone_stats, zone_goals, players, gks);
                                    return true;
                                case R.id.def_goal_filter:
                                    for (int i = 0; i < players.size(); i++) {
                                        btn_players[i].setPressed(false);
                                        btn_players[i].setVisibility(View.INVISIBLE);
                                    }
                                    refreshDefenseStatistics(zone_stats, zone_goals, gks);
                                    return true;
                                case R.id.def_all_filter:
                                    for (int i = 0; i < players.size(); i++) {
                                        btn_players[i].setPressed(false);
                                        btn_players[i].setVisibility(View.INVISIBLE);
                                    }
                                    refreshDefenseGlobalStatistics(zone_stats, zone_goals, gks);
                                    return true;
                            }
                        }
                    } else {

                        v.setPressed(false);

                        if (v.getId() == R.id.gk1_stats || v.getId() == R.id.gk2_stats){
                            list = gks;
                        } else {
                            list = players;
                        }

                        if ((getPlayerPressed(list, teamPlayer) == null) && (filter = isChildrenButtonPressed(filters)) != null) {

                            for (int i = 0; i < players.size(); i++) {
                                btn_players[i].setVisibility(View.VISIBLE);
                            }

                            refreshAssistStatistics(assist_stats, players, gks);
                            refreshTecFailStatistics(ftec_stats, players, gks);
                            ftec_adv_stats.setText("Falhas Técnicas Advers. " + String.valueOf(game.getTechnicalFailAdv()));

                            switch (filter.getId()) {
                                case R.id.atk_ca_filter:
                                    refreshAllAttackStatistics(zone_stats, zone_goals, players, gks);
                                    return true;
                                case R.id.ca_filter:
                                    refreshCAStatistics(zone_stats, zone_goals, players, gks);
                                    return true;
                                case R.id.atk_filter:
                                    refreshAttackStatistics(zone_stats, zone_goals, players, gks);
                                    return true;
                                case R.id.def_goal_filter:
                                    for (int i = 0; i < players.size(); i++) {
                                        btn_players[i].setPressed(false);
                                        btn_players[i].setVisibility(View.INVISIBLE);
                                    }
                                    refreshDefenseStatistics(zone_stats, zone_goals, gks);
                                    return true;
                                case R.id.def_all_filter:
                                    for (int i = 0; i < players.size(); i++) {
                                        btn_players[i].setPressed(false);
                                        btn_players[i].setVisibility(View.INVISIBLE);
                                    }
                                    refreshDefenseGlobalStatistics(zone_stats, zone_goals, gks);
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
        def_goal_filter.setOnTouchListener(playerTouchListener);
        def_all_filter.setOnTouchListener(playerTouchListener);


        for (int i = 0; i < players.size(); i++){
            btn_players[i].setTag(players.get(i).getNumber());
        }

        btn_gks[0].setTag(gks.get(0).getNumber());
        btn_gks[1].setTag(gks.get(1).getNumber());

        RelativeLayout statisticsLayout = (RelativeLayout) findViewById(R.id.statisticsLayout);

        statisticsLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeRight() {
                finish();
            }
            public void onSwipeLeft() {
                callIntentToStatisticsGK();
            }
        });

        ImageView mHome = (ImageView) findViewById(R.id.stats_pager_home);
        ImageView mSheet = (ImageView) findViewById(R.id.stats_pager_sheet);
        ImageView mGoal = (ImageView) findViewById(R.id.stats_pager_goal);

        mSheet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsTeam.this, GameActivity.class);
                startActivity(intent);

            }
        });

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsTeam.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsTeam.this, StatisticsGoalkeeper.class);
                intent.putExtra("Game", game);
                intent.putExtra("Goalkeepers", gks);
                intent.putExtra("Players", players);
                startActivityForResult(intent, 2);
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

    private Player getPlayerPressed(LinkedList players, RelativeLayout teamPlayer) {
        ImageButton btnPlayer;
        LinkedList<Player> team = (LinkedList<Player>) players;
        if ((btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null) {
            for (Player player : team) {
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

    private Button isChildrenButtonPressed(RelativeLayout container){
        for (int i=0 ; i < container.getChildCount(); i++){
            if(container.getChildAt(i).isPressed() == true)
                return (Button) container.getChildAt(i);
        }
        return null;
    }


    public void callIntentToMain(){
        Intent intent = new Intent(this, GameActivity.class);
        setResult(RESULT_CANCELED, intent);
        startActivity(intent);
    }

    public void callIntentToStatisticsGK(){
        Intent intent = new Intent(this, StatisticsGoalkeeper.class);
        intent.putExtra("Game", game);
        intent.putExtra("Goalkeepers", gks);
        intent.putExtra("Players", players);
        startActivityForResult(intent, 2);
    }

    private void refreshAssistStatistics(TextView assist_stats, LinkedList<Player> players, LinkedList<Goalkeeper> gks){

        int assist = 0;

        for (Player player : players){

            assist += player.getAssistance();
        }

        for (Goalkeeper gk : gks){

            assist += gk.getAssistance();
        }

        assist_stats.setText("Assist. " + String.valueOf(assist));
    }

    private void refreshTecFailStatistics(TextView ftec, LinkedList<Player> players, LinkedList<Goalkeeper> gks){

        int tecFail = 0;

        for (Player player : players){

            tecFail += player.getTechnicalFailure();
        }

        for (Goalkeeper gk : gks){

            tecFail += gk.getTechnicalFailure();
        }

        ftec.setText("Falhas Técnicas " + String.valueOf(tecFail));
    }

    private void refreshAllAttackPlayerStatistics(TextView zone_stats[], TextView zone_goals[], Player player){



        for (int i = 0; i < 9; i++){

            int effectiveness = Math.round(player.getZoneAllGoals(i + 1) / (float) player.getZoneAllShots(i + 1) * 100);

            if (player.getZoneAllShots(i + 1) !=0){
                zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                setColor(zone_stats[i], zone_goals[i], effectiveness);
            }else{
                zone_stats[i].setText("---");
                zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                zone_stats[i].setTextColor(Color.parseColor("#000000"));
                zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
            }

            zone_goals[i].setText(player.getZoneAllGoals(i + 1) +  "/" + player.getZoneAllShots(i + 1));





        }
    }


    private void refreshAttackPlayerStatistics(TextView zone_stats[], TextView zone_goals[], Player player){

        for (int i = 0; i < 9; i++){

            int effectiveness = Math.round((player.getZoneAtkGoals(i + 1)/(float)player.getZoneAtkShots(i+1)*100));

            if (player.getZoneAtkShots(i + 1) !=0){
                zone_stats[i].setText(String.valueOf(effectiveness)+ "%");
                setColor(zone_stats[i], zone_goals[i], effectiveness);
            }else{
                zone_stats[i].setText("---");
                zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                zone_stats[i].setTextColor(Color.parseColor("#000000"));
                zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
            }

            zone_goals[i].setText(player.getZoneAtkGoals(i + 1) +  "/" + player.getZoneAtkShots(i+1));



        }
    }

    private void refreshCAPlayerStatistics(TextView zone_stats[], TextView zone_goals[], Player player){

        for (int i = 0; i < 9; i++){

            int effectiveness = Math.round((player.getZoneCAGoals(i + 1)/(float)player.getZoneCAShots(i + 1)*100));

            if (player.getZoneCAShots(i + 1) !=0){
                zone_stats[i].setText(String.valueOf(effectiveness)+ "%");
                setColor(zone_stats[i], zone_goals[i], effectiveness);
            }else{
                zone_stats[i].setText("---");
                zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                zone_stats[i].setTextColor(Color.parseColor("#000000"));
                zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
            }

            zone_goals[i].setText((player.getZoneCAGoals(i + 1)) +  "/" + player.getZoneCAShots(i + 1));


        }
    }

    private void refreshDefenseStatistics(TextView zone_stats[], TextView zone_goals[], LinkedList<Goalkeeper> gks){

        int opponent_zoneGoals[] = new int[9];
        int opponent_zoneShots[] = new int[9];

        for (Goalkeeper goalkeeper : gks){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    opponent_zoneGoals[i] += goalkeeper.getZoneAllGoals(i + 1, j + 1);
                    opponent_zoneShots[i] += goalkeeper.getZoneAllDefended(i + 1, j + 1) + goalkeeper.getZoneAllGoals(i + 1, j + 1);
                }

                int effectiveness = 100 - Math.round(opponent_zoneGoals[i] / ((float) opponent_zoneShots[i]) * 100);

                if (opponent_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                    setColor(zone_stats[i], zone_goals[i], effectiveness);
                }else{
                    zone_stats[i].setText("---");
                    zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                    zone_stats[i].setTextColor(Color.parseColor("#000000"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
                }

                zone_goals[i].setText(opponent_zoneGoals[i] +  "/" + (opponent_zoneShots[i]));


            }
        }
    }

    private void refreshDefenseGlobalStatistics(TextView zone_stats[], TextView zone_goals[], LinkedList<Goalkeeper> gks){

        int opponent_zoneGoals[] = new int[9];
        int opponent_zoneShots[] = new int[9];

        for (Goalkeeper goalkeeper : gks){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    opponent_zoneGoals[i] += goalkeeper.getZoneAllGoals(i + 1, j + 1);
                    opponent_zoneShots[i] += goalkeeper.getZoneAllShots(i + 1, j + 1);
                }

                int effectiveness = 100 - Math.round(opponent_zoneGoals[i] / ((float) opponent_zoneShots[i]) * 100);

                if (opponent_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                    setColor(zone_stats[i], zone_goals[i], effectiveness);
                }else{
                    zone_stats[i].setText("---");
                    zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                    zone_stats[i].setTextColor(Color.parseColor("#000000"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
                }

                zone_goals[i].setText(opponent_zoneGoals[i] +  "/" + (opponent_zoneShots[i]));


            }
        }
    }

    private void refreshDefensePlayerStatistics(TextView zone_stats[], TextView zone_goals[], Goalkeeper goalkeeper){

        int opponent_zoneGoals[] = new int[9];
        int opponent_zoneShots[] = new int[9];


            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    opponent_zoneGoals[i] += goalkeeper.getZoneAllGoals(i + 1, j + 1);
                    opponent_zoneShots[i] += goalkeeper.getZoneAllDefended(i + 1, j + 1) + goalkeeper.getZoneAllGoals(i + 1, j + 1);
                }

                int effectiveness = 100 - Math.round(opponent_zoneGoals[i] / ((float) opponent_zoneShots[i]) * 100);

                if (opponent_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                    setColor(zone_stats[i], zone_goals[i], effectiveness);
                }else{
                    zone_stats[i].setText("---");
                    zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                    zone_stats[i].setTextColor(Color.parseColor("#000000"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
                }

                zone_goals[i].setText(opponent_zoneGoals[i] +  "/" + (opponent_zoneShots[i]));


            }

    }

    private void refreshDefenseGlobalPlayerStatistics(TextView zone_stats[], TextView zone_goals[], Goalkeeper goalkeeper){

        int opponent_zoneGoals[] = new int[9];
        int opponent_zoneShots[] = new int[9];


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                opponent_zoneGoals[i] += goalkeeper.getZoneAllGoals(i + 1, j + 1);
                opponent_zoneShots[i] += goalkeeper.getZoneAllShots(i + 1, j + 1);
            }

            int effectiveness = 100 - Math.round(opponent_zoneGoals[i] / ((float) opponent_zoneShots[i]) * 100);

            if (opponent_zoneShots[i] !=0){
                zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                setColor(zone_stats[i], zone_goals[i], effectiveness);
            }else{
                zone_stats[i].setText("---");
                zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                zone_stats[i].setTextColor(Color.parseColor("#000000"));
                zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
            }

            zone_goals[i].setText(opponent_zoneGoals[i] +  "/" + (opponent_zoneShots[i]));


        }

    }


    private void refreshAllAttackStatistics(TextView zone_stats[], TextView zone_goals[], LinkedList<Player> players, LinkedList<Goalkeeper> gks){

        int team_zoneGoals[] = new int[9];
        int team_zoneShots[] = new int[9];

        for (Goalkeeper goalkeeper : gks){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += goalkeeper.getZoneAllGoals(i + 1);
                team_zoneShots[i] += goalkeeper.getZoneAllShots(i + 1);
            }
        }

        for (Player player : players){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += player.getZoneAllGoals(i + 1);
                team_zoneShots[i] += player.getZoneAllShots(i + 1);

                int effectiveness = Math.round(team_zoneGoals[i] / (float) team_zoneShots[i] * 100);

                if (team_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                    setColor(zone_stats[i], zone_goals[i], effectiveness);
                }else{
                    zone_stats[i].setText("---");
                    zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                    zone_stats[i].setTextColor(Color.parseColor("#000000"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
                }

                zone_goals[i].setText(team_zoneGoals[i] +  "/" + team_zoneShots[i]);



            }
        }
    }

    private void refreshAttackStatistics(TextView zone_stats[], TextView zone_goals[], LinkedList<Player> players, LinkedList<Goalkeeper> gks){

        int team_zoneGoals[] = new int[9];
        int team_zoneShots[] = new int[9];

        for (Goalkeeper goalkeeper : gks){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += goalkeeper.getZoneAtkGoals(i + 1);
                team_zoneShots[i] += goalkeeper.getZoneAtkShots(i + 1);
            }
        }

        for (Player player : players){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += player.getZoneAtkGoals(i + 1);
                team_zoneShots[i] += player.getZoneAtkShots(i + 1);

                int effectiveness = Math.round(team_zoneGoals[i] / (float) team_zoneShots[i] * 100);

                if (team_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                    setColor(zone_stats[i], zone_goals[i], effectiveness);
                }else{
                    zone_stats[i].setText("---");
                    zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                    zone_stats[i].setTextColor(Color.parseColor("#000000"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
                }

                zone_goals[i].setText(String.valueOf(team_zoneGoals[i]) +  "/" + String.valueOf(team_zoneShots[i]));



            }
        }
    }

    private void refreshCAStatistics(TextView zone_stats[], TextView zone_goals[], LinkedList<Player> players,  LinkedList<Goalkeeper> gks){

        int team_zoneGoals[] = new int[9];
        int team_zoneShots[] = new int[9];

        for (Goalkeeper goalkeeper : gks){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += goalkeeper.getZoneCAGoals(i + 1);
                team_zoneShots[i] += goalkeeper.getZoneCAShots(i + 1);
            }
        }

        for (Player player : players){

            for (int i = 0; i < 9; i++){

                team_zoneGoals[i] += player.getZoneCAGoals(i + 1);
                team_zoneShots[i] += player.getZoneCAShots(i + 1);

                int effectiveness = Math.round(team_zoneGoals[i] / (float) team_zoneShots[i] * 100);

                if (team_zoneShots[i] !=0){
                    zone_stats[i].setText(String.valueOf(effectiveness) + "%");
                    setColor(zone_stats[i], zone_goals[i], effectiveness);
                }else{
                    zone_stats[i].setText("---");
                    zone_stats[i].setBackgroundColor(Color.parseColor("#959595"));
                    zone_stats[i].setTextColor(Color.parseColor("#000000"));
                    zone_goals[i].setBackgroundColor(Color.parseColor("#959595"));
                }

                zone_goals[i].setText(team_zoneGoals[i] +  "/" + team_zoneShots[i]);



            }
        }
    }

    private void setColor(TextView stats_zones, TextView stats_goals, int effectiveness){
        if (effectiveness < 25){

            stats_zones.setTextColor(Color.parseColor("#000000"));
            stats_zones.setBackgroundColor(Color.parseColor("#cc0000"));
            stats_goals.setBackgroundColor(Color.parseColor("#cc0000"));
        } else if (effectiveness < 50){

            stats_zones.setTextColor(Color.parseColor("#000000"));
            stats_zones.setBackgroundColor(Color.parseColor("#ff9705"));
            stats_goals.setBackgroundColor(Color.parseColor("#ff9705"));
        } else if (effectiveness < 75){

            stats_zones.setTextColor(Color.parseColor("#000000"));
            stats_zones.setBackgroundColor(Color.parseColor("#fffcff00"));
            stats_goals.setBackgroundColor(Color.parseColor("#fffcff00"));
        } else if (effectiveness < 101){

            stats_zones.setTextColor(Color.parseColor("#000000"));
            stats_zones.setBackgroundColor(Color.parseColor("#ff01ff00"));
            stats_goals.setBackgroundColor(Color.parseColor("#ff01ff00"));
        }
    }

    protected void onResume()
    {
        super.onResume();

    }

    protected void onRestart(){
        super.onRestart();
    }


}
