package pt.ipleiria.estg.GEDD;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

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

        final ImageButton btn_player1 = (ImageButton) findViewById(R.id.imgbtn_player1);
        final ImageButton btn_player2 = (ImageButton) findViewById(R.id.imgbtn_player2);
        final ImageButton btn_player3 = (ImageButton) findViewById(R.id.imgbtn_player3);
        final ImageButton btn_player4 = (ImageButton) findViewById(R.id.imgbtn_player4);
        final ImageButton btn_player5 = (ImageButton) findViewById(R.id.imgbtn_player5);
        final ImageButton btn_player6 = (ImageButton) findViewById(R.id.imgbtn_player6);

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
        final Button btn_unarm = (Button) findViewById(R.id.btn_unarm);
        final Button btn_interception = (Button) findViewById(R.id.btn_interception);

        String player1Number = lbl_player1.getText().toString();
        String player2Number = lbl_player2.getText().toString();
        String player3Number = lbl_player3.getText().toString();
        String player4Number = lbl_player4.getText().toString();
        String player5Number = lbl_player5.getText().toString();
        String player6Number = lbl_player6.getText().toString();

        final TextView lastAction = (TextView) findViewById(R.id.lastAction);

        final RelativeLayout zones = (RelativeLayout) findViewById(R.id.zones);
        final RelativeLayout finalization = (RelativeLayout) findViewById(R.id.finalization);
        final RelativeLayout offensiveAction = (RelativeLayout) findViewById(R.id.offensiveAction);
        final RelativeLayout defensiveAction = (RelativeLayout) findViewById(R.id.defensiveAction);
        final RelativeLayout teamPlayer = (RelativeLayout) findViewById(R.id.players);

        final Player player1 = new Player(Integer.parseInt(player1Number));
        final Player player2 = new Player(Integer.parseInt(player2Number));
        final Player player3 = new Player(Integer.parseInt(player3Number));
        final Player player4 = new Player(Integer.parseInt(player4Number));
        final Player player5 = new Player(Integer.parseInt(player5Number));
        final Player player6 = new Player(Integer.parseInt(player6Number));

        final LinkedList<Player> players = new LinkedList();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);

        try{
            FileInputStream fin = openFileInput("mydata");
            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            lastAction.setText(temp);
            /* A toast foi so pra testar */ Toast.makeText(getBaseContext(),"file read", Toast.LENGTH_SHORT).show();

        }catch(Exception e){

        }


        final View.OnTouchListener zoneTouchListener = new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(!(v.isPressed())) {

                        ViewGroup container = (ViewGroup) v.getParent();
                        for (int i=0 ; i < container.getChildCount(); i++){
                            container.getChildAt(i).setPressed(false);
                        }
                        v.setPressed(true);
                        Player player;

                        //verifica se é botão de jogador, se for verdade atualiza os campos
                        if(v.getParent() == (RelativeLayout) teamPlayer){
                            Player tempPlayer;
                            if((tempPlayer = isPlayerPressed(players, teamPlayer)) != null){
                                refreshAttackStatistics(btn_ca, btn_6m, btn_7m, btn_9m, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, tempPlayer);
                                refreshDefensiveStatistics(btn_block_def, btn_unarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, tempPlayer);

                            }
                         }

                        if ((player = allPressedOffensive(offensiveAction, finalization, zones, teamPlayer, players)) != null){
                            refreshAttackStatistics(btn_ca, btn_6m, btn_7m, btn_9m, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                            lastAction.setText(player.getTeste());

                            String saveData = player.getTeste();

                            try {
                                FileOutputStream fOut = openFileOutput("mydata" ,MODE_WORLD_READABLE);
                                fOut.write(saveData.getBytes());
                                fOut.close();
                                Toast.makeText(getBaseContext(),"File saved",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        if ((player = allPressedDefensive(defensiveAction, zones, teamPlayer, players)) != null){
                            refreshDefensiveStatistics(btn_block_def, btn_unarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                            lastAction.setText(player.getTeste());

                            String saveData = player.getTeste();

                            try {
                                FileOutputStream fOut = openFileOutput("mydata" ,MODE_WORLD_READABLE);
                                fOut.write(saveData.getBytes());
                                fOut.close();
                                Toast.makeText(getBaseContext(),"File saved",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }else{
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
        btn_unarm.setOnTouchListener(zoneTouchListener);
        btn_interception.setOnTouchListener(zoneTouchListener);

        btn_block_def.setTag("btn_block_def");
        btn_unarm.setTag("btn_unarm");
        btn_interception.setTag("btn_interception");

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

    private Player allPressedOffensive(RelativeLayout offensiveAction, RelativeLayout finalization, RelativeLayout zones, RelativeLayout teamPlayer, LinkedList<Player> players){
        Button btnOffAct;
        Button btnFinalization;
        Button btnZone;
        ImageButton btnPlayer;

        if((btnOffAct = isChildrenButtonPressed(offensiveAction)) != null && (btnFinalization = isChildrenButtonPressed(finalization)) != null && (btnZone = isChildrenButtonPressed(zones))!= null && (btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null){

            for(Player player : players){
                if(player.getNumber()==(int) btnPlayer.getTag()){
                    player.setTeste("Jogador " + btnPlayer.getTag().toString() + " - " + btnOffAct.getTag().toString() + "\n" + btnFinalization.getTag().toString() + ", Zona " + btnZone.getTag().toString());
                    player.refreshPlayerStats(btnFinalization.getTag().toString(), (int) btnZone.getTag(), btnOffAct.getTag().toString());
                    refreshLabels(btnOffAct, null, btnFinalization, btnZone, btnPlayer);

                    return player;
                }
            }
        }
        return null;
    }

    private Player isPlayerPressed(LinkedList<Player> players, RelativeLayout teamPlayer) {
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
                    player.setTeste("Jogador " + btnPlayer.getTag().toString() + "\n" + btnDefAct.getTag().toString() + "Zona " + btnZone.getTag().toString());
                    refreshLabels(null, btnDefAct, null, btnZone, btnPlayer);

                    return player;
                }
            }
        }
        return null;
    }

    private void refreshAttackStatistics(Button btn_ca, Button btn_6m, Button btn_7m, Button btn_9m, Button btn_goal, Button btn_out, Button btn_atk_block, Button btn_goalpost, Button btn_defense, Button btn_zone_1, Button btn_zone_2, Button btn_zone_3, Button btn_zone_4, Button btn_zone_5, Button btn_zone_6, Button btn_zone_7, Button btn_zone_8, Button btn_zone_9 , Player player){
        btn_ca.setText("Contra Ataque - "+player.getAllShots(player.getCaShotGoal())+"/"+player.getAllCaShots());
        btn_6m.setText("6 metros - "+player.getAllShots(player.getSixShotGoal())+"/"+player.getAllSixShots());
        btn_7m.setText("7 metros - "+player.getAllShots(player.getSevenShotGoal())+"/"+player.getAllSevenShots());
        btn_9m.setText("9 metros - "+player.getAllShots(player.getNineShotGoal())+"/"+player.getAllNineShots());

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

    }

    private void refreshDefensiveStatistics(Button btn_def_block, Button btn_unarm, Button btn_interception,Button btn_zone_1, Button btn_zone_2, Button btn_zone_3, Button btn_zone_4, Button btn_zone_5, Button btn_zone_6, Button btn_zone_7, Button btn_zone_8, Button btn_zone_9 , Player player){

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

        btn_def_block.setText("Bloco - " + player.getBlock());
        btn_unarm.setText("Desarme - " + player.getDisarm());
        btn_interception.setText("Interceção - " + player.getInterception());

    }

    private void refreshLabels(Button btnOffAct, Button btnDefAct, Button btnFinalization, Button btnZone, ImageButton btn_player){

        if (btnOffAct != null) {
            btnOffAct.setPressed(false);
        }
        if (btnDefAct != null) {
            btnDefAct.setPressed(false);
        }

        if (btnFinalization != null) {
            btnFinalization.setPressed(false);
        }

        btnZone.setPressed(false);
        btn_player.setPressed(false);
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
