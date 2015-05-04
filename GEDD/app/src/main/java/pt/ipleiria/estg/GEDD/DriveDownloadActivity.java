/*
package pt.ipleiria.estg.GEDD;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DriveDownloadActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = "android-drive-quickstart";
    private String EXISTING_FILE_ID;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    private GoogleApiClient mGoogleApiClient;

    */
/**
     * Create a new file and save it to Drive.
     *//*

    private void downloadFileFromDrive() {
        DriveFile file;
        // Start by creating a new contents, and setting a callback.
        Log.i(TAG, "Creating new contents.");
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveContentsResult>() {

                    @Override
                    public void onResult(DriveContentsResult result) {
                        // If the operation was not successful, we cannot do anything
                        // and must
                        // fail.
                        if (!result.getStatus().isSuccess()) {
                            Log.i(TAG, "Failed to create new contents.");
                            return;
                        }
                        final DriveContents driveContents = result.getDriveContents();

                        DriveContents contents = result.getDriveContents();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(contents.getInputStream()));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                builder.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String contentsAsString = builder.toString();

                    }

                });



    }

    final private ResultCallback<DriveFileResult> fileCallback = new
            ResultCallback<DriveFileResult>() {
                @Override
                public void onResult(DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i("driveActivity","Error while trying to create the file");
                        Intent intent = new Intent(DriveDownloadActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    Log.i("driveActivity","Created a file with content: " + result.getDriveFile().getDriveId());
                    Intent intent = new Intent(DriveDownloadActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
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
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        //new UploadFileTask().execute();

        DriveFolder folder = Drive.DriveApi.getRootFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "application/octet-stream"))
                .build();
        folder.queryChildren(mGoogleApiClient, query).setResultCallback(childrenRetrievedCallback);

        Drive.DriveApi.fetchDriveId(mGoogleApiClient, EXISTING_FILE_ID)
                .setResultCallback(idCallback);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    ResultCallback<MetadataBufferResult> childrenRetrievedCallback = new
            ResultCallback<MetadataBufferResult>() {
                @Override
                public void onResult(MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i(TAG,"Problem while retrieving files");
                        return;
                    }
                    Log.i(TAG, "Successfully listed files.");
                    EXISTING_FILE_ID = result.getMetadataBuffer().get(0).getDriveId().getResourceId();
                }
            };

    final private ResultCallback<DriveIdResult> idCallback = new ResultCallback<DriveIdResult>() {
        @Override
        public void onResult(DriveIdResult result) {
            new RetrieveDriveFileContentsAsyncTask(
                    RetrieveContentsActivity.this).execute(result.getDriveId());
        }
    };

    final private class RetrieveDriveFileContentsAsyncTask
            extends ApiClientAsyncTask<DriveId, Boolean, String> {

        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {
            String contents = null;
            DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(), params[0]);
            DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
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
                showMessage("Error while reading from the file");
                return;
            }
            showMessage("File contents: " + result);
        }
    }


}
*/
