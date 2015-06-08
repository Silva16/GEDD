package pt.ipleiria.estg.GEDD;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import android.os.Handler;

import org.json.JSONObject;

import pt.ipleiria.estg.GEDD.Models.Game;
import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;


public class GameActivity extends CustomActionBarActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    boolean isStart = false;
    int seconds = 0;
    int minutes = 0;
    String opponentName = "";
    LinkedList<Player> players = new LinkedList<Player>();
    LinkedList<Goalkeeper> gks = new LinkedList<Goalkeeper>();
    LinkedList<Player> players2min = new LinkedList<Player>();
    Game game;
    Goalkeeper goalkeeper1;
    TextView lbl_goalkeeper1;
    ImageButton btn_goalkeeper1;
    Boolean existFile = false;
    Button btn_tf_adv;
    TextView lbl_opponent;
    TextView lbl_myTeam;
    ImageButton btn_players[] = new ImageButton[6];
    Boolean playable = false;
    ArrayList<Game> games;
    Boolean save = true;
    int RESULT_FINISH = 3;


    private static final String TAG = "main activity";
    private ViewPager mPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        /*mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (IconPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);*/

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

        /*final ImageButton btn_player1 = (ImageButton) findViewById(R.id.imgbtn_player1);
        final ImageButton btn_player2 = (ImageButton) findViewById(R.id.imgbtn_player2);
        final ImageButton btn_player3 = (ImageButton) findViewById(R.id.imgbtn_player3);
        final ImageButton btn_player4 = (ImageButton) findViewById(R.id.imgbtn_player4);
        final ImageButton btn_player5 = (ImageButton) findViewById(R.id.imgbtn_player5);
        final ImageButton btn_player6 = (ImageButton) findViewById(R.id.imgbtn_player6);*/
        btn_goalkeeper1 = (ImageButton) findViewById(R.id.imgbtn_goalkeeper1);



        lbl_goalkeeper1 = (TextView) findViewById(R.id.lbl_gk1);
        lbl_opponent = (TextView) findViewById(R.id.opponent);

        final TextView lbl_scoreMyTeam = (TextView) findViewById(R.id.scoreMyTeam);
        final TextView lbl_scoreOpponent = (TextView) findViewById(R.id.scoreOpponent);
        lbl_myTeam = (TextView) findViewById(R.id.myTeam);
        final TextView time = (TextView) findViewById(R.id.time);


        final Button btn_ca = (Button) findViewById(R.id.btn_ca);
        final Button btn_atk = (Button) findViewById(R.id.btn_attack);

        final Button btn_goal = (Button) findViewById(R.id.btn_goal);
        final Button btn_goalpost = (Button) findViewById(R.id.btn_goalpost);
        final Button btn_block_atk = (Button) findViewById(R.id.btn_block_atk);
        final Button btn_out = (Button) findViewById(R.id.btn_out);
        final Button btn_defense = (Button) findViewById(R.id.btn_defense);

        final Button btn_block_def = (Button) findViewById(R.id.btn_block_def);
        final Button btn_disarm = (Button) findViewById(R.id.btn_unarm);
        final Button btn_interception = (Button) findViewById(R.id.btn_interception);

        final Button btn_assist = (Button) findViewById(R.id.btn_assistance);
        final Button btn_ft = (Button) findViewById(R.id.btn_ft);

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
        final ImageButton btn_subs = (ImageButton) findViewById(R.id.imgbtn_subs);

        btn_tf_adv = (Button) findViewById(R.id.btn_ft_adv);



        /*if(readSerializable()){

            players = game.getPlayers();


        }else{
            createGame();

        }*/

        String str = getIntent().getStringExtra("type");

        String id_btn;
        int k =1;
        for (int i = 0; i < 6; i++) {

            id_btn = "imgbtn_player" + k;
            btn_players[i] = (ImageButton) findViewById(getResources().getIdentifier(id_btn, "id", getPackageName()));
            k++;
        }

        games = readSerializable();
        if(games == null){
            games = new ArrayList<Game>();
        }

        if(str.contentEquals("new")){
            createGame();
            associatePlayersToButton();
            games.add(game);

        }else{
            //Game game = readSerializable();
            game = (Game) getIntent().getSerializableExtra("game");
            players = game.getPlayers();
            gks = game.getGks();
            btn_tf_adv.setText("Falha Técnica Adversária "+game.getTechnicalFailAdv());

            lbl_scoreMyTeam.setText(String.valueOf(game.getScoreMyTeam()));
            lbl_scoreOpponent.setText(String.valueOf(game.getScoreOpponent()));
            seconds = game.getSeconds();
            minutes = game.getMinutes();
            setTime(time);

            refreshPlayerImage();


            Log.i("loaded", "carreguei");
            Toast.makeText(getApplicationContext(), "Jogo Carregado",
                    Toast.LENGTH_LONG).show();
        }

        if(players.size()>5 && gks.size()>0){
            playable = true;
        }

        lbl_myTeam.setText(game.getMyTeam());
        lbl_opponent.setText(game.getOpponent());






                final TextView lastAction = (TextView) findViewById(R.id.lastAction);

                final RelativeLayout zones = (RelativeLayout) findViewById(R.id.zones);
                final RelativeLayout finalization = (RelativeLayout) findViewById(R.id.finalization);
                final RelativeLayout offensiveAction = (RelativeLayout) findViewById(R.id.offensiveAction);
                final RelativeLayout defensiveAction = (RelativeLayout) findViewById(R.id.defensiveAction);
                final RelativeLayout otherAction = (RelativeLayout) findViewById(R.id.otherActions);
                final RelativeLayout teamPlayer = (RelativeLayout) findViewById(R.id.players);
                final RelativeLayout goalkeeperZone = (RelativeLayout) findViewById(R.id.goalLayout);
                final RelativeLayout goalkeeperAction = (RelativeLayout) findViewById(R.id.goalkeeperActions);
                final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainActivityLayout);



                //btn_discipline.setEnabled(false);

                try {
                    FileInputStream fin = openFileInput("mydata");
                    int c;
                    String temp = "";
                    while ((c = fin.read()) != -1) {
                        temp = temp + Character.toString((char) c);
                    }
                    lastAction.setText(temp);

                } catch (Exception e) {

                }



                final View.OnTouchListener substitutionListener = new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Player player;
                            if ((player = getPlayerPressed(players, teamPlayer)) != null) {
                                if(player.isTwoMinOut() || player.isRedCard()){
                                    Toast.makeText(getBaseContext(), "Não é possivel substituir um jogador suspenso", Toast.LENGTH_SHORT).show();
                                }else {
                                    ImageButton btn_selectedPlayer = isChildrenImgButtonPressed(teamPlayer);
                                    showPopUpSubs(v, teamPlayer, player, btn_selectedPlayer);
                                }
                            } else {
                                Goalkeeper gk;
                                if (isGoalkeeperPressed(teamPlayer)) {
                                    changeGK(btn_goalkeeper1);
                                }
                            }
                        }
                        return true;
                    }

                };

                mainLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
                    @Override
                    public void onSwipeLeft() {
                        callIntentToStatistics(game, players, gks);
                    }
                });


                final View.OnTouchListener disciplineListener = new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Player player;
                            if ((player = getPlayerPressed(players, teamPlayer)) != null) {
                                ImageButton btn_selectedPlayer = isChildrenImgButtonPressed(teamPlayer);
                                ImageButton btn_cards = (ImageButton) findViewById(R.id.imgbtn_cards);

                                if(player.isRedCard()){
                                    btn_cards.setEnabled(false);
                                    Toast.makeText(getBaseContext(), "Jogador já possuí a penalização máxima", Toast.LENGTH_SHORT).show();
                                }else {
                                    showPopUpDiscipline(v, teamPlayer, players, player, btn_selectedPlayer);
                                }
                            }
                        }
                        return true;
                    }

                };


                final View.OnTouchListener zoneTouchListener = new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if(!playable){
                                Toast.makeText(getApplicationContext(), "Têm de configurar a equipa antes de registar ações",
                                        Toast.LENGTH_LONG).show();
                                }
                            else if(game.isClosed() != null || !game.isClosed()){
                                if(game.isClosed()) {
                                    Toast.makeText(getApplicationContext(), "O jogo já se encontra terminado.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }else {
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
                                            refreshAttackStatistics(btn_ft, btn_assist, btn_ca, btn_atk, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, tempPlayer);
                                            refreshDefensiveStatistics(btn_block_def, btn_disarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, tempPlayer);
                                        }

                                        if (btn_goalkeeper1.isPressed()) {
                                            Log.i("presionou o fk", "gk pressionado");
                                            refreshAttackStatistics(btn_ft, btn_assist, btn_ca, btn_atk, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, (Player) goalkeeper1);
                                            refreshDefensiveStatistics(btn_block_def, btn_disarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, (Player) goalkeeper1);
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

                                    if ((v.getParent() == (RelativeLayout) offensiveAction) || (v.getParent() == (RelativeLayout) finalization)) {
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

                                    if (v.getParent() == (RelativeLayout) defensiveAction) {
                                        btn_atk.setPressed(false);
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

                                    if (v.getParent() == (RelativeLayout) goalkeeperAction || v.getParent() == (RelativeLayout) goalkeeperZone) {
                                        btn_atk.setPressed(false);
                                        btn_ca.setPressed(false);
                                        btn_goal.setPressed(false);
                                        btn_defense.setPressed(false);
                                        btn_out.setPressed(false);
                                        btn_goalpost.setPressed(false);
                                        btn_block_atk.setPressed(false);
                                        btn_block_def.setPressed(false);
                                        btn_disarm.setPressed(false);
                                        btn_interception.setPressed(false);
                                        btn_players[0].setPressed(false);
                                        btn_players[1].setPressed(false);
                                        btn_players[2].setPressed(false);
                                        btn_players[3].setPressed(false);
                                        btn_players[4].setPressed(false);
                                        btn_players[5].setPressed(false);
                                    }

                                    if (v.getParent() == (RelativeLayout) otherAction) {
                                        btn_atk.setPressed(false);
                                        btn_ca.setPressed(false);
                                        btn_goal.setPressed(false);
                                        btn_defense.setPressed(false);
                                        btn_out.setPressed(false);
                                        btn_goalpost.setPressed(false);
                                        btn_block_atk.setPressed(false);
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

                                    if (v.getTag() == "btn_ft_adv") {
                                        btn_atk.setPressed(false);
                                        btn_ca.setPressed(false);
                                        btn_goal.setPressed(false);
                                        btn_defense.setPressed(false);
                                        btn_out.setPressed(false);
                                        btn_goalpost.setPressed(false);
                                        btn_block_atk.setPressed(false);
                                        btn_block_def.setPressed(false);
                                        btn_disarm.setPressed(false);
                                        btn_interception.setPressed(false);
                                        btn_players[0].setPressed(false);
                                        btn_players[1].setPressed(false);
                                        btn_players[2].setPressed(false);
                                        btn_players[3].setPressed(false);
                                        btn_players[4].setPressed(false);
                                        btn_players[5].setPressed(false);
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

                                    if ((goalkeeper = allPressedGoalkeeperAction(lbl_scoreOpponent, goalkeeperZone, goalkeeperAction, zones, goalkeeper1, game)) != null) {
                                        lastAction.setText(goalkeeper.getLastAction());
                                        game.setStarted();
                                    }

                                    if ((player = allPressedOffensive(lbl_scoreMyTeam, offensiveAction, finalization, zones, teamPlayer, players, game)) != null) {
                                        refreshAttackStatistics(btn_ft, btn_assist, btn_ca, btn_atk, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                                        lastAction.setText(player.getLastAction());
                                        game.setStarted();

                                        String saveData = player.getLastAction();

                                        try {
                                            FileOutputStream fOut = openFileOutput("mydata", MODE_WORLD_READABLE);
                                            fOut.write(saveData.getBytes());
                                            fOut.close();
                                            Toast.makeText(getBaseContext(), "Acção Registada",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }

                                    if ((player = pressedAsist(teamPlayer, players, btn_assist)) != null) {
                                        player.addAssistance();
                                        game.setStarted();
                                        refreshAttackStatistics(btn_ft, btn_assist, btn_ca, btn_atk, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                                        refreshLabels(null, null, null, null, btn_assist, null, null, null);
                                    }

                                    if ((player = pressedTechFail(teamPlayer, players, btn_ft)) != null) {
                                        player.addTechFail();
                                        game.setStarted();
                                        refreshAttackStatistics(btn_ft, btn_assist, btn_ca, btn_atk, btn_goal, btn_out, btn_block_atk, btn_goalpost, btn_defense, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                                        refreshLabels(null, null, null, null, null, btn_ft, null, null);
                                    }


                                    if ((player = allPressedDefensive(defensiveAction, zones, teamPlayer, players)) != null) {
                                        refreshDefensiveStatistics(btn_block_def, btn_disarm, btn_interception, btn_zone_1, btn_zone_2, btn_zone_3, btn_zone_4, btn_zone_5, btn_zone_6, btn_zone_7, btn_zone_8, btn_zone_9, player);
                                        lastAction.setText(player.getLastAction());
                                        game.setStarted();

                                        String saveData = player.getLastAction();

                                        try {
                                            FileOutputStream fOut = openFileOutput("mydata", MODE_WORLD_READABLE);
                                            fOut.write(saveData.getBytes());
                                            fOut.close();
                                            Toast.makeText(getBaseContext(), "Acção Registada",
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

                btn_players[0].setOnTouchListener(zoneTouchListener);
                btn_players[1].setOnTouchListener(zoneTouchListener);
                btn_players[2].setOnTouchListener(zoneTouchListener);
                btn_players[3].setOnTouchListener(zoneTouchListener);
                btn_players[4].setOnTouchListener(zoneTouchListener);
                btn_players[5].setOnTouchListener(zoneTouchListener);
                btn_goalkeeper1.setOnTouchListener(zoneTouchListener);




                btn_ca.setOnTouchListener(zoneTouchListener);
                btn_atk.setOnTouchListener(zoneTouchListener);

                btn_ca.setTag("btn_ca");
                btn_atk.setTag("btn_atk");

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

                btn_ft.setOnTouchListener(zoneTouchListener);
                btn_assist.setOnTouchListener(zoneTouchListener);
                btn_discipline.setOnTouchListener(disciplineListener);
                btn_subs.setOnTouchListener(substitutionListener);


                btn_gk_goal.setOnTouchListener(zoneTouchListener);
                btn_gk_def.setOnTouchListener(zoneTouchListener);
                btn_gk_post.setOnTouchListener(zoneTouchListener);
                btn_gk_out.setOnTouchListener(zoneTouchListener);

                btn_gk_goal.setTag("btn_gk_goal");
                btn_gk_def.setTag("btn_gk_def");
                btn_gk_out.setTag("btn_gk_out");
                btn_gk_post.setTag("btn_gk_post");


                final ImageButton start = (ImageButton) findViewById(R.id.imgbtn_play);

                View.OnClickListener stopWatchListener =
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                if(playable) {
                                    if (isStart == false) {
                                        isStart = true;
                                        start.setImageResource(R.drawable.pause);
                                    } else {
                                        isStart = false;
                                        start.setImageResource(R.drawable.play);
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Têm de configurar a equipa para poder registar ações.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        };

                start.setOnClickListener(stopWatchListener);

                final Handler handler;

                handler = new Handler();





                setTime(time);


                final Runnable r = new Runnable() {
                    public void run() {

                        if (isStart) {
                            seconds++;
                            for (Iterator<Player> iterator = players2min.iterator(); iterator.hasNext();) {
                                Player p = iterator.next();
                                p.incrementTwoMinTimer();
                                if(p.getTwoMinTimer()==120){
                                    iterator.remove();
                                    p.setTwoMinTimer(0);
                                    p.setTwoMinOut();
                                    Toast.makeText(getApplicationContext(), "O jogador com o número "+p.getNumber()+" já não está suspenso.",
                                            Toast.LENGTH_LONG).show();
                                    refreshPlayerImage();
                                }
                            }
                            if (seconds == 60) {
                                seconds = 0;
                                minutes++;
                            }

                            game.setMinutes(minutes);
                            game.setSeconds(seconds);

                            setTime(time);


                        }
                        handler.postDelayed(this, 1000);


                    }
                };

                handler.postDelayed(r, 1000);

        if(playable == false){
            Toast.makeText(getApplicationContext(), "Têm de configurar a equipa para poder registar.",
                    Toast.LENGTH_LONG).show();
        }

        ImageView mHome = (ImageView) findViewById(R.id.pager_home);
        ImageView mStats = (ImageView) findViewById(R.id.pager_stats);
        ImageView mGoal = (ImageView) findViewById(R.id.pager_goal);

        mStats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callIntentToStatistics(game, players, gks);
            }
        });

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, StatisticsGoalkeeper.class);
                intent.putExtra("Game", game);
                intent.putExtra("Goalkeepers", gks);
                intent.putExtra("CalledBy", "sheet");
                startActivityForResult(intent, 2);
            }
        });


    }

    @Override
    protected void onDestroy(){
        game.setPlayers(players);
        game.setGks(gks);
        if(!verifyExistGame()) {
            games.add(game);

        }else{
            int gameIndex = getThisGameFromList();
            games.remove(gameIndex);
            games.add(gameIndex, game);
        }
        if(save){
            saveFile();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        game.setPlayers(players);
        game.setGks(gks);




        if(save) {
            int gameIndex = getThisGameFromList();
            games.remove(gameIndex);
            games.add(gameIndex, game);
            saveFile();
        }
        super.onPause();
    }

    public void saveFile(){
        String filename = "game-gedd.ser";
        game.setMinutes(minutes);
        game.setSeconds(seconds);
        // save the object to file
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        Log.i("onDestroy", "Entrei no on destroy");
        try {
            Log.i("onDestroy", "1");
            fos = new FileOutputStream(getApplicationContext().getFilesDir().getPath().toString() + filename);
            Log.i("onDestroy", "2");
            out = new ObjectOutputStream(fos);
            Log.i("onDestroy", "3");
            out.writeObject(games);
            Log.i("onDestroy", "4");

            out.close();

            Log.i("onDestroy", "Detrui cenas");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public Boolean verifyExistGame(){
        if(games != null) {
            for (Game gameTemp : games) {

                if (gameTemp.getID().compareTo(game.getID()) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getThisGameFromList(){
        if(games != null) {
            for (int i=0; i < games.size();i++) {

                if (games.get(i).getID().compareTo(game.getID()) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void showPopUpDiscipline (final View view, final RelativeLayout teamPlayer, final LinkedList<Player> players, final  Player player, final ImageButton btnPlayer){

        PopupMenu popupMenu = new PopupMenu(this, view);

        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.popup_discipline, popupMenu.getMenu());
        popupMenu.show();

        MenuItem btn_twoMin = popupMenu.getMenu().findItem(R.id.btn_2min);

        MenuItem btn_yellowCard = popupMenu.getMenu().findItem(R.id.btn_yellowCard);

        if(player.isYellowCard()){

            btn_yellowCard.setVisible(false);
        }


        if(player.isTwoMinOut()){

            btn_twoMin.setVisible(false);
        }



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String numberShirt;
                Resources resources = getResources();
                final int resourceId;

                switch (item.getItemId()){
                    case R.id.btn_yellowCard:

                        player.setYellowCard();
                        game.setStarted();
                        numberShirt = "ic_shirt_" + Integer.toString((player.getNumber())) + "_yellowcard";
                        resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
                        btnPlayer.setImageResource(resourceId);
                        return true;

                    case R.id.btn_redCard:

                        player.setRedCard();
                        game.setStarted();
                        numberShirt = "ic_shirt_" + Integer.toString((player.getNumber())) + "_redcard";
                        resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
                        btnPlayer.setImageResource(resourceId);
                        return true;

                    case R.id.btn_2min:

                        player.setTwoMinOut();
                        game.setStarted();
                        players2min.add(player);
                        numberShirt = "ic_shirt_" + Integer.toString((player.getNumber())) + "_2min";
                        resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
                        btnPlayer.setImageResource(resourceId);
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
            if(container.getChildAt(i).isPressed() == true && container.getChildAt(i).getTag() != null)
                return (ImageButton) container.getChildAt(i);
        }
        return null;
    }

    private Goalkeeper allPressedGoalkeeperAction(TextView score, RelativeLayout goalkeeperZone, RelativeLayout goalkeeperAction, RelativeLayout zones, Goalkeeper goalkeeper, Game game) {

        Button btnGkZone;
        Button btnGkAction;
        Button btnZone;

        if ((btnGkZone = isChildrenButtonPressed(goalkeeperZone)) != null && (btnGkAction = isChildrenButtonPressed(goalkeeperAction)) != null && (btnZone = isChildrenButtonPressed(zones))!= null) {

            goalkeeper.setLastAction("Guarda-Redes - " + btnGkAction.getTag().toString() + "\n Zona de Baliza " + btnGkZone.getTag().toString() + ", Zona " + btnZone.getTag().toString());

            if (btnGkAction.getTag() == "btn_gk_goal") {
                goalkeeper.setGoal((int) btnZone.getTag(), (int) btnGkZone.getTag());
                game.setScoreOpponent();
                score.setText(String.valueOf(game.getScoreOpponent()));
            } else if (btnGkAction.getTag() == "btn_gk_def") {
                goalkeeper.setDefended((int) btnZone.getTag(), (int) btnGkZone.getTag());
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

    private Player allPressedOffensive(TextView score, RelativeLayout offensiveAction, RelativeLayout finalization, RelativeLayout zones, RelativeLayout teamPlayer, LinkedList<Player> players, Game game){

        Button btnOffAct;
        Button btnFinalization;
        Button btnZone;
        ImageButton btnPlayer;



        if((btnOffAct = isChildrenButtonPressed(offensiveAction)) != null && (btnFinalization = isChildrenButtonPressed(finalization)) != null && (btnZone = isChildrenButtonPressed(zones))!= null && (btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null){

            if(btnOffAct.getTag() == "btn_goal"){
                game.setScoreMyTeam();
                score.setText(String.valueOf(game.getScoreMyTeam()));
            }

            for(Player player : players){
                if(player.getNumber()== Integer.valueOf(btnPlayer.getTag().toString())){
                    player.setLastAction("Jogador " + btnPlayer.getTag().toString() + " - " + btnOffAct.getTag().toString() + "\n" + btnFinalization.getTag().toString() + ", Zona " + btnZone.getTag().toString());
                    player.refreshPlayerStats(btnFinalization.getTag().toString(), (int) btnZone.getTag(), btnOffAct.getTag().toString());
                    refreshLabels(btnOffAct, null, btnFinalization, btnZone, null, null, null, null);

                    return player;
                }
            }

            for(Goalkeeper gk : gks){
                if(gk.getNumber()==Integer.valueOf(btnPlayer.getTag().toString())){
                    gk.setLastAction("Jogador " + btnPlayer.getTag().toString() + " - " + btnOffAct.getTag().toString() + "\n" + btnFinalization.getTag().toString() + ", Zona " + btnZone.getTag().toString());
                    gk.refreshPlayerStats(btnFinalization.getTag().toString(), (int) btnZone.getTag(), btnOffAct.getTag().toString());
                    refreshLabels(btnOffAct, null, btnFinalization, btnZone, null, null, null, null);

                    return gk;
                }
            }
        }
        return null;
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

    private Player allPressedDefensive(RelativeLayout defensiveAction, RelativeLayout zones, RelativeLayout teamPlayer, LinkedList<Player> players){
        Button btnDefAct;
        Button btnZone;
        ImageButton btnPlayer;

        if((btnDefAct = isChildrenButtonPressed(defensiveAction)) != null && (btnZone = isChildrenButtonPressed(zones))!= null && (btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null){

            for(Player player : players){
                if(player.getNumber() == Integer.valueOf(btnPlayer.getTag().toString())){
                    player.setLastAction("Jogador " + btnPlayer.getTag().toString() + "\n" + btnDefAct.getTag().toString() + ", Zona " + btnZone.getTag().toString());
                    if (btnDefAct.getTag() == "btn_block_def"){
                        player.setBlock((int) btnZone.getTag());
                        game.setStarted();
                    } else if(btnDefAct.getTag() == "btn_disarm") {
                        player.setDisarm((int) btnZone.getTag());
                        game.setStarted();
                    } else if(btnDefAct.getTag() == "btn_interception"){
                        player.setInterception((int) btnZone.getTag());
                        game.setStarted();
                    }
                    refreshLabels(null, btnDefAct, null, btnZone, null, null, null, null);

                    return player;
                }
            }

            for(Goalkeeper gk : gks){
                if(gk.getNumber() == Integer.valueOf(btnPlayer.getTag().toString())){
                    gk.setLastAction("Jogador " + btnPlayer.getTag().toString() + "\n" + btnDefAct.getTag().toString() + ", Zona " + btnZone.getTag().toString());
                    if (btnDefAct.getTag() == "btn_block_def"){
                        gk.setBlock((int) btnZone.getTag());
                    } else if(btnDefAct.getTag() == "btn_disarm") {
                        gk.setDisarm((int) btnZone.getTag());
                    } else if(btnDefAct.getTag() == "btn_interception"){
                        gk.setInterception((int) btnZone.getTag());
                    }
                    refreshLabels(null, btnDefAct, null, btnZone, null, null, null, null);

                    return gk;
                }
            }
        }
        return null;
    }

    private void refreshAttackStatistics(Button btn_tf, Button btn_assist, Button btn_ca, Button btn_atk, Button btn_goal, Button btn_out, Button btn_atk_block, Button btn_goalpost, Button btn_defense, Button btn_zone_1, Button btn_zone_2, Button btn_zone_3, Button btn_zone_4, Button btn_zone_5, Button btn_zone_6, Button btn_zone_7, Button btn_zone_8, Button btn_zone_9 , Player player){
        btn_ca.setText("Contra Ataque - "+player.getAllActions(player.getCaShotGoal())+"/"+player.getAllCaShots());
        btn_atk.setText("Ataque - "+player.getAllActions(player.getAtkShotGoal())+"/"+player.getAllAtkShots());

        btn_zone_1.setText("Zona 1 - "+player.getZoneAllGoals(1)+"/"+player.getZoneAllShots(1));
        btn_zone_2.setText("Zona 2 - "+player.getZoneAllGoals(2)+"/"+player.getZoneAllShots(2));
        btn_zone_3.setText("Zona 3 - "+player.getZoneAllGoals(3)+"/"+player.getZoneAllShots(3));
        btn_zone_4.setText("Zona 4 - "+player.getZoneAllGoals(4)+"/"+player.getZoneAllShots(4));
        btn_zone_5.setText("Zona 5 - "+player.getZoneAllGoals(5)+"/"+player.getZoneAllShots(5));
        btn_zone_6.setText("Zona 6 - "+player.getZoneAllGoals(6)+"/"+player.getZoneAllShots(6));
        btn_zone_7.setText("Zona 7 - "+player.getZoneAllGoals(7)+"/"+player.getZoneAllShots(7));
        btn_zone_8.setText("Zona 8 - "+player.getZoneAllGoals(8)+"/"+player.getZoneAllShots(8));
        btn_zone_9.setText("Zona 9 - "+player.getZoneAllGoals(9)+"/"+player.getZoneAllShots(9));

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

        btn_zone_1.setText("Zona 1 - "+player.getZoneAllGoals(1)+"/"+player.getZoneAllShots(1));
        btn_zone_2.setText("Zona 2 - "+player.getZoneAllGoals(2)+"/"+player.getZoneAllShots(2));
        btn_zone_3.setText("Zona 3 - "+player.getZoneAllGoals(3)+"/"+player.getZoneAllShots(3));
        btn_zone_4.setText("Zona 4 - "+player.getZoneAllGoals(4)+"/"+player.getZoneAllShots(4));
        btn_zone_5.setText("Zona 5 - "+player.getZoneAllGoals(5)+"/"+player.getZoneAllShots(5));
        btn_zone_6.setText("Zona 6 - "+player.getZoneAllGoals(6)+"/"+player.getZoneAllShots(6));
        btn_zone_7.setText("Zona 7 - "+player.getZoneAllGoals(7)+"/"+player.getZoneAllShots(7));
        btn_zone_8.setText("Zona 8 - "+player.getZoneAllGoals(8)+"/"+player.getZoneAllShots(8));
        btn_zone_9.setText("Zona 9 - "+player.getZoneAllGoals(9)+"/"+player.getZoneAllShots(9));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.configureTeam) {
            callIntentToConfigureTeam();
        }
        if (id == R.id.editGame) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Get the layout inflater
            LayoutInflater inflater = this.getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setTitle("Editar Jogo");

            builder.setView(inflater.inflate(R.layout.dialog_newgame2, null))
                    // Add action buttons
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog dialog = builder.create();

            dialog.show();

            TimePicker tpHourMin = (TimePicker) dialog.findViewById(R.id.timePicker);
            tpHourMin.setIs24HourView(true);

            DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            datePicker.setMinDate(System.currentTimeMillis() - 1000);

            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new EditGameListener(dialog));

            EditText myTeamDialogText = (EditText) dialog.findViewById(R.id.myTeamEditTextDialog);
            EditText advTeamDialogText = (EditText) dialog.findViewById(R.id.advTeamEditTextDialog);
            EditText localDialogText = (EditText) dialog.findViewById(R.id.localEditTextDialog);

            myTeamDialogText.setText(game.getMyTeam());
            advTeamDialogText.setText(game.getOpponent());
            localDialogText.setText(game.getLocal());





        }

        if(id == R.id.deleteGame){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tem a certeza que deseja apagar o jogo?");
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int var = getThisGameFromList();
                    games.remove(var);
                    saveFile();
                    save=false;

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            });



            builder.show();





        }

        return super.onOptionsItemSelected(item);
    }



    //MENU
    public void configureTeam(MenuItem item){
        callIntentToConfigureTeam();
    }

    public void callIntentToConfigureTeam(){
        Intent intent = new Intent(this, ConfigureTeamActivity.class);
        if(players.size()>0 && gks.size()>0){
            intent.putExtra("Players", players);
            intent.putExtra("Goalkeepers", gks);
        }
        if(!game.isStarted()){
            intent.putExtra("started",false);
        }

        startActivityForResult(intent, 1);
    }

    public void callIntentToStatistics(Game game, LinkedList<Player> players, LinkedList<Goalkeeper> gks){

        Intent intent = new Intent(this, StatisticsTeam.class);
        intent.putExtra("Game", game);
        intent.putExtra("Players", players);
        intent.putExtra("Goalkeepers", gks);
        startActivityForResult(intent, 2);
    }

    public int[] stringToInt(String text){
        String[] raw = text.split("[,]");
        int[] intArray = new int[raw.length];
        for (int i = 0; i < raw.length; i++) {
            intArray[i] = Integer.parseInt(raw[i]);
        }
        return intArray;
    }


    public void newGame(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);


        final TextView lbl_opponentName = new TextView(this);
        final EditText opponentTeam = new EditText(this);

        opponentTeam.setClickable(true);

        builder.setTitle("Novo jogo");

        lbl_opponentName.setTextSize(16);
        lbl_opponentName.setTextColor(Color.BLACK);
        lbl_opponentName.setText("  Nome da equipa adversária: ");

        opponentTeam.setInputType(InputType.TYPE_CLASS_TEXT);
        opponentTeam.setFocusable(true); // Add This Line And try


        layout.addView(lbl_opponentName);
        layout.addView(opponentTeam);

        builder.setView(layout);



        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lbl_opponent.setText(opponentTeam.getText().toString());
                game.setOpponent(opponentTeam.getText().toString());
                createGame();
                TextView scoreMyTeam = (TextView) findViewById(R.id.scoreMyTeam);
                TextView scoreOpponent = (TextView) findViewById(R.id.scoreOpponent);

                scoreMyTeam.setText(String.valueOf(game.getScoreMyTeam()));
                scoreOpponent.setText(String.valueOf(game.getScoreOpponent()));

                minutes = 0;
                seconds = 0;
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void showPopUpSubs(View view, final RelativeLayout teamPlayer, final Player player, final ImageButton btnPlayer){


        PopupMenu popupMenu = new PopupMenu(this, view);

        for(Player playerTemp : players){
            Log.i("Playing?",String.valueOf(playerTemp.getPlaying()));
            if(!playerTemp.getPlaying()){
                popupMenu.getMenu().add(Menu.NONE,playerTemp.getNumber(),Menu.NONE,playerTemp.getName()+" #"+String.valueOf(playerTemp.getNumber()));
            }
        }

        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.popup_sub, popupMenu.getMenu());

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                Player playerIn = getPlayerWhichNumberIs(item.getItemId(), players);
                playerIn.setPlaying(true);
                player.setPlaying(false);


                        //vai buscar o valor da tag do botão e vê se corresponde ao numero do jogador que vai sair

                        if(btnPlayer.getTag() != null && player.getNumber() == Integer.valueOf(btnPlayer.getTag().toString())){
                            //caso corresponda troca o valor da tag pelo numero do novo jogador

                            Log.i("Entra",btnPlayer.getTag().toString());
                            String numberShirt;
                            if(playerIn.isYellowCard()) {
                                numberShirt = "ic_shirt_" + Integer.toString((playerIn.getNumber())) + "_yellowcard";
                            }else{
                                numberShirt = "ic_shirt_" + Integer.toString((playerIn.getNumber()));
                            }

                            Resources resources = getResources();
                            final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
                            btnPlayer.setImageResource(resourceId);

                            btnPlayer.setTag(playerIn.getNumber());
                        }



                return true;

            }
        });
    }

    public void changeGK(ImageButton btn_goalkeeper1){
        if(gks.size() > 1) {
            if (gks.get(0).getPlaying()) {
                gks.get(0).setPlaying(false);
                gks.get(1).setPlaying(true);
                goalkeeper1 = gks.get(1);
            } else {
                gks.get(1).setPlaying(false);
                gks.get(0).setPlaying(true);
                goalkeeper1 = gks.get(0);
            }
            lbl_goalkeeper1.setText(String.valueOf(goalkeeper1.getNumber()));
            btn_goalkeeper1.setTag(String.valueOf(goalkeeper1.getNumber()));
        }
    }

    public Player getPlayerWhichNumberIs(int number, LinkedList<Player> players){
        for (Player player : players){
            if(player.getNumber()==number){
                return player;
            }
        }
        return null;
    }

    public ArrayList<Game> readSerializable(){
        // read the object from file
        // save the object to file
        FileInputStream fis = null;
        ObjectInputStream in = null;
            try {
                fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"game-gedd.ser");
                in = new ObjectInputStream(fis);
                games = (ArrayList<Game>) in.readObject();
                in.close();
                Log.i("read True","Consegui Ler");
                return games;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        Log.i("read false","nao li nada");
        return null;
        }

    public void createGame(){
        Intent intent = getIntent();

        game = new Game(
                intent.getStringExtra("myTeam"),
                intent.getStringExtra("advTeam"),
                intent.getIntExtra("hour",0),
                intent.getIntExtra("minute",0),
                intent.getIntExtra("day",0),
                intent.getIntExtra("month",0),
                intent.getIntExtra("year",0),
                intent.getStringExtra("local"));

        players = new LinkedList<Player>();
        gks = new LinkedList<Goalkeeper>();
        minutes = 0;
        seconds = 0;


        setTeam();

        game.setPlayers(players);
        game.setGks(gks);
        existFile = true;




    }

   /* public void popUpLoadGame(final TextView lbl_scoreMyTeam, final TextView lbl_scoreOpponent){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);

        // set title
        alertDialogBuilder.setTitle("Carregar jogo");

        // set dialog message
        alertDialogBuilder
                .setMessage("Deseja carregar o último jogo?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, do nothing


                    }
                })
                .setNegativeButton("Não",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, create a new game
                        createGame();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }*/

    public void setTime(TextView time){
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

    private Boolean isGoalkeeperPressed(RelativeLayout teamPlayer) {
        ImageButton btnPlayer;
        if ((btnPlayer = isChildrenImgButtonPressed(teamPlayer)) != null) {
            return true;
        }
        return false;
    }



    public void clickFtAdv(View v){
        game.setTechnicalFailAdv(game.getTechnicalFailAdv()+1);
        btn_tf_adv.setText("Falha Técnica Adversária "+game.getTechnicalFailAdv());
    }

    private void refreshPlayerImage(){
        Player p;
        int j = 0;
        for(int i = 0; i<players.size(); i++){

            p = players.get(i);

            if(p.getPlaying()) {

                String numberShirt;
                if (p.isYellowCard()) {
                    numberShirt = "ic_shirt_" + Integer.toString((p.getNumber())) + "_yellowcard";
                } else if (p.isTwoMinOut()) {
                    numberShirt = "ic_shirt_" + Integer.toString((p.getNumber())) + "_2min";
                    if(players2min.indexOf(p) == -1)
                        players2min.add(p);
                } else {
                    numberShirt = "ic_shirt_" + Integer.toString((p.getNumber()));
                }
                Resources resources = getResources();
                final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
                if(j<6) {
                    btn_players[j].setImageResource(resourceId);
                    btn_players[j].setTag(p.getNumber());
                }
                Log.i(TAG, j + p.getName());
                j++;
            }
        }
        if(gks.size()>0) {
            for(int i =0; i<gks.size();i++){
                if(gks.get(i).getPlaying()) {
                    goalkeeper1 = gks.get(i);
                    btn_goalkeeper1.setTag(String.valueOf(goalkeeper1.getNumber()));
                    gks.get(0).setPlaying(true);
                    lbl_goalkeeper1.setText(String.valueOf(gks.get(0).getNumber()));
                }
            }

        }else{
            btn_goalkeeper1.setTag(0);
            lbl_goalkeeper1.setText("0");
        }

    }



protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 2) {
        if(resultCode == RESULT_OK){
            onResume();
            if(!game.isStarted()){
                setTeam();
                associatePlayersToButton();
            }
        }
        if (resultCode == RESULT_CANCELED) {
            onResume();
            finish();
        }
        if(resultCode == 2){
            finish();
        }
    }
}

    private Boolean setTeam(){
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"GEDD-Players-Data");
            in = new ObjectInputStream(fis);
            players = (LinkedList<Player>) in.readObject();
            in.close();
            fis.close();
            fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"GEDD-Goalkeepers-Data");
            in = new ObjectInputStream(fis);
            gks = (LinkedList<Goalkeeper>) in.readObject();
            in.close();
            fis.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i("read false","nao li nada");
        return false;
    }

    private void associatePlayersToButton(){
        for (int i = 0; i < 6; i++) {
            String numberShirt;
            if(i<players.size()){
                Player p = players.get(i);
                numberShirt = "ic_shirt_" + Integer.toString((p.getNumber()));
                p.setPlaying(true);
            }else{
                numberShirt = "ic_shirt_1";
            }



            Resources resources = getResources();
            final int resourceId = resources.getIdentifier(numberShirt, "drawable", getPackageName());
            btn_players[i].setImageResource(resourceId);



        }

        for (int i = 0; i<6; i++){
            if(i<players.size()){
                btn_players[i].setTag(String.valueOf(players.get(i).getNumber()));
            }else{
                btn_players[i].setTag(0);
            }
        }

        if(gks.size()>0) {
            goalkeeper1 = gks.get(0);
            btn_goalkeeper1.setTag(String.valueOf(goalkeeper1.getNumber()));
            gks.get(0).setPlaying(true);
            lbl_goalkeeper1.setText(String.valueOf(gks.get(0).getNumber()));
        }else{
            btn_goalkeeper1.setTag(0);
            lbl_goalkeeper1.setText("0");
        }




    }

    protected void onRestart(){
        super.onRestart();
    }

    protected void onResume(){
        super.onResume();
    }

    public class EditGameListener implements View.OnClickListener {
        private final Dialog dialog;
        public EditGameListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            // put your code here
            EditText myTeam = (EditText) dialog.findViewById(R.id.myTeamEditTextDialog);
            EditText advTeam = (EditText) dialog.findViewById(R.id.advTeamEditTextDialog);
            EditText local = (EditText) dialog.findViewById(R.id.myTeamEditTextDialog);
            DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            TimePicker timePicker =(TimePicker) dialog.findViewById(R.id.timePicker);

            String myTeamString = myTeam.getText().toString();
            String advTeamString = advTeam.getText().toString();
            String localString = local.getText().toString();
            if(!myTeamString.isEmpty() || !advTeamString.isEmpty() || !localString.isEmpty()){
                if(game.getMyTeam() != myTeam.getText().toString()){
                    game.setMyTeam(myTeam.getText().toString());
                    lbl_myTeam.setText(myTeam.getText().toString());

                }
                if(game.getOpponent() != advTeam.getText().toString()){
                    game.setOpponent(advTeam.getText().toString());
                    lbl_opponent.setText(advTeam.getText().toString());
                }
                if(game.getLocal() != local.getText().toString()){
                    game.setLocal(local.getText().toString());
                }
                if(game.getGameHour() !=timePicker.getCurrentHour() ){
                    game.setGameHour(timePicker.getCurrentHour());
                }
                if(game.getGameMinute() !=timePicker.getCurrentMinute() ){
                    game.setGameMinute(timePicker.getCurrentMinute());
                }
                if(game.getGameDay() !=datePicker.getDayOfMonth() ){
                    game.setGameMinute(datePicker.getDayOfMonth());
                }

                if(game.getGameMonth() !=datePicker.getMonth() ){
                    game.setGameMinute(datePicker.getMonth());

                }

                if(game.getGameYear() !=datePicker.getYear() ){
                    game.setGameMinute(datePicker.getYear());
                }
                dialog.dismiss();
            }else{
                Toast.makeText(GameActivity.this, "Os campos tem de estar preenchidos", Toast.LENGTH_SHORT).show();
            }
        }
    }





}
