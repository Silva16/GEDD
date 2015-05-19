package pt.ipleiria.estg.GEDD;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.GEDD.Models.Game;
import pt.ipleiria.estg.GEDD.R;

public class MainActivity extends CustomActionBarActivity{


    ArrayList<Game> games = new ArrayList<Game>();
    Game game;
    GamesAdapter gamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ListView listView = (ListView) findViewById(R.id.listView);

        readSerializable();

        gamesAdapter = new GamesAdapter(this, games);

        listView.setAdapter(gamesAdapter);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Game item = (Game) listView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Selected : "+item.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("type", "load");
                intent.putExtra("game",item);
                startActivity(intent);
            }
        });

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
        if (id == R.id.new_game) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Get the layout inflater
            LayoutInflater inflater = this.getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout


            builder.setTitle("Novo Jogo");

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
            positiveButton.setOnClickListener(new NewGameListener(dialog));
        }

        if (id == R.id.uploadDrive) {
            selectAction(1);
        }

        if (id == R.id.changeDriveAccount) {
            selectAction(2);
        }

        return super.onOptionsItemSelected(item);
    }

    public class NewGameListener implements View.OnClickListener {
        private final Dialog dialog;
        public NewGameListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            // put your code here
            EditText myTeam = (EditText) dialog.findViewById(R.id.myTeamEditTextDialog);
            EditText advTeam = (EditText) dialog.findViewById(R.id.advTeamEditTextDialog);
            EditText local = (EditText) dialog.findViewById(R.id.localEditTextDialog);
            DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            TimePicker timePicker =(TimePicker) dialog.findViewById(R.id.timePicker);

            String myTeamString = myTeam.getText().toString();
            String advTeamString = advTeam.getText().toString();
            String localString = local.getText().toString();
            if(!myTeamString.isEmpty() || !advTeamString.isEmpty() || !localString.isEmpty()){
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("type", "new");
                intent.putExtra("myTeam",myTeam.getText().toString());
                intent.putExtra("advTeam",advTeam.getText().toString());
                intent.putExtra("local",local.getText().toString());
                intent.putExtra("hour", timePicker.getCurrentHour());
                intent.putExtra("minute", timePicker.getCurrentMinute());
                intent.putExtra("day",datePicker.getDayOfMonth());
                intent.putExtra("month",datePicker.getMonth());
                intent.putExtra("year",datePicker.getYear());
                startActivity(intent);
                dialog.dismiss();
            }else{
                Toast.makeText(MainActivity.this, "Os campos tem de estar preenchidos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readSerializable(){
        // read the object from file
        // save the object to file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"game-gedd.ser");
            in = new ObjectInputStream(fis);
            games = (ArrayList<Game>) in.readObject();
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readSerializable();
        gamesAdapter.games = games;
        gamesAdapter.notifyDataSetChanged();
    }


}
