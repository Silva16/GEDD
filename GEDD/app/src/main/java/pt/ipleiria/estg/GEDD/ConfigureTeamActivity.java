package pt.ipleiria.estg.GEDD;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;

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
            LinkedList<Goalkeeper> gks = jsonUtil.getGKList(jsonObject);

            int counter = 0;
            int counterGK = 0;
            for (int i = 0, j = table.getChildCount(); i < j; i++) {
                View view2 = table.getChildAt(i);
                if (view2 instanceof TableRow) {

                    TableRow row = (TableRow) view2;
                    if(row.getChildAt(1) instanceof EditText) {
                        EditText name = (EditText) row.getChildAt(1);
                        EditText number = (EditText) row.getChildAt(2);
                        if(counter == 6 && counterGK < gks.size()){

                                name.setText(gks.get(counterGK).getName());
                                number.setText(Integer.toString(gks.get(counterGK).getNumber()));

                                counterGK++;


                        }else{
                            if (counter < players.size()) {

                                name.setText(players.get(counter).getName());
                                number.setText(Integer.toString(players.get(counter).getNumber()));

                                counter++;
                            }

                        }


                    }


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

        if(!verifyFields()) {
            return;
        }

        EditText number;
        EditText name;
        ArrayList<Player> players = new ArrayList<Player>();
        ArrayList<Goalkeeper> gks = new ArrayList<Goalkeeper>();
        for(int i = 0, j = table.getChildCount(); i < j; i++) {

            View view2 = table.getChildAt(i);
            if (view2 instanceof TableRow) {
                TableRow row = (TableRow) view2;
                if(row.getChildAt(1) instanceof EditText)
                {
                    name = (EditText) row.getChildAt(1);
                    number = (EditText) row.getChildAt(2);
                    if (name.getText().toString().compareTo("") != 0 && number.getText().toString().compareTo("") != 0) {
                        if(i != 8 && i !=10 ) {
                            players.add(new Player(Integer.valueOf(number.getText().toString()), name.getText().toString()));
                        }else{
                            gks.add(new Goalkeeper(Integer.valueOf(number.getText().toString()), name.getText().toString()));
                        }
                    }
                }
            }


        }

        String file = JsonUtil.playerToJSon(players,gks);


        FileOutputStream fos = null;
        try {
            fos = openFileOutput("GEDDData2", Context.MODE_PRIVATE);
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);

    }

    public void cancelClick (View view){
        finish();
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

    public boolean verifyFields(){
        EditText number;
        EditText name;
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        String[] names = new String[7];
        String regex = "[A-Z]?([a-z]+)\\s?([A-Z]?[a-z]+)?";
        int counter = 0;
        for(int i = 0, j = table.getChildCount(); i < j; i++) {
            View view2 = table.getChildAt(i);
            if (view2 instanceof TableRow) {
                TableRow row = (TableRow) view2;
                if(row.getChildAt(1) instanceof EditText){

                    name = (EditText) row.getChildAt(1);
                    number = (EditText) row.getChildAt(2);






                    if(name.getText().toString().matches("")){
                        if(counter < 7){
                            Toast.makeText(getApplicationContext(), "Preencha as primeiras sete linhas",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }
                        counter++;
                    }else {
                        counter++;
                        if (!Pattern.matches(regex, name.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Um ou mais nomes inválidos",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }

                        if (!isInteger(number.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Um ou mais números inválidos",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }

                        if(Integer.valueOf(number.getText().toString()) < 1 || Integer.valueOf(number.getText().toString()) > 99){
                            Toast.makeText(getApplicationContext(), "Os números tem de ser superiores a 0 e inferiores a 100",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }

                        numbers.add(Integer.valueOf(number.getText().toString()));
                    }
                }
            }
        }
        Set<Integer> set = new HashSet<Integer>(numbers);

        if(set.size() < numbers.size()){
            Toast.makeText(getApplicationContext(), "Não pode repetir números",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

}
