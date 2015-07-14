package pt.ipleiria.estg.GEDD;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import pt.ipleiria.estg.GEDD.Models.Game;
import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;

public class ConfigureTeamActivity extends ActionBarActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    private TableLayout table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_team);
        table = (TableLayout) findViewById(R.id.table_team);
        Button confirmBtn = (Button) findViewById(R.id.btn_confirm);
        Button cancelBtn = (Button) findViewById(R.id.btn_cancel);

        if(getIntent().getBooleanExtra("started",true)){
            confirmBtn.setVisibility(View.GONE);
            cancelBtn.setText("Voltar");
        }

        //CARREGAR DADOS CASO EXISTAM
        LinkedList<Player> players = new LinkedList<>();
        LinkedList<Goalkeeper> gks = new LinkedList<>();

        if(getIntent().getSerializableExtra("Players") !=null && getIntent().getSerializableExtra("Goalkeepers") !=null){
            players = new LinkedList((List) (getIntent().getSerializableExtra("Players")));
            gks = new LinkedList((List) (getIntent().getSerializableExtra("Goalkeepers")));
        }



        if(players != null && gks != null){
            fillTable(players, gks);
        }else if((players = getPlayers())!= null && (gks = getGoalkeepers()) != null) {
            fillTable(players, gks);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        LinkedList<Player> players = new LinkedList<Player>();
        LinkedList<Goalkeeper> gks = new LinkedList<Goalkeeper>();
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


        FileOutputStream fos = null;
        ObjectOutputStream out = null;

        try {
            // save the object to file

            Log.i("onDestroy", "Entrei no on destroy");
            Log.i("players","1");
            fos = new FileOutputStream(getApplicationContext().getFilesDir().getPath().toString()+"GEDD-Players-Data");
            Log.i("players", "2");
            out = new ObjectOutputStream(fos);
            Log.i("players","3");
            out.writeObject(players);
            Log.i("players","4");

            fos.close();
            out.close();

            Log.i("gks","1");
            fos = new FileOutputStream(getApplicationContext().getFilesDir().getPath().toString()+"GEDD-Goalkeepers-Data");
            Log.i("gks", "2");
            out = new ObjectOutputStream(fos);
            Log.i("gks","3");
            out.writeObject(gks);
            Log.i("gks","4");

            fos.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent resultIntent = new Intent();
        setResult(MainActivity.RESULT_OK, resultIntent);
        finish();

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
        String regex = "[\\p{L}]?([\\p{L}]+)\\s?([\\p{L}]?[\\p{L}]+)?";
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

    public LinkedList<Player> getPlayers(){
        // read the object from file
        // save the object to file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        LinkedList<Player> players = new LinkedList<Player>();
        try {
            fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"GEDD-Players-Data");
            in = new ObjectInputStream(fis);
            players = (LinkedList<Player>) in.readObject();
            in.close();
            return players;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i("read false","nao li nada");
        return null;
    }

    public LinkedList<Goalkeeper> getGoalkeepers(){
        // read the object from file
        // save the object to file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        LinkedList<Goalkeeper> gks = new LinkedList<Goalkeeper>();
        try {
            fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"GEDD-Goalkeepers-Data");
            in = new ObjectInputStream(fis);
            gks = (LinkedList<Goalkeeper>) in.readObject();
            in.close();
            return gks;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i("read false","nao li nada");
        return null;
    }

    private void fillTable(LinkedList<Player> players, LinkedList<Goalkeeper> gks){
        int counter = 0;
        int counterGK = 0;
        for (int i = 0, j = table.getChildCount(); i < j; i++) {
            View view2 = table.getChildAt(i);
            if (view2 instanceof TableRow) {

                TableRow row = (TableRow) view2;
                if(row.getChildAt(1) instanceof EditText) {
                    EditText name = (EditText) row.getChildAt(1);
                    EditText number = (EditText) row.getChildAt(2);

                    if(getIntent().getBooleanExtra("started",true)){
                        name.setKeyListener(null);
                        number.setKeyListener(null);
                    }

                    if((counter == 6 || counter == 7) && counterGK < gks.size()){

                        name.setText(gks.get(counterGK).getName());
                        number.setText(Integer.toString(gks.get(counterGK).getNumber()));

                        counterGK++;
                        counter ++;


                    }else{
                        if (counter < players.size()+counterGK && counter != 7) {

                            name.setText(players.get(counter-counterGK).getName());
                            number.setText(Integer.toString(players.get(counter-counterGK).getNumber()));

                            counter++;
                        }

                    }


                }


            }
        }
    }

}
