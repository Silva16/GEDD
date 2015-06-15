package pt.ipleiria.estg.GEDD;

/**
 * Created by Andre on 10/06/2015.
 */
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class GmailApiAsyncTask extends AsyncTask<Void, Void, Void> {
private static GmailApiBase mActivity;

    static String TAG = "GMAIL ASYNC";

        /**
         * Constructor.
         * @param activity MainActivity that spawned this task.
         */
        GmailApiAsyncTask(GmailApiBase activity) {
        this.mActivity = activity;
        }

/**
 * Background task to call Gmail API.
 * @param params no parameters needed for this task.
 */
@Override
protected Void doInBackground(Void... params) {
        try {
            getStatistics();
            Log.i(TAG, "CENA 1");
        MimeMessage email = createEmail("andrerosado09@gmail.com","andrerosado09@gmail.com","Teste","Texto do teste");
            sendMessage(mActivity.mService,mActivity.credential.getSelectedAccountName(),email);
            Log.i(TAG, "CENA 1");

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
        mActivity.showGooglePlayServicesAvailabilityErrorDialog(
                availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
        mActivity.startActivityForResult(
                userRecoverableException.getIntent(),
                GmailApiBase.REQUEST_AUTHORIZATION);

        } catch (IOException e) {
        mActivity.updateStatus("The following error occurred: " +
                e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    return null;
        }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    public static MimeMessage createEmail(String to, String from, String subject,
                                          String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        InternetAddress tAddress = new InternetAddress(to);
        InternetAddress fAddress = new InternetAddress(from);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        //email.setText(bodyText);

        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        messageBodyPart.setText(bodyText);

        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(mActivity.getApplicationContext().getFilesDir().getPath().toString() + "/teste.txt");
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(mActivity.getApplicationContext().getFilesDir().getPath().toString() + "/teste.txt");
        multipart.addBodyPart(messageBodyPart);

        email.setContent(multipart);



        return email;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param email Email to be sent.
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendMessage(Gmail service, String userId, MimeMessage email)
            throws MessagingException, IOException {

        Message message = createMessageWithEmail(email);
        Log.i(TAG,"criei a message");
        message = service.users().messages().send("me", message).execute();

        Log.i(TAG,"Message id: " + message.getId());
        Log.i(TAG,message.toPrettyString());
    }

    /**
     * Create a Message from an email
     *
     * @param email Email to be set to raw of message
     * @return Message containing base64url encoded email.
     * @throws IOException
     * @throws MessagingException
     */
    public static Message createMessageWithEmail(MimeMessage email)
            throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        Log.i(TAG,"return message!");
        return message;
    }

    private void getStatistics(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(mActivity.getApplicationContext().getFilesDir().getPath().toString() + "teste.txt", "UTF-8");
            writer.println("The first line");
            writer.println("The second line");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }



}
