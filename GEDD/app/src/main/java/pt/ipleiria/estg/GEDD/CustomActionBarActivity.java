package pt.ipleiria.estg.GEDD;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

    protected static final int DRIVE_ACTION_UPLOAD = 2;

    protected static final int DRIVE_ACTION_DOWNLOAD = 1;



    //GOOGLE DRIVE
    public void connectToDrive(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
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
        //saveFileToDrive();
        downloadFileFromDrive();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.

        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
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


                                FileInputStream fis = null;
                                ObjectInputStream in = null;
                                try {
                                    fis = new FileInputStream(getApplicationContext().getFilesDir().getPath().toString()+"game-gedd.ser");
                                    in = new ObjectInputStream(fis);
                                    Log.i("read True","Consegui Ler");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                OutputStream outputStream = driveContents.getOutputStream();

                                byte[] buffer = new byte[1024]; // Adjust if you want
                                int bytesRead;
                                assert in != null;
                                try {
                                    while ((bytesRead = in.read(buffer)) != -1)
                                    {
                                        try {
                                            outputStream.write(buffer, 0, bytesRead);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
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
        new Thread() {
            @Override
            public void run() {
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
                        Log.i(TAG, md.getTitle());
                    }
                }
            };

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

    public void startDrive(MenuItem item){
        connectToDrive();
    }
}
