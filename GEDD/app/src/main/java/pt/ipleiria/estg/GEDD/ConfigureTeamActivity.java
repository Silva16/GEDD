package pt.ipleiria.estg.GEDD;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import pt.ipleiria.estg.GEDD.Models.Player;
import pt.ipleiria.estg.GEDD.R;

public class ConfigureTeamActivity extends ActionBarActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_team);
         table = (TableLayout) findViewById(R.id.table_team);

        //CARREGAR DADOS CASO EXISTAM
        JsonUtil jsonUtil = new JsonUtil();
        JSONObject jsonObject = jsonUtil.readFile(getApplicationContext());
        if(jsonObject!=null) {
            LinkedList<Player> players = jsonUtil.getPlayersList(jsonObject);
            int counter = 0;
            for (int i = 0, j = table.getChildCount(); i < j; i++) {
                View view2 = table.getChildAt(i);
                if (view2 instanceof TableRow) {

                    TableRow row = (TableRow) view2;
                    EditText name = (EditText) row.getChildAt(2);
                    EditText number = (EditText) row.getChildAt(4);

                    name.setText(players.get(counter).getName());
                    number.setText(Integer.toString(players.get(counter).getNumber()));

                    counter ++;


                }
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configure_team, menu);
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

    public void submitClick(View view){
        EditText number;
        EditText name;
        Player[] players = new Player[7];
        int[] numbers = new int[7];
        String[] names = new String[7];
        for(int i = 0, j = table.getChildCount(); i < j; i++) {
            View view2 = table.getChildAt(i);
            if (view2 instanceof TableRow) {
                TableRow row = (TableRow) view2;
                name = (EditText) row.getChildAt(2);
                number = (EditText) row.getChildAt(4);

                players[i] = new Player(Integer.valueOf(number.getText().toString()), name.getText().toString());
            }


        }

        String file = JsonUtil.playerToJSon(players);


        FileOutputStream fos = null;
        try {
            fos = openFileOutput("GEDDTeamData", Context.MODE_PRIVATE);
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public String intToString(int[] intArray){
        String text;
        text = "" + intArray[0];
        for (int i = 1; i < intArray.length; i++) {
            text += ","+intArray[i];
        }
        return text;
    }

    public int[] stringToInt(String text){
        String[] raw = text.split("[,]");
        int[] intArray = new int[raw.length];
        for (int i = 0; i < raw.length; i++) {
            intArray[i] = Integer.parseInt(raw[i]);
        }
        return intArray;
    }

}
