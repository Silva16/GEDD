package pt.ipleiria.estg.GEDD;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
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
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;


public class DriveActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener {

    private static final String TAG = "android-drive-quickstart";
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    private GoogleApiClient mGoogleApiClient;
    private Bitmap mBitmapToSave;

    /**
     * Create a new file and save it to Drive.
     */
    private void saveFileToDrive() {
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

                        // Perform I/O off the UI thread.
                        new Thread() {
                            @Override
                            public void run() {
                                // write content to DriveContents
                                OutputStream outputStream = driveContents.getOutputStream();
                                Writer writer = new OutputStreamWriter(outputStream);
                                try {
                                    writer.write("Hello World!");
                                    writer.close();
                                } catch (IOException e) {
                                    Log.e(TAG, e.getMessage());
                                }

                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle("New file")
                                        .setMimeType("text/plain")
                                        .setStarred(true).build();

                                // create a file on root folder
                                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                        .createFile(mGoogleApiClient, changeSet, driveContents)
                                        .setResultCallback(fileCallback);
                            }
                        }.start();
                    }
                });
    }

    final private ResultCallback<DriveFileResult> fileCallback = new
            ResultCallback<DriveFileResult>() {
                @Override
                public void onResult(DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i("driveActivity","Error while trying to create the file");
                        return;
                    }
                    Log.i("driveActivity","Created a file with content: " + result.getDriveFile().getDriveId());
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
        // Connect the client. Once connected, the camera is launched.
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
        saveFileToDrive();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }
}
