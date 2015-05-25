package pt.ipleiria.estg.GEDD;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import pt.ipleiria.estg.GEDD.Models.Game;


public class ListDriveGamesActivity extends CustomActionBarActivity {

    private ListView mResultsListView;
    private ResultsAdapter mResultsAdapter;
    private String TAG = "ListDriveGamesActivity";
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToDrive();

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        setContentView(R.layout.activity_list_drive_games);
        mResultsListView = (ListView) findViewById(R.id.listViewResults);
        mResultsAdapter = new ResultsAdapter(this);
        mResultsListView.setAdapter(mResultsAdapter);

        mResultsListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String item = mResultsAdapter.getItemString(position);
                final SynchronizeLocalFileWithDrive task = new SynchronizeLocalFileWithDrive();


                AlertDialog.Builder builder = new AlertDialog.Builder(ListDriveGamesActivity.this);
                builder.setTitle("Tem a certeza que deseja substituir?");
                //builder.setMessage("Ao substituir irá perder todos os seus dados, que não foram guardados.");
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task.execute(item);
                        Toast.makeText(ListDriveGamesActivity.this, "Selected : "+item, Toast.LENGTH_SHORT).show();
                    }
                });



                builder.show();



            }
        });


        DownloadFilesFromDrive task = new DownloadFilesFromDrive();
        task.execute();


        /*new Thread() {
            @Override
            public void run() {
                DriveId folderId = findDriveId("GEDD", "application/vnd.google-apps.folder", null);
                if (folderId == null) {
                    Toast.makeText(getApplicationContext(), "Não existem ficheiros para ser carregados",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "VOU CARREGAR A FOLDER");
                    DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), folderId);
                    Log.i(TAG, "VOU MOSTRAR AS CHILDREN");
                    folder.listChildren(getGoogleApiClient()).setResultCallback(childrenRetrievedCallback);
                }
            }
        }.start();*/
    }

    ResultCallback<DriveApi.MetadataBufferResult> childrenRetrievedCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Toast.makeText(getApplicationContext(), "Erro ao carregar os ficheiros",
                                Toast.LENGTH_LONG).show();
                    }

                    mResultsAdapter.clear();
                    mResultsAdapter.append(result.getMetadataBuffer());
                    mResultsAdapter.notifyDataSetChanged();
                }
            };




    // Async Task Class
    class DownloadFilesFromDrive extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            progressDialog = new ProgressDialog(ListDriveGamesActivity.this);
            progressDialog.setTitle("A obter lista de ficheiros");
            progressDialog.setMessage("Por Favor Aguarde ...");
            progressDialog.setView(findViewById(R.id.progress_circular));
            progressDialog.show();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... string) {
            DriveId folderId = findDriveId("GEDD", "application/vnd.google-apps.folder", null);
            if (folderId == null) {
                Toast.makeText(getApplicationContext(), "Não existem ficheiros para ser carregados",
                        Toast.LENGTH_LONG).show();
            } else {
                Log.i(TAG, "VOU CARREGAR A FOLDER");
                DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), folderId);
                Log.i(TAG, "VOU MOSTRAR AS CHILDREN");
                folder.listChildren(getGoogleApiClient()).setResultCallback(childrenRetrievedCallback);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Lista de jogos da drive descarregados", Toast.LENGTH_LONG).show();
        }
    }

    class SynchronizeLocalFileWithDrive extends AsyncTask<String, String, String> {


        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            progressDialog = new ProgressDialog(ListDriveGamesActivity.this);
            progressDialog.setTitle("A sincronizar o ficheiro localmente");
            progressDialog.setMessage("Por Favor Aguarde ...");
            progressDialog.setView(findViewById(R.id.progress_circular));
            progressDialog.show();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... title) {
            Log.i(TAG,"Vou escrever ficheiro");
            DriveId folderId = findDriveId("GEDD", "application/vnd.google-apps.folder", null);
            Log.i(TAG,"Vou encontrei folderID");
            DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), folderId);
            Log.i(TAG,"Vou encontrei folder");
            DriveId fileId = findDriveId(title[0], "application/octet-stream", folder);
            Log.i(TAG,"Vou encontrei fileID");

            DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(), fileId);
            Log.i(TAG,"Vou encontrei file");

            DriveApi.DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();

            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }

            String filename = "game-gedd.ser";
            // save the object to file
            FileOutputStream fos = null;
            ObjectOutputStream out = null;

            DriveContents driveContents = driveContentsResult.getDriveContents();
            InputStream inputStream = driveContents.getInputStream();

            try {
                Log.i(TAG,"Entrei no try");
                ObjectInputStream in = new ObjectInputStream(inputStream);
                ArrayList<Game> games = (ArrayList<Game>) in.readObject();

                fos = new FileOutputStream(getApplicationContext().getFilesDir().getPath().toString() + filename);
                out = new ObjectOutputStream(fos);
                out.writeObject(games);
                out.close();
                fos.close();
                inputStream.close();

                Log.i(TAG,"Escrevi ficheiro");


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            getGoogleApiClient().disconnect();
            return null;
        }

        @Override
        protected void onPostExecute(String name) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Jogos sincronizados localmente", Toast.LENGTH_LONG).show();
        }
    }

    ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    Log.i(TAG,"Entrei no callback");
                    if (!result.getStatus().isSuccess()) {
                        // display an error saying file can't be opened
                        Log.i(TAG,"Insucesso");
                        return;
                    }
                    // DriveContents object contains pointers
                    // to the actual byte stream



                    String filename = "game-gedd.ser";
                    // save the object to file
                    FileOutputStream fos = null;
                    ObjectOutputStream out = null;

                    DriveContents driveContents = result.getDriveContents();
                    InputStream inputStream = driveContents.getInputStream();

                    try {
                        Log.i(TAG,"Entrei no try");
                        ObjectInputStream in = new ObjectInputStream(inputStream);
                        ArrayList<Game> games = (ArrayList<Game>) in.readObject();

                        fos = new FileOutputStream(getApplicationContext().getFilesDir().getPath().toString() + filename);
                        out = new ObjectOutputStream(fos);
                        out.writeObject(games);
                        out.close();
                        fos.close();
                        inputStream.close();

                        Log.i(TAG,"Escrevi ficheiro");


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            };


}
