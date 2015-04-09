package pt.ipleiria.estg.GEDD;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;

import android.os.Handler;

import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;


public class MainActivity extends ActionBarActivity {

    DBAdapter database;
    boolean isStart = false;
    int seconds = 0;
    int minutes = 0;


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

        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imgbtn_play);
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


        final Button btn_zone_1 = (Button) findViewById(R.id.btn_zone_1);
        final Button btn_zone_2 = (Button) findViewById(R.id.btn_zone_2);
        final Button btn_zone_3 = (Button) findViewById(R.id.btn_zone_3);
        final Button btn_zone_4 = (Button) findViewById(R.id.btn_zone_4);
        final Button btn_zone_5 = (Button) findViewById(R.id.btn_zone_5);
        final Button btn_zone_6 = (Button) findViewById(R.id.btn_zone_6);
        final Button btn_zone_7 = (Button) findViewById(R.id.btn_zone_7);
        final Button btn_zone_8 = (Button) findViewById(R.id.btn_zone_8);
        final Button btn_zone_9 = (Button) findViewById(R.id.btn_zone_9);

        final Button btn_gk_goal = (Button) findViewById(R.id.btn_gk_goal);
        final Button btn_gk_def = (Button) findViewById(R.id.btn_gk_def);
        final Button btn_gk_post = (Button) findViewById(R.id.btn_gk_post);
        final Button btn_gk_out = (Button) findViewById(R.id.btn_gk_out);

        final ImageButton btn_player1 = (ImageButton) findViewById(R.id.imgbtn_player1);
        final ImageButton btn_player2 = (ImageButton) findViewById(R.id.imgbtn_player2);
        final ImageButton btn_player3 = (ImageButton) findViewById(R.id.imgbtn_player3);
        final ImageButton btn_player4 = (ImageButton) findViewById(R.id.imgbtn_player4);
        final ImageButton btn_player5 = (ImageButton) findViewById(R.id.imgbtn_player5);
        final ImageButton btn_player6 = (ImageButton) findViewById(R.id.imgbtn_player6);
        final ImageButton btn_goalkeeper1 = (ImageButton) findViewById(R.id.imgbtn_goalkeeper1);

        TextView lbl_player1 = (TextView) findViewById(R.id.lbl_player1);
        TextView lbl_player2 = (TextView) findViewById(R.id.lbl_player2);
        TextView lbl_player3 = (TextView) findViewById(R.id.lbl_player3);
        TextView lbl_player4 = (TextView) findViewById(R.id.lbl_player4);
        TextView lbl_player5 = (TextView) findViewById(R.id.lbl_player5);
        TextView lbl_player6 = (TextView) findViewById(R.id.lbl_player6);
        TextView lbl_goalkeeper1 = (TextView) findViewById(R.id.lbl_gk1);

        final Button btn_ca = (Button) findViewById(R.id.btn_ca);
        final Button btn_6m = (Button) findViewById(R.id.btn_6m);
        final Button btn_7m = (Button) findViewById(R.id.btn_7m);
        final Button btn_9m = (Button) findViewById(R.id.btn_9m);

        final Button btn_goal = (Button) findViewById(R.id.btn_goal);
        final Button btn_goalpost = (Button) findViewById(R.id.btn_goalpost);
        final Button btn_block_atk = (Button) findViewById(R.id.btn_block_atk);
        final Button btn_out = (Button) findViewById(R.id.btn_out);
        final Button btn_defense = (Button) findViewById(R.id.btn_defense);

        final Button btn_block_def = (Button) findViewById(R.id.btn_block_def);
        final Button btn_disarm = (Button) findViewById(R.id.btn_unarm);
        final Button btn_interception = (Button) findViewById(R.id.btn_interception);

        final Button btn_assist = (Button) findViewById(R.id.btn_assistance);
        final Button btn_tf = (Button) findViewById(R.id.btn_tf);

        final Button btn_b1 = (Button) findViewById(R.id.btn_b1);
        final Button btn_b2 = (Button) findViewById(R.id.btn_b2);
        final Button btn_b3 = (Button) findViewById(R.id.btn_b3);
        final Button btn_b4 = (Button) findViewById(R.id.btn_b4);
        final Button btn_b5 = (Button) findViewById(R.id.btn_b5);
        final Button btn_b6 = (Button) findViewById(R.id.btn_b6);
        final Button btn_b7 = (Button) findViewById(R.id.btn_b7);
        final Button btn_b8 = (Button) findViewById(R.id.btn_b8);
        final Button btn_b9 = (Button) findViewById(R.id.btn_b9);

        final ImageButton btn_discipline = (ImageButton) findViewById(R.id.imgbtn_cards);

        String player1Number = lbl_player1.getText().toString();
        String player2Number = lbl_player2.getText().toString();
        String player3Number = lbl_player3.getText().toString();
        String player4Number = lbl_player4.getText().toString();
        String player5Number = lbl_player5.getText().toString();
        String player6Number = lbl_player6.getText().toString();
        String goalkeeper1Number = lbl_goalkeeper1.getText().toString();

        final TextView lastAction = (TextView) findViewById(R.id.lastAction);

        final RelativeLayout zones = (RelativeLayout) findViewById(R.id.zones);
        final RelativeLayout finalization = (RelativeLayout) findViewById(R.id.finalization);
        final RelativeLayout offensiveAction = (RelativeLayout) findViewById(R.id.offensiveAction);
        final RelativeLayout defensiveAction = (RelativeLayout) findViewById(R.id.defensiveAction);
        final RelativeLayout teamPlayer = (RelativeLayout) findViewById(R.id.players);
        final RelativeLayout goalkeeperZone = (RelativeLayout) findViewById(R.id.goalLayout);
        final RelativeLayout goalkeeperAction = (RelativeLayout) findViewById(R.id.goalkeeperActions);

        final TextView time = (TextView) findViewById(R.id.time);

        final Player player1 = new Player(Integer.parseInt(player1Number));
        final Player player2 = new Player(Integer.parseInt(player2Number));
        final Player player3 = new Player(Integer.parseInt(player3Number));
        final Player player4 = new Player(Integer.parseInt(player4Number));
        final Player player5 = new Player(Integer.parseInt(player5Number));
        final Player player6 = new Player(Integer.parseInt(player6Number));
        final Goalkeeper goalkeeper1 = new Goalkeeper(Integer.parseInt(goalkeeper1Number));

        final LinkedList<Player> players = new LinkedList();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);

        //btn_discipline.setEnabled(false);

        try {
            FileInputStream fin = openFileInput("mydata");
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            lastAction.setText(temp);
            /* A toast foi so pra testar */
            Toast.makeText(getBaseContext(), "file read", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

        }


        final View.OnTouchListener zoneTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!(v.isPressed())) {

                        ViewGroup container = (ViewGroup) v.getParent();
                        for (int i = 0; i < container.getChildCount(); i++) {
                            container.getChildAt(i).setPressed(false);
                        }
                        v.setPressed(true);
                        Player player;
                        Goalkeeper goalkeeper;

                        //verifica se é botão de jogador, se for verdade atualiza os campos
                        if (v.getParent() == (RelativeLayout) teamPlayer) {
                            Player tempPlayer;
                            if ((tempPlayer = getPlayerPressed(players, teamPlayer)) != null) {
                                refreshAttackStatistics(btn_tf, btn_assist, btn_ca, btn_6m, btn_7m, btn_9m, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, tempPlayer);
                                refreshDefensiveStatistics(btn_block_def, btn_disarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, tempPlayer);

                                //btn_discipline.setEnabled(true);
                            }

                            btn_b1.setPressed(false);
                            btn_b2.setPressed(false);
                            btn_b3.setPressed(false);
                            btn_b4.setPressed(false);
                            btn_b5.setPressed(false);
                            btn_b6.setPressed(false);
                            btn_b7.setPressed(false);
                            btn_b8.setPressed(false);
                            btn_b9.setPressed(false);
                            btn_gk_goal.setPressed(false);
                            btn_gk_def.setPressed(false);
                            btn_gk_out.setPressed(false);
                            btn_gk_post.setPressed(false);
                        }

                        if ((v.getParent() == (RelativeLayout) offensiveAction) || (v.getParent() == (RelativeLayout) finalization)){
                            btn_block_def.setPressed(false);
                            btn_disarm.setPressed(false);
                            btn_interception.setPressed(false);
                            btn_b1.setPressed(false);
                            btn_b2.setPressed(false);
                            btn_b3.setPressed(false);
                            btn_b4.setPressed(false);
                            btn_b5.setPressed(false);
                            btn_b6.setPressed(false);
                            btn_b7.setPressed(false);
                            btn_b8.setPressed(false);
                            btn_b9.setPressed(false);
                            btn_gk_goal.setPressed(false);
                            btn_gk_def.setPressed(false);
                            btn_gk_out.setPressed(false);
                            btn_gk_post.setPressed(false);
                        }

                        if (v.getParent() == (RelativeLayout) defensiveAction){
                            btn_6m.setPressed(false);
                            btn_7m.setPressed(false);
                            btn_9m.setPressed(false);
                            btn_ca.setPressed(false);
                            btn_goal.setPressed(false);
                            btn_defense.setPressed(false);
                            btn_out.setPressed(false);
                            btn_goalpost.setPressed(false);
                            btn_block_atk.setPressed(false);
                            btn_b1.setPressed(false);
                            btn_b2.setPressed(false);
                            btn_b3.setPressed(false);
                            btn_b4.setPressed(false);
                            btn_b5.setPressed(false);
                            btn_b6.setPressed(false);
                            btn_b7.setPressed(false);
                            btn_b8.setPressed(false);
                            btn_b9.setPressed(false);
                            btn_gk_goal.setPressed(false);
                            btn_gk_def.setPressed(false);
                            btn_gk_out.setPressed(false);
                            btn_gk_post.setPressed(false);
                        }

                        if (v.getParent() == (RelativeLayout) goalkeeperAction || v.getParent() == (RelativeLayout) goalkeeperZone){
                            btn_6m.setPressed(false);
                            btn_7m.setPressed(false);
                            btn_9m.setPressed(false);
                            btn_ca.setPressed(false);
                            btn_goal.setPressed(false);
                            btn_defense.setPressed(false);
                            btn_out.setPressed(false);
                            btn_goalpost.setPressed(false);
                            btn_block_atk.setPressed(false);
                            btn_block_def.setPressed(false);
                            btn_disarm.setPressed(false);
                            btn_interception.setPressed(false);
                            btn_player1.setPressed(false);
                            btn_player2.setPressed(false);
                            btn_player3.setPressed(false);
                            btn_player4.setPressed(false);
                            btn_player5.setPressed(false);
                            btn_player6.setPressed(false);
                        }

                        if ((goalkeeper = allPressedGoalkeeperAction(goalkeeperZone, goalkeeperAction, zones, goalkeeper1)) != null){
                            lastAction.setText(goalkeeper.getLastAction());
                        }

                        if ((player = allPressedOffensive(offensiveAction, finalization, zones, teamPlayer, players)) != null) {
                            refreshAttackStatistics(btn_tf, btn_assist, btn_ca, btn_6m, btn_7m, btn_9m, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                            lastAction.setText(player.getLastAction());

                            String saveData = player.getLastAction();

                            try {
                                FileOutputStream fOut = openFileOutput("mydata", MODE_WORLD_READABLE);
                                fOut.write(saveData.getBytes());
                                fOut.close();
                                Toast.makeText(getBaseContext(), "File saved",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        if ((player = pressedDiscipline(teamPlayer, players, btn_discipline)) != null){
                            showPopUpDiscipline(v, player);

                        }

                        if ((player = pressedAsist(teamPlayer, players, btn_assist)) != null) {
                            player.addAssistance();
                            refreshAttackStatistics(btn_tf, btn_assist, btn_ca, btn_6m, btn_7m, btn_9m, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                            refreshLabels(null, null, null, null, btn_assist, null, null, null);
                        }

                        if ((player = pressedTechFail(teamPlayer, players, btn_tf)) != null) {
                            player.addTechFail();
                            refreshAttackStatistics(btn_tf, btn_assist, btn_ca, btn_6m, btn_7m, btn_9m, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                            refreshLabels(null, null, null, null, null, btn_tf, null, null);
                        }


                        if ((player = allPressedDefensive(defensiveAction, zones, teamPlayer, players)) != null) {
                            refreshDefensiveStatistics(btn_block_def, btn_disarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                            lastAction.setText(player.getLastAction());

                            String saveData = player.getLastAction();

                            try {
                                FileOutputStream fOut = openFileOutput("mydata", MODE_WORLD_READABLE);
                                fOut.write(saveData.getBytes());
                                fOut.close();
                                Toast.makeText(getBaseContext(), "File saved",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    } else {
                        v.setPressed(false);
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

        btn_zone_1.setTag(1);
        btn_zone_2.setTag(2);
        btn_zone_3.setTag(3);
        btn_zone_4.setTag(4);
        btn_zone_5.setTag(5);
        btn_zone_6.setTag(6);
        btn_zone_7.setTag(7);
        btn_zone_8.setTag(8);
        btn_zone_9.setTag(9);

        btn_b1.setOnTouchListener(zoneTouchListener);
        btn_b2.setOnTouchListener(zoneTouchListener);
        btn_b3.setOnTouchListener(zoneTouchListener);
        btn_b4.setOnTouchListener(zoneTouchListener);
        btn_b5.setOnTouchListener(zoneTouchListener);
        btn_b6.setOnTouchListener(zoneTouchListener);
        btn_b7.setOnTouchListener(zoneTouchListener);
        btn_b8.setOnTouchListener(zoneTouchListener);
        btn_b9.setOnTouchListener(zoneTouchListener);

        btn_b1.setTag(1);
        btn_b2.setTag(2);
        btn_b3.setTag(3);
        btn_b4.setTag(4);
        btn_b5.setTag(5);
        btn_b6.setTag(6);
        btn_b7.setTag(7);
        btn_b8.setTag(8);
        btn_b9.setTag(9);

        btn_player1.setOnTouchListener(zoneTouchListener);
        btn_player2.setOnTouchListener(zoneTouchListener);
        btn_player3.setOnTouchListener(zoneTouchListener);
        btn_player4.setOnTouchListener(zoneTouchListener);
        btn_player5.setOnTouchListener(zoneTouchListener);
        btn_player6.setOnTouchListener(zoneTouchListener);

        btn_player1.setTag(player1.getNumber());
        btn_player2.setTag(player2.getNumber());
        btn_player3.setTag(player3.getNumber());
        btn_player4.setTag(player4.getNumber());
        btn_player5.setTag(player5.getNumber());
        btn_player6.setTag(player6.getNumber());


        btn_ca.setOnTouchListener(zoneTouchListener);
        btn_6m.setOnTouchListener(zoneTouchListener);
        btn_7m.setOnTouchListener(zoneTouchListener);
        btn_9m.setOnTouchListener(zoneTouchListener);

        btn_ca.setTag("btn_ca");
        btn_6m.setTag("btn_6m");
        btn_7m.setTag("btn_7m");
        btn_9m.setTag("btn_9m");

        btn_goal.setOnTouchListener(zoneTouchListener);
        btn_goalpost.setOnTouchListener(zoneTouchListener);
        btn_out.setOnTouchListener(zoneTouchListener);
        btn_defense.setOnTouchListener(zoneTouchListener);
        btn_block_atk.setOnTouchListener(zoneTouchListener);

        btn_goal.setTag("btn_goal");
        btn_goalpost.setTag("btn_goalpost");
        btn_out.setTag("btn_out");
        btn_defense.setTag("btn_defense");
        btn_block_atk.setTag("btn_block_atk");

        btn_block_def.setOnTouchListener(zoneTouchListener);
        btn_disarm.setOnTouchListener(zoneTouchListener);
        btn_interception.setOnTouchListener(zoneTouchListener);

        btn_block_def.setTag("btn_block_def");
        btn_disarm.setTag("btn_disarm");
        btn_interception.setTag("btn_interception");

        btn_tf.setOnTouchListener(zoneTouchListener);
        btn_assist.setOnTouchListener(zoneTouchListener);

        btn_gk_goal.setTag("btn_gk_goal");
        btn_gk_def.setTag("btn_gk_def");
        btn_gk_out.setTag("btn_gk_out");
        btn_gk_post.setTag("btn_gk_post");

        btn_gk_goal.setOnTouchListener(zoneTouchListener);
        btn_gk_def.setOnTouchListener(zoneTouchListener);
        btn_gk_post.setOnTouchListener(zoneTouchListener);
        btn_gk_out.setOnTouchListener(zoneTouchListener);

        btn_discipline.setOnTouchListener(zoneTouchListener);

        final ImageButton start = (ImageButton) findViewById(R.id.imgbtn_play);

        View.OnClickListener stopWatchListener =
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (isStart == false) {
                            isStart = true;
                            start.setImageResource(R.drawable.pause);
                        } else {
                            isStart = false;
                            start.setImageResource(R.drawable.play);
                        }
                    }
                };

        start.setOnClickListener(stopWatchListener);

        final Handler handler;

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                if (isStart) {
                    seconds++;
                    if (seconds == 60) {
                        seconds = 0;
                        minutes++;
                    }

                    if(seconds < 10 && minutes >= 10){
                        time.setText(minutes + ":" +'0'+seconds);
                    }

                    if(minutes < 10 && seconds >= 10){
                        time.setText("0"+minutes + ":" +seconds);
                    }

                    if(minutes < 10 && seconds < 10){
                        time.setText("0"+minutes + ":" +'0'+seconds);
                    }

                    if(minutes >= 10 && seconds >= 10){
                        time.setText(minutes + ":" +seconds);
                    }


                }
                handler.postDelayed(this, 1000);





            }
        };

        handler.postDelayed(r, 1000);
    }



    public void showPopUpDiscipline (View view, final Player player){



        PopupMenu popupMenu = new PopupMenu(this, view);

        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.popup_discipline, popupMenu.getMenu());
        popupMenu.show();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()){
                    case R.id.btn_yellowCard:
                        player.setYellowCard();

                        return true;
                    case R.id.btn_redCard:
                        //player.isRedCard();
                        //item.setEnabled(false);
                        return true;
                    case R.id.btn_2min:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void onStartButtonClick(){
            isStart=true;
        }

        private void onStopButtonClick(){
            isStart=false;
        }


    private Player pressedDiscipline(RelativeLayout teamPlayer, LinkedList<Player> players, ImageButton btn_discipline) {
        Player player;
        if(btn_discipline.isPressed() && (player = getPlayerPressed(players, teamPlayer)) != null){

            return player;
        }
        return null;
    }

    private Player pressedAsist(RelativeLayout teamPlayer, LinkedList<Player> players, Button btn_assist) {
        Player player;
        if(btn_assist.isPressed() && (player = getPlayerPressed(players, teamPlayer)) != null){
            return player;
        }
        return null;
    }

    private Player pressedTechFail(RelativeLayout teamPlayer, LinkedList<Player> players, Button btn_tf) {
        Player player;
        if(btn_tf.isPressed() && (player = getPlayerPressed(players, teamPlayer)) != null){
            return player;
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

    private ImageButton isChildrenImgButtonPressed(RelativeLayout container){
        for (int i=0 ; i < container.getChildCount(); i++){
            if(container.getChildAt(i).isPressed() == true)
                return (ImageButton) container.getChildAt(i);
        }
        return null;
    }

    private Goalkeeper allPressedGoalkeeperAction(RelativeLayout goalkeeperZone, RelativeLayout goalkeeperAction, RelativeLayout zones, Goalkeeper goalkeeper) {

        Button btnGkZone;
        Button btnGkAction;
        Button btnZone;

        if ((btnGkZone = isChildrenButtonPressed(goalkeeperZone)) != null && (btnGkAction = isChildrenButtonPressed(goalkeeperAction)) != null && (btnZone = isChildrenButtonPressed(zones))!= null) {

            goalkeeper.setLastAction("Guarda-Redes - " + btnGkAction.getTag().toString() + "\n Zona de Baliza " + btnGkZone.getTag().toString() + ", Zona " + btnZone.getTag().toString());

            if (btnGkAction.getTag() == "btn_gk_goal") {
                goalkeeper.setGoal((int) btnGkZone.getTag(), (int) btnZone.getTag());
            } else if (btnGkAction.getTag() == "btn_gk_def") {
                goalkeeper.setDefended((int) btnGkZone.getTag(), (int) btnZone.getTag());
            } else if (btnGkAction.getTag() == "btn_gk_post") {
                goalkeeper.setPost((int) btnZone.getTag());
            } else if (btnGkAction.getTag() == "btn_gk_out") {
                goalkeeper.setOut((int) btnZone.getTag());
            }

            refreshLabels(null, null, null, btnZone, null, null, btnGkAction, btnGkZone);

            return goalkeeper;
        }

        return null;
    }

    private Player allPressedOffensive(RelativeLayout offensiveAction, RelativeLayout finalization, RelativeLayout zones, RelativeLayout teamPlayer, LinkedList<Player> players){

        Button btnOffAct;
        Button btnFinalization;
        Button btnZone;
        ImageButton btnPlayer;

        if((btnOffAct = isChildrenButtonPressed(offensiveAction)) != null && (btnFinalization = isChildrenButtonPressed(finalization)) != null && (btnZone = isChildrenButtonPressed(zones))!= null && (btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null){

            for(Player player : players){
                if(player.getNumber()==(int) btnPlayer.getTag()){
                    player.setLastAction("Jogador " + btnPlayer.getTag().toString() + " - " + btnOffAct.getTag().toString() + "\n" + btnFinalization.getTag().toString() + ", Zona " + btnZone.getTag().toString());
                    player.refreshPlayerStats(btnFinalization.getTag().toString(), (int) btnZone.getTag(), btnOffAct.getTag().toString());
                    refreshLabels(btnOffAct, null, btnFinalization, btnZone, null, null, null, null);

                    return player;
                }
            }
        }
        return null;
    }



    private Player getPlayerPressed(LinkedList<Player> players, RelativeLayout teamPlayer) {
        ImageButton btnPlayer;
        if ((btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null) {
            for (Player player : players) {
                if (player.getNumber() == (int) btnPlayer.getTag()) {
                    return player;
                }
            }
        }
        return null;
    }

    private Player allPressedDefensive(RelativeLayout defensiveAction, RelativeLayout zones, RelativeLayout teamPlayer, LinkedList<Player> players){
        Button btnDefAct;
        Button btnZone;
        ImageButton btnPlayer;

        if((btnDefAct = isChildrenButtonPressed(defensiveAction)) != null && (btnZone = isChildrenButtonPressed(zones))!= null && (btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null){

            for(Player player : players){
                if(player.getNumber()==(int) btnPlayer.getTag()){
                    player.setLastAction("Jogador " + btnPlayer.getTag().toString() + "\n" + btnDefAct.getTag().toString() + ", Zona " + btnZone.getTag().toString());
                    if (btnDefAct.getTag() == "btn_block_def"){
                        player.setBlock((int) btnZone.getTag());
                    } else if(btnDefAct.getTag() == "btn_disarm") {
                        player.setDisarm((int) btnZone.getTag());
                    } else if(btnDefAct.getTag() == "btn_interception"){
                        player.setInterception((int) btnZone.getTag());
                    }
                    refreshLabels(null, btnDefAct, null, btnZone, null, null, null, null);

                    return player;
                }
            }
        }
        return null;
    }

    private void refreshAttackStatistics(Button btn_tf, Button btn_assist, Button btn_ca, Button btn_6m, Button btn_7m, Button btn_9m, Button btn_goal, Button btn_out, Button btn_atk_block, Button btn_goalpost, Button btn_defense, Button btn_zone_1, Button btn_zone_2, Button btn_zone_3, Button btn_zone_4, Button btn_zone_5, Button btn_zone_6, Button btn_zone_7, Button btn_zone_8, Button btn_zone_9 , Player player){
        btn_ca.setText("Contra Ataque - "+player.getAllActions(player.getCaShotGoal())+"/"+player.getAllCaShots());
        btn_6m.setText("6 metros - "+player.getAllActions(player.getSixShotGoal())+"/"+player.getAllSixShots());
        btn_7m.setText("7 metros - "+player.getAllActions(player.getSevenShotGoal())+"/"+player.getAllSevenShots());
        btn_9m.setText("9 metros - "+player.getAllActions(player.getNineShotGoal())+"/"+player.getAllNineShots());

        btn_zone_1.setText("Zona 1 - "+player.getZoneGoals(1)+"/"+player.getZoneShots(1));
        btn_zone_2.setText("Zona 2 - "+player.getZoneGoals(2)+"/"+player.getZoneShots(2));
        btn_zone_3.setText("Zona 3 - "+player.getZoneGoals(3)+"/"+player.getZoneShots(3));
        btn_zone_4.setText("Zona 4 - "+player.getZoneGoals(4)+"/"+player.getZoneShots(4));
        btn_zone_5.setText("Zona 5 - "+player.getZoneGoals(5)+"/"+player.getZoneShots(5));
        btn_zone_6.setText("Zona 6 - "+player.getZoneGoals(6)+"/"+player.getZoneShots(6));
        btn_zone_7.setText("Zona 7 - "+player.getZoneGoals(7)+"/"+player.getZoneShots(7));
        btn_zone_8.setText("Zona 8 - "+player.getZoneGoals(8)+"/"+player.getZoneShots(8));
        btn_zone_9.setText("Zona 9 - "+player.getZoneGoals(9)+"/"+player.getZoneShots(9));

        btn_goal.setText("Golo - " + player.getAllShotGoals());
        btn_goalpost.setText("Poste - " + player.getAllShotPost());
        btn_out.setText("Fora - " + player.getAllShotOut());
        btn_atk_block.setText("Bloco - " + player.getAllBlocked());
        btn_defense.setText("Defesa - " + player.getAllShotDefended());

        btn_assist.setText("Assistência - "+player.getAssistance());

        btn_tf.setText("Falha Técnica - "+player.getTechnicalFailure());

    }

    private void refreshDefensiveStatistics(Button btn_def_block, Button btn_disarm, Button btn_interception,Button btn_zone_1, Button btn_zone_2, Button btn_zone_3, Button btn_zone_4, Button btn_zone_5, Button btn_zone_6, Button btn_zone_7, Button btn_zone_8, Button btn_zone_9 , Player player){

        /* Não se têm de distinguir nas estatiticas das zonas as acções defensivas das ofensivas?*/

        btn_zone_1.setText("Zona 1 - "+player.getZoneGoals(1)+"/"+player.getZoneShots(1));
        btn_zone_2.setText("Zona 2 - "+player.getZoneGoals(2)+"/"+player.getZoneShots(2));
        btn_zone_3.setText("Zona 3 - "+player.getZoneGoals(3)+"/"+player.getZoneShots(3));
        btn_zone_4.setText("Zona 4 - "+player.getZoneGoals(4)+"/"+player.getZoneShots(4));
        btn_zone_5.setText("Zona 5 - "+player.getZoneGoals(5)+"/"+player.getZoneShots(5));
        btn_zone_6.setText("Zona 6 - "+player.getZoneGoals(6)+"/"+player.getZoneShots(6));
        btn_zone_7.setText("Zona 7 - "+player.getZoneGoals(7)+"/"+player.getZoneShots(7));
        btn_zone_8.setText("Zona 8 - "+player.getZoneGoals(8)+"/"+player.getZoneShots(8));
        btn_zone_9.setText("Zona 9 - "+player.getZoneGoals(9)+"/"+player.getZoneShots(9));

        btn_def_block.setText("Bloco - " + player.getAllBlocked());
        btn_disarm.setText("Desarme - " + player.getAllDisarms());
        btn_interception.setText("Interceção - " + player.getAllInterceptions());

    }

    private void refreshLabels(Button btnOffAct, Button btnDefAct, Button btnFinalization, Button btnZone, Button btnAssist, Button btnTecFail, Button btnGKAction, Button btnGKZone){

        if (btnOffAct != null) {
            btnOffAct.setPressed(false);
        }
        if (btnDefAct != null) {
            btnDefAct.setPressed(false);
        }

        if (btnFinalization != null) {
            btnFinalization.setPressed(false);
        }

        if(btnAssist != null)
             btnAssist.setPressed(false);

        if(btnTecFail != null)
            btnTecFail.setPressed(false);

        if(btnZone != null)
            btnZone.setPressed(false);

        if(btnGKAction != null)
            btnGKAction.setPressed(false);

        if(btnGKZone != null)
            btnGKZone.setPressed(false);

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
