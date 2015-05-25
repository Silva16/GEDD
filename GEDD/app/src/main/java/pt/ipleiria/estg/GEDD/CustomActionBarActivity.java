package pt.ipleiria.estg.GEDD;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.metadata.CustomPropertyKey;
import com.google.android.gms.drive.query.Filter;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.plus.Plus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import pt.ipleiria.estg.GEDD.Models.Game;

/**
 * Created by Andre on 08/05/2015.
 */
public class CustomActionBarActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private static final String TAG = "Action Bar";

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Next available request code.
     */
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;

    protected int driveActionType = 0;

    protected static final int DRIVE_ACTION_CHANGE = 3;

    protected static final int DRIVE_ACTION_UPLOAD = 1;

    protected static final int DRIVE_ACTION_DOWNLOAD = 2;

    private int driveAction;

    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    private LinkedList<String> driveFiles = new LinkedList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
    }



    //GOOGLE DRIVE
    public void connectToDrive(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addApi(Plus.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        switch(driveAction){
            case 1: saveFileToDrive();
                break;
            case 2: downloadFileFromDrive();
                break;
            case 3: changeAccount();
                break;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.

        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        }else if(!result.hasResolution()) {
            Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
            // show the localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            mResolvingError = true;
            Log.e(TAG, "Exception while starting resolution activity", e);
            mGoogleApiClient.connect();
        }
    }



    private void saveFileToDrive() {
        // Start by creating a new contents, and setting a callback.
        Log.i(TAG, "Creating new contents.");
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        // If the operation was not successful, we cannot do anything
                        // and must
                        // fail.
                        if (!result.getStatus().isSuccess()) {
                            Log.i(TAG, "Failed to create new contents.");
                            return;
                        }
                        final DriveContents driveContents = result.getDriveContents();

                        // Perform I/O off the UI thread.
                        new Thread() {
                            @Override
                            public void run() {
                                // write content to DriveContents

                                ArrayList<Game> games = new ArrayList<Game>();

                                FileInputStream fis = null;
                                ObjectInputStream in = null;
                                try {
                                    fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"game-gedd.ser");
                                    in = new ObjectInputStream(fis);

                                    games = (ArrayList<Game>) in.readObject();

                                    Log.i("read True","Consegui Ler");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }



                                OutputStream outputStream = driveContents.getOutputStream();

                                try {

                                    ObjectOutputStream out = new ObjectOutputStream(outputStream);
                                    out.writeObject(games);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                DriveId dId;
                                if((dId = findDriveId("GEDD","application/vnd.google-apps.folder",null)) == null ){
                                    createFolder("GEDD");
                                    dId = findDriveId("GEDD","application/vnd.google-apps.folder",null);

                                }

                                Time now = new Time();
                                now.setToNow();

                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle("GEDD #"+now.hour+":"+now.minute+":"+now.second+" - "+now.monthDay+"/"+now.month+"/"+now.year)
                                        .setMimeType("application/octet-stream")
                                        .setStarred(true).build();

                                DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, dId);

                                // create a file on root folder
                                folder
                                        .createFile(mGoogleApiClient, changeSet, driveContents)
                                        .setResultCallback(fileCallback);


                            }
                        }.start();


                    }

                });



    }

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i("driveActivity","Error while trying to create the file");
                        Toast.makeText(getApplicationContext(), "Erro ao gravar ficheiro!!!",
                                Toast.LENGTH_LONG).show();
                        mGoogleApiClient.disconnect();
                    }
                    Log.i("driveActivity","Created a file with content: " + result.getDriveFile().getDriveId());
                    Toast.makeText(getApplicationContext(), "Ficheiro gravado com sucesso.",
                            Toast.LENGTH_LONG).show();
                    mGoogleApiClient.disconnect();
                }
            };

    final private ResultCallback<DriveApi.MetadataBufferResult> metadataCallback =
            new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i(TAG, "Problem while retrieving results");
                        return;
                    }
                    result.getMetadataBuffer().get(0).getDriveId();
                }
            };

    //find files, folders. ANY 'null' ARGUMENT VALUE MEANS 'any'
    public DriveId findDriveId(String title, String mime, DriveFolder fldr) {
        DriveId dId = null;
        ArrayList<Filter> fltrs = new ArrayList<Filter>();
        fltrs.add(Filters.eq(SearchableField.TRASHED, false));
        if (title != null) fltrs.add(Filters.eq(SearchableField.TITLE, title));
        if (mime != null) fltrs.add(Filters.eq(SearchableField.MIME_TYPE, mime));
        Query qry = new Query.Builder().addFilter(Filters.and(fltrs)).build();
        MetadataBufferResult rslt = (fldr == null) ?
                Drive.DriveApi.query(mGoogleApiClient, qry).await() :
                fldr.queryChildren(mGoogleApiClient, qry).await();
        if (rslt.getStatus().isSuccess()) {
            MetadataBuffer mdb = null;
            try {
                mdb = rslt.getMetadataBuffer();
                if (mdb != null) {
                    for (Metadata md : mdb) {
                        if (md == null) continue;
                        dId = md.getDriveId();      // here is the "Drive ID"
                        String titleIn = md.getTitle();
                        String mimeIn = md.getMimeType();
                    }
                }
            } finally {
                if (mdb != null) mdb.close();

            }
        }
        return dId;
    }

    private void createFolder(String title){
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(title).build();
        Drive.DriveApi.getRootFolder(mGoogleApiClient).createFolder(
                mGoogleApiClient, changeSet);
    }

    private void downloadFileFromDrive(){

        Log.i(TAG, "PROCURAR A FOLDER");
        DriveId folderId = findDriveId("GEDD", "application/vnd.google-apps.folder", null);
        if (folderId == null) {
            Toast.makeText(getApplicationContext(), "Não existem ficheiros para ser carregados",
                    Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "VOU CARREGAR A FOLDER");
            DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, folderId);
            Log.i(TAG, "VOU MOSTRAR AS CHILDREN");
            folder.listChildren(mGoogleApiClient).setResultCallback(childrenRetrievedCallback);
        }

        new Thread() {
            @Override
            public void run() {
                DriveId folderId = findDriveId("GEDD", "application/vnd.google-apps.folder", null);
                DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, folderId);
                DriveId fileId = findDriveId("GEDD #18:41:7 - 19/4/2015", "application/octet-stream", folder);

                DriveFile file = Drive.DriveApi.getFile(mGoogleApiClient, fileId);

                //file.open(mGoogleApiClient,DriveFile.MODE_READ_ONLY, null).setResultCallback(contentsOpenedCallback);

                mGoogleApiClient.disconnect();


            }

        }.start();

    }

    ResultCallback<MetadataBufferResult> childrenRetrievedCallback = new
            ResultCallback<MetadataBufferResult>() {
                @Override
                public void onResult(MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Toast.makeText(getApplicationContext(), "Erro ao carregar os ficheiros",
                                Toast.LENGTH_LONG).show();
                    }

                    for(Metadata md : result.getMetadataBuffer()){
                        if(md.isTrashed()){
                            Log.i(TAG, md.getTitle()+" - LIXO");
                        }else {
                            Log.i(TAG, md.getTitle());
                            driveFiles.add(md.getTitle());
                        }
                    }
                }
            };

   /* ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        // display an error saying file can't be opened
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
            };*/

   /* final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            new RetrieveDriveFileContentsAsyncTask(MainActivity.this).execute(result.getDriveId());
        }
    };

    final private class RetrieveDriveFileContentsAsyncTask
            extends DriveAsyncTask<DriveId, Boolean, String> {

        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {
            String contents = null;
            DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(), params[0]);
            DriveApi.DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();


            String filename = "game-gedd.ser";

            // save the object to file
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            Log.i("onDestroy","Entrei no on destroy");



            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(driveContents.getInputStream());
                Log.i("read True","Consegui Ler");

                try {
                    Log.i("onDestroy","1");
                    fos = new FileOutputStream(getApplicationContext().getFilesDir().getPath().toString()+filename);
                    Log.i("onDestroy","2");
                    out = new ObjectOutputStream(fos);
                    Log.i("onDestroy","3");
                    out.writeObject(in);
                    Log.i("onDestroy","4");

                    out.close();

                    Log.i("onDestroy","Detrui cenas");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }






            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(driveContents.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                contents = builder.toString();
            } catch (IOException e) {
                Log.e(TAG, "IOException while reading from the stream", e);
            }

            driveContents.discard(getGoogleApiClient());
            return contents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Log.i(TAG,"Error while reading from the file");
                return;
            }
            Log.i(TAG,"File contents: " + result);


        }
    }*/

    public void selectAction(int action){
        driveAction = action;
        connectToDrive();
    }


    private void changeAccount(){
        if (mGoogleApiClient.isConnected()) {
     //       mGoogleApiClient.clearDefaultAccountAndReconnect();
       //     mGoogleApiClient.disconnect();
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    public GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }

}
