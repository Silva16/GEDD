package pt.ipleiria.estg.GEDD;

/**
 * Created by Andre on 10/06/2015.
 */
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;


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
import java.util.LinkedList;
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

import pt.ipleiria.estg.GEDD.Models.Game;
import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;


public class GmailApiAsyncTask extends AsyncTask<GmailApiTaskParams, Void, Void> {
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
protected Void doInBackground(GmailApiTaskParams... params) {
        try {
            LinkedList<Player> players = params[0].players;
            Game game = params[0].game;
            LinkedList<Goalkeeper> gks = params[0].gks;
            LinkedList<String> filenames = new LinkedList<String>();
            Log.i(TAG,game.getName());
            filenames.add(createGlobalStatistics(players, game, gks));
            for(Player player : players) {
                filenames.add(createPlayerStatistics(player, game));
            }

            for(Goalkeeper gk : gks){
                filenames.add(createGoalkeeperReport(gk,game));
            }
            Log.i(TAG, "CENA 1");
        MimeMessage email = createEmail("andrerosado09@gmail.com","andrerosado09@gmail.com","Teste","Texto do teste", filenames);
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
                                          String bodyText, LinkedList<String> filenames) throws MessagingException {
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

        for(String filename : filenames) {
            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(mActivity.getApplicationContext().getFilesDir().getPath().toString() + filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
        }

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

    private String createGlobalStatistics(LinkedList<Player> players, Game game, LinkedList<Goalkeeper> gks){
        PrintWriter writer = null;
        int zone_goals[] = new int[10];
        int zone_out[] = new int[10];
        int zone_blocked[] = new int[10];
        int zone_defended[] = new int[10];
        int zone_post[] = new int[10];
        int zone_all[] = new int[10];

        int zone_ca_goals[] = new int[10];
        int zone_ca_out[] = new int[10];
        int zone_ca_blocked[] = new int[10];
        int zone_ca_defended[] = new int[10];
        int zone_ca_post[] = new int[10];
        int zone_ca_all[] = new int[10];

        int zone_def_block[] = new int[10];
        int zone_def_disarm[] = new int[10];
        int zone_def_int[] = new int[10];
        int zone_def_all[] = new int[10];

        int assists = 0;
        int tecnichalFail = 0;
        int advTecnichalFail = game.getTechnicalFailAdv();

        for(Player player : players){

            zone_goals[0] += player.getAllAtkShotsGoal();
            zone_out[0] += player.getAllAtkShotsOut();
            zone_blocked[0] += player.getAllAtkShotsBlocked();
            zone_defended[0] += player.getAllAtkShotsDefended();
            zone_post[0] += player.getAllAtkShotsPost();
            zone_all[0] += player.getAllAtkShots();

            zone_ca_goals[0] += player.getAllCaShotsGoal();
            zone_ca_out[0] += player.getAllCaShotsOut();
            zone_ca_blocked[0] += player.getAllCaShotsBlocked();
            zone_ca_defended[0] += player.getAllCaShotsDefended();
            zone_ca_post[0] += player.getAllCaShotsPost();
            zone_ca_all[0] += player.getAllCaShots();

            zone_def_block[0] = player.getAllBlocked();
            zone_def_disarm[0] = player.getAllDisarms();
            zone_def_int[0] = player.getAllInterceptions();
            zone_def_all[0] = player.getAllBlocked()+player.getAllDisarms()+player.getAllInterceptions();



            for (int i = 1; i<10; i++){
                zone_goals[i] += player.getZoneAtkGoals(i);
                zone_out[i] += player.getZoneAtkShotsOut(i);
                zone_blocked[i] += player.getZoneAtkShotsBlocked(i);
                zone_defended[i] += player.getZoneAtkShotsDefended(i);
                zone_post[i] += player.getZoneAtkShotsPost(i);
                zone_all[i] += player.getZoneAtkShots(i);

                zone_ca_goals[i] += player.getZoneCAGoals(i);
                zone_ca_out[i] += player.getZoneCaShotsOut(i);
                zone_ca_blocked[i] += player.getZoneCaShotsBlocked(i);
                zone_ca_defended[i] += player.getZoneCaShotsDefended(i);
                zone_ca_post[i] += player.getZoneCaShotsPost(i);
                zone_ca_all[i] += player.getZoneCAShots(i);

                zone_def_block[i] = player.getAllBlocked();
                zone_def_disarm[i] = player.getAllDisarms();
                zone_def_int[i] = player.getAllInterceptions();
                zone_def_all[i] = player.getAllBlocked()+player.getAllDisarms()+player.getAllInterceptions();

                assists = player.getAssistance();
                tecnichalFail = player.getTechnicalFailure();


            }
        }
        try {
            String filename = game.getMyTeam()+"vs"+game.getOpponent()+" -- Geral.txt";
            writer = new PrintWriter(mActivity.getApplicationContext().getFilesDir().getPath().toString() + filename, "UTF-8");
            writer.println("                    ==================================//Dados Jogo\\\\==================================");
            writer.println("                    "+game.getMyTeam()+" "+game.getScoreMyTeam()+":"+game.getScoreOpponent()+" "+game.getOpponent());
            writer.println("                    Local: "+game.getLocal());
            writer.println("                    Data: "+game.getDate()+" Hora: "+game.getTime());
            writer.println("                    ================================//Estatisticas Jogo\\\\================================");
            writer.println("                                    ==============|| Acções Ofensivas ||==============");

            writer.println("\n                                              ////Ataques e Contra Ataques\\\\\\\\");


            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(zone_goals[0],zone_ca_goals[0])+"||"+getTableCell(zone_goals[1],zone_ca_goals[1])+"||"+getTableCell(zone_goals[2],zone_ca_goals[2])+"||"+getTableCell(zone_goals[3],zone_ca_goals[3])+"||"+getTableCell(zone_goals[4],zone_ca_goals[4])+"||"+getTableCell(zone_goals[5],zone_ca_goals[5])+"||"+getTableCell(zone_goals[6],zone_ca_goals[6])+"||"+getTableCell(zone_goals[7],zone_ca_goals[7])+"||"+getTableCell(zone_goals[8],zone_ca_goals[8])+"||"+getTableCell(zone_goals[9],zone_ca_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(zone_defended[0],zone_ca_defended[0])+"||"+getTableCell(zone_defended[1],zone_ca_defended[1])+"||"+getTableCell(zone_defended[2],zone_ca_defended[2])+"||"+getTableCell(zone_defended[3],zone_ca_defended[3])+"||"+getTableCell(zone_defended[4],zone_ca_defended[4])+"||"+getTableCell(zone_defended[5],zone_ca_defended[5])+"||"+getTableCell(zone_defended[6],zone_ca_defended[6])+"||"+getTableCell(zone_defended[7],zone_ca_defended[7])+"||"+getTableCell(zone_defended[8],zone_ca_defended[8])+"||"+getTableCell(zone_defended[9],zone_ca_defended[9])+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(zone_blocked[0],zone_ca_blocked[0])+"||"+getTableCell(zone_blocked[1],zone_ca_blocked[1])+"||"+getTableCell(zone_blocked[2],zone_ca_blocked[2])+"||"+getTableCell(zone_blocked[3],zone_ca_blocked[3])+"||"+getTableCell(zone_blocked[4],zone_ca_blocked[4])+"||"+getTableCell(zone_blocked[5],zone_ca_blocked[5])+"||"+getTableCell(zone_blocked[6],zone_ca_blocked[6])+"||"+getTableCell(zone_blocked[7],zone_ca_blocked[7])+"||"+getTableCell(zone_blocked[8],zone_ca_blocked[8])+"||"+getTableCell(zone_blocked[9],zone_ca_blocked[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(zone_out[0],zone_ca_out[0])+"||"+getTableCell(zone_out[1],zone_ca_out[1])+"||"+getTableCell(zone_out[2],zone_ca_out[2])+"||"+getTableCell(zone_out[3],zone_ca_out[3])+"||"+getTableCell(zone_out[4],zone_ca_out[4])+"||"+getTableCell(zone_out[5],zone_ca_out[5])+"||"+getTableCell(zone_out[6],zone_ca_out[6])+"||"+getTableCell(zone_out[7],zone_ca_out[7])+"||"+getTableCell(zone_out[8],zone_ca_out[8])+"||"+getTableCell(zone_out[9],zone_ca_out[9])+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(zone_post[0],zone_ca_post[0])+"||"+getTableCell(zone_post[1],zone_ca_post[1])+"||"+getTableCell(zone_post[2],zone_ca_post[2])+"||"+getTableCell(zone_post[3],zone_ca_post[3])+"||"+getTableCell(zone_post[4],zone_ca_post[4])+"||"+getTableCell(zone_post[5],zone_ca_post[5])+"||"+getTableCell(zone_post[6],zone_ca_post[6])+"||"+getTableCell(zone_post[7],zone_ca_post[7])+"||"+getTableCell(zone_post[8],zone_ca_post[8])+"||"+getTableCell(zone_post[9],zone_ca_post[9])+"||");
            writer.println("            Eficácia||"+getReverseEfficciencyString(zone_goals[0] + zone_ca_goals[0], zone_all[0] + zone_ca_all[0])+"||"+getEfficciencyString(zone_goals[1] + zone_ca_goals[1], zone_all[1] + zone_ca_all[1])+"||"+getEfficciencyString(zone_goals[2] + zone_ca_goals[2], zone_all[2] + zone_ca_all[2])+"||"+getEfficciencyString(zone_goals[3] + zone_ca_goals[3], zone_all[3] + zone_ca_all[3])+"||"+getEfficciencyString(zone_goals[4] + zone_ca_goals[4], zone_all[4] + zone_ca_all[4])+"||"+getEfficciencyString(zone_goals[5] + zone_ca_goals[5], zone_all[5] + zone_ca_all[5])+"||"+getEfficciencyString(zone_goals[6] + zone_ca_goals[6], zone_all[6] + zone_ca_all[6])+"||"+getEfficciencyString(zone_goals[7] + zone_ca_goals[7], zone_all[7] + zone_ca_all[7])+"||"+getEfficciencyString(zone_goals[8] + zone_ca_goals[8], zone_all[8] + zone_ca_all[8])+"||"+getEfficciencyString(zone_goals[9] + zone_ca_goals[9], zone_all[9] + zone_ca_all[9])+"||");
            writer.println("                    ==================================================================================");



            //////////////////////////////ATAQUES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            writer.println("\n                                                  ////Ataques\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(zone_goals[0],0)+"||"+getTableCell(zone_goals[1],0)+"||"+getTableCell(zone_goals[2],0)+"||"+getTableCell(zone_goals[3],0)+"||"+getTableCell(zone_goals[4],0)+"||"+getTableCell(zone_goals[5],0)+"||"+getTableCell(zone_goals[6],0)+"||"+getTableCell(zone_goals[7],0)+"||"+getTableCell(zone_goals[8],0)+"||"+getTableCell(zone_goals[9],0)+"||");
            writer.println("  Remates Defendidos||"+getTableCell(zone_defended[0],0)+"||"+getTableCell(zone_defended[1],0)+"||"+getTableCell(zone_defended[2],0)+"||"+getTableCell(zone_defended[3],0)+"||"+getTableCell(zone_defended[4],0)+"||"+getTableCell(zone_defended[5],0)+"||"+getTableCell(zone_defended[6],0)+"||"+getTableCell(zone_defended[7],0)+"||"+getTableCell(zone_defended[8],0)+"||"+getTableCell(zone_defended[9],0)+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(zone_blocked[0],0)+"||"+getTableCell(zone_blocked[1],0)+"||"+getTableCell(zone_blocked[2],0)+"||"+getTableCell(zone_blocked[3],0)+"||"+getTableCell(zone_blocked[4],0)+"||"+getTableCell(zone_blocked[5],0)+"||"+getTableCell(zone_blocked[6],0)+"||"+getTableCell(zone_blocked[7],0)+"||"+getTableCell(zone_blocked[8],0)+"||"+getTableCell(zone_blocked[9],0)+"||");
            writer.println("        Remates Fora||"+getTableCell(zone_out[0],0)+"||"+getTableCell(zone_out[1],0)+"||"+getTableCell(zone_out[2],0)+"||"+getTableCell(zone_out[3],0)+"||"+getTableCell(zone_out[4],0)+"||"+getTableCell(zone_out[5],0)+"||"+getTableCell(zone_out[6],0)+"||"+getTableCell(zone_out[7],0)+"||"+getTableCell(zone_out[8],0)+"||"+getTableCell(zone_out[9],0)+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(zone_post[0],0)+"||"+getTableCell(zone_post[1],0)+"||"+getTableCell(zone_post[2],0)+"||"+getTableCell(zone_post[3],0)+"||"+getTableCell(zone_post[4],0)+"||"+getTableCell(zone_post[5],0)+"||"+getTableCell(zone_post[6],0)+"||"+getTableCell(zone_post[7],0)+"||"+getTableCell(zone_post[8],0)+"||"+getTableCell(zone_post[9],0)+"||");
            writer.println("            Eficácia||"+getEfficciencyString(zone_goals[0],zone_all[0])+"||"+getEfficciencyString(zone_goals[1],zone_all[1])+"||"+getEfficciencyString(zone_goals[2],zone_all[2])+"||"+getEfficciencyString(zone_goals[3],zone_all[3])+"||"+getEfficciencyString(zone_goals[4],zone_all[4])+"||"+getEfficciencyString(zone_goals[5],zone_all[5])+"||"+getEfficciencyString(zone_goals[6],zone_all[6])+"||"+getEfficciencyString(zone_goals[7],zone_all[7])+"||"+getEfficciencyString(zone_goals[8],zone_all[8])+"||"+getEfficciencyString(zone_goals[9],zone_all[9])+"||");
            writer.println("                    ==================================================================================");



            //////////////////////////////CONTRA ATAQUES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            writer.println("\n                                              ////Contra Ataques\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(0,zone_ca_goals[0])+"||"+getTableCell(0,zone_ca_goals[1])+"||"+getTableCell(0,zone_ca_goals[2])+"||"+getTableCell(0,zone_ca_goals[3])+"||"+getTableCell(0,zone_ca_goals[4])+"||"+getTableCell(0,zone_ca_goals[5])+"||"+getTableCell(0,zone_ca_goals[6])+"||"+getTableCell(zone_goals[7],zone_ca_goals[7])+"||"+getTableCell(0,zone_ca_goals[8])+"||"+getTableCell(0,zone_ca_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(0,zone_ca_defended[0])+"||"+getTableCell(0,zone_ca_defended[1])+"||"+getTableCell(0,zone_ca_defended[2])+"||"+getTableCell(0,zone_ca_defended[3])+"||"+getTableCell(0,zone_ca_defended[4])+"||"+getTableCell(0,zone_ca_defended[5])+"||"+getTableCell(0,zone_ca_defended[6])+"||"+getTableCell(0,zone_ca_defended[7])+"||"+getTableCell(0,zone_ca_defended[8])+"||"+getTableCell(0,zone_ca_defended[9])+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(0,zone_ca_blocked[0])+"||"+getTableCell(0,zone_ca_blocked[1])+"||"+getTableCell(0,zone_ca_blocked[2])+"||"+getTableCell(0,zone_ca_blocked[3])+"||"+getTableCell(0,zone_ca_blocked[4])+"||"+getTableCell(0,zone_ca_blocked[5])+"||"+getTableCell(0,zone_ca_blocked[6])+"||"+getTableCell(0,zone_ca_blocked[7])+"||"+getTableCell(0,zone_ca_blocked[8])+"||"+getTableCell(0,zone_ca_blocked[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(0,zone_ca_out[0])+"||"+getTableCell(0,zone_ca_out[1])+"||"+getTableCell(0,zone_ca_out[2])+"||"+getTableCell(0,zone_ca_out[3])+"||"+getTableCell(0,zone_ca_out[4])+"||"+getTableCell(0,zone_ca_out[5])+"||"+getTableCell(0,zone_ca_out[6])+"||"+getTableCell(0,zone_ca_out[7])+"||"+getTableCell(0,zone_ca_out[8])+"||"+getTableCell(0,zone_ca_out[9])+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(0,zone_ca_post[0])+"||"+getTableCell(0,zone_ca_post[1])+"||"+getTableCell(0,zone_ca_post[2])+"||"+getTableCell(0,zone_ca_post[3])+"||"+getTableCell(0,zone_ca_post[4])+"||"+getTableCell(0,zone_ca_post[5])+"||"+getTableCell(0,zone_ca_post[6])+"||"+getTableCell(0,zone_ca_post[7])+"||"+getTableCell(zone_post[8],zone_ca_post[8])+"||"+getTableCell(0,zone_ca_post[9])+"||");
            writer.println("            Eficácia||"+getEfficciencyString(zone_ca_goals[0],zone_ca_all[0])+"||"+getEfficciencyString(zone_ca_goals[1],zone_ca_all[1])+"||"+getEfficciencyString(zone_ca_goals[2],zone_ca_all[2])+"||"+getEfficciencyString(zone_ca_goals[3],zone_ca_all[3])+"||"+getEfficciencyString(zone_ca_goals[4],zone_ca_all[4])+"||"+getEfficciencyString(zone_ca_goals[5],zone_ca_all[5])+"||"+getEfficciencyString(zone_ca_goals[6],zone_ca_all[6])+"||"+getEfficciencyString(zone_ca_goals[7],zone_ca_all[7])+"||"+getEfficciencyString(zone_ca_goals[8],zone_ca_all[8])+"||"+getEfficciencyString(zone_ca_goals[9],zone_ca_all[9])+"||");
            writer.println("                    ==================================================================================");

            writer.println("\n                                              ////Acções Defensivas\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("              Blocos||"+getTableCell(0, zone_def_block[0])+"||"+getTableCell(0,zone_def_block[1])+"||"+getTableCell(0,zone_def_block[2])+"||"+getTableCell(0,zone_def_block[3])+"||"+getTableCell(0,zone_def_block[4])+"||"+getTableCell(0,zone_def_block[5])+"||"+getTableCell(0,zone_def_block[6])+"||"+getTableCell(zone_goals[7],zone_def_block[7])+"||"+getTableCell(0,zone_def_block[8])+"||"+getTableCell(0,zone_def_block[9])+"||");
            writer.println("            Desarmes||"+getTableCell(0,zone_def_disarm[0])+"||"+getTableCell(0,zone_def_disarm[1])+"||"+getTableCell(0,zone_def_disarm[2])+"||"+getTableCell(0,zone_def_disarm[3])+"||"+getTableCell(0,zone_def_disarm[4])+"||"+getTableCell(0,zone_def_disarm[5])+"||"+getTableCell(0,zone_def_disarm[6])+"||"+getTableCell(0,zone_def_disarm[7])+"||"+getTableCell(0,zone_def_disarm[8])+"||"+getTableCell(0,zone_def_disarm[9])+"||");
            writer.println("        Intercepções||"+getTableCell(0,zone_def_int[0])+"||"+getTableCell(0,zone_def_int[1])+"||"+getTableCell(0,zone_def_int[2])+"||"+getTableCell(0,zone_def_int[3])+"||"+getTableCell(0,zone_def_int[4])+"||"+getTableCell(0,zone_def_int[5])+"||"+getTableCell(0,zone_def_int[6])+"||"+getTableCell(0,zone_def_int[7])+"||"+getTableCell(0,zone_def_int[8])+"||"+getTableCell(0,zone_def_int[9])+"||");
           writer.println("                    ==================================================================================");


            writer.println("\n                                              ////Outras Acções\\\\\\\\");
            writer.println("\n                  Assistências:"+assists);
            writer.println("\n                  Falhas Técnicas:"+tecnichalFail);
            writer.println("\n                  Falhas Técnicas Adversário:"+advTecnichalFail);

            int zone_def_goals[] = new int[10];
            int zone_def_defended[] = new int[10];
            int zone_def_out[] = new int[10];
            int zone_def_post[] = new int[10];
            int zone_def_adv_all[] = new int[10];
            for(Goalkeeper gk : gks){
                for(int i = 1; i<10; i++){

                    zone_def_out[0] += zone_def_out[i] = gk.getZoneOutShots(i);
                    zone_def_post[0] += zone_def_post[i] = gk.getZonePostShots(i);
                    for (int j = 1; j<10; j++){
                        zone_def_goals[0] +=gk.getZoneAllGoals(i,j);
                        zone_def_goals[i] += gk.getZoneAllGoals(i,j);
                        zone_def_defended[0] += gk.getZoneAllDefended(i,j);
                        zone_def_defended[i] += gk.getZoneAllDefended(i,j);
                    }

                    zone_def_adv_all[i] = zone_def_goals[i]+zone_def_defended[i]+zone_def_post[i]+zone_def_out[i];
                }
            }

            zone_def_adv_all[0] = zone_def_goals[0]+zone_def_defended[0]+zone_def_post[0]+zone_def_out[0];

            writer.println("\n                                              ////Acções Guarda Redes por Zona\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(0, zone_def_goals[0])+"||"+getTableCell(0,zone_def_goals[1])+"||"+getTableCell(0,zone_def_goals[2])+"||"+getTableCell(0,zone_def_goals[3])+"||"+getTableCell(0,zone_def_goals[4])+"||"+getTableCell(0,zone_def_goals[5])+"||"+getTableCell(0,zone_def_goals[6])+"||"+getTableCell(zone_goals[7],zone_def_goals[7])+"||"+getTableCell(0,zone_def_goals[8])+"||"+getTableCell(0,zone_def_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(0,zone_def_defended[0])+"||"+getTableCell(0,zone_def_defended[1])+"||"+getTableCell(0,zone_def_defended[2])+"||"+getTableCell(0,zone_def_defended[3])+"||"+getTableCell(0,zone_def_defended[4])+"||"+getTableCell(0,zone_def_defended[5])+"||"+getTableCell(0,zone_def_defended[6])+"||"+getTableCell(0,zone_def_defended[7])+"||"+getTableCell(0,zone_def_defended[8])+"||"+getTableCell(0,zone_def_defended[9])+"||");
            writer.println("       Remates Poste||"+getTableCell(0,zone_def_out[0])+"||"+getTableCell(0,zone_def_out[1])+"||"+getTableCell(0,zone_def_out[2])+"||"+getTableCell(0,zone_def_out[3])+"||"+getTableCell(0,zone_def_out[4])+"||"+getTableCell(0,zone_def_out[5])+"||"+getTableCell(0,zone_def_out[6])+"||"+getTableCell(0,zone_def_out[7])+"||"+getTableCell(0,zone_def_out[8])+"||"+getTableCell(0,zone_def_out[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(0,zone_def_post[0])+"||"+getTableCell(0,zone_def_post[1])+"||"+getTableCell(0,zone_def_post[2])+"||"+getTableCell(0,zone_def_post[3])+"||"+getTableCell(0,zone_def_post[4])+"||"+getTableCell(0,zone_def_post[5])+"||"+getTableCell(0,zone_def_post[6])+"||"+getTableCell(0,zone_def_post[7])+"||"+getTableCell(0,zone_def_post[8])+"||"+getTableCell(0,zone_def_post[9])+"||");
            writer.println("            Eficácia||"+getReverseEfficciencyString(zone_def_goals[0], zone_def_adv_all[0])+"||"+getReverseEfficciencyString(zone_def_goals[1], zone_def_adv_all[1])+"||"+getReverseEfficciencyString(zone_def_goals[2], zone_def_adv_all[2])+"||"+getReverseEfficciencyString(zone_def_goals[3], zone_def_adv_all[3])+"||"+getReverseEfficciencyString(zone_def_goals[4], zone_def_adv_all[4])+"||"+getReverseEfficciencyString(zone_def_goals[5], zone_def_adv_all[5])+"||"+getReverseEfficciencyString(zone_def_goals[6], zone_def_adv_all[6])+"||"+getReverseEfficciencyString(zone_def_goals[7], zone_def_adv_all[7])+"||"+getReverseEfficciencyString(zone_def_goals[8], zone_def_adv_all[8])+"||"+getReverseEfficciencyString(zone_def_goals[9], zone_def_adv_all[9])+"||");
            writer.println("                    ==================================================================================");



            writer.close();
            return filename;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createPlayerStatistics(Player player, Game game){
        PrintWriter writer = null;
        int zone_goals[] = new int[10];
        int zone_out[] = new int[10];
        int zone_blocked[] = new int[10];
        int zone_defended[] = new int[10];
        int zone_post[] = new int[10];
        int zone_all[] = new int[10];

        int zone_ca_goals[] = new int[10];
        int zone_ca_out[] = new int[10];
        int zone_ca_blocked[] = new int[10];
        int zone_ca_defended[] = new int[10];
        int zone_ca_post[] = new int[10];
        int zone_ca_all[] = new int[10];

        int zone_def_block[] = new int[10];
        int zone_def_disarm[] = new int[10];
        int zone_def_int[] = new int[10];
        int zone_def_all[] = new int[10];

        int assists = 0;
        int tecnichalFail = 0;
        int advTecnichalFail = game.getTechnicalFailAdv();
        zone_goals[0] += player.getAllAtkShotsGoal();
        zone_out[0] += player.getAllAtkShotsOut();
        zone_blocked[0] += player.getAllAtkShotsBlocked();
        zone_defended[0] += player.getAllAtkShotsDefended();
        zone_post[0] += player.getAllAtkShotsPost();
        zone_all[0] += player.getAllAtkShots();

        zone_ca_goals[0] += player.getAllCaShotsGoal();
        zone_ca_out[0] += player.getAllCaShotsOut();
        zone_ca_blocked[0] += player.getAllCaShotsBlocked();
        zone_ca_defended[0] += player.getAllCaShotsDefended();
        zone_ca_post[0] += player.getAllCaShotsPost();
        zone_ca_all[0] += player.getAllCaShots();

        zone_def_block[0] = player.getAllBlocked();
        zone_def_disarm[0] = player.getAllDisarms();
        zone_def_int[0] = player.getAllInterceptions();
        zone_def_all[0] = player.getAllBlocked()+player.getAllDisarms()+player.getAllInterceptions();



        for (int i = 1; i<10; i++){
            zone_goals[i] += player.getZoneAtkGoals(i);
            zone_out[i] += player.getZoneAtkShotsOut(i);
            zone_blocked[i] += player.getZoneAtkShotsBlocked(i);
            zone_defended[i] += player.getZoneAtkShotsDefended(i);
            zone_post[i] += player.getZoneAtkShotsPost(i);
            zone_all[i] += player.getZoneAtkShots(i);

            zone_ca_goals[i] += player.getZoneCAGoals(i);
            zone_ca_out[i] += player.getZoneCaShotsOut(i);
            zone_ca_blocked[i] += player.getZoneCaShotsBlocked(i);
            zone_ca_defended[i] += player.getZoneCaShotsDefended(i);
            zone_ca_post[i] += player.getZoneCaShotsPost(i);
            zone_ca_all[i] += player.getZoneCAShots(i);

            zone_def_block[i] = player.getAllBlocked();
            zone_def_disarm[i] = player.getAllDisarms();
            zone_def_int[i] = player.getAllInterceptions();
            zone_def_all[i] = player.getAllBlocked()+player.getAllDisarms()+player.getAllInterceptions();

            assists = player.getAssistance();
            tecnichalFail = player.getTechnicalFailure();


        }

        try {
            String filename = game.getMyTeam()+"vs"+game.getOpponent()+" -- "+player.getName()+".txt";
            writer = new PrintWriter(mActivity.getApplicationContext().getFilesDir().getPath().toString() + filename, "UTF-8");
            writer.println("                    ==================================//Dados Jogo\\\\==================================");
            writer.println("                    "+game.getMyTeam()+" "+game.getScoreMyTeam()+":"+game.getScoreOpponent()+" "+game.getOpponent());
            writer.println("                    Local: "+game.getLocal());
            writer.println("                    Data: "+game.getDate()+" Hora: "+game.getTime());
            writer.println("                    ================================//Estatisticas Jogo\\\\================================");
            writer.println("                    Jogador:"+player.getName()+" #"+player.getNumber());
            writer.println("                                    ==============|| Acções Ofensivas ||==============");

            writer.println("\n                                              ////Ataques e Contra Ataques\\\\\\\\");


            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(zone_goals[0],zone_ca_goals[0])+"||"+getTableCell(zone_goals[1],zone_ca_goals[1])+"||"+getTableCell(zone_goals[2],zone_ca_goals[2])+"||"+getTableCell(zone_goals[3],zone_ca_goals[3])+"||"+getTableCell(zone_goals[4],zone_ca_goals[4])+"||"+getTableCell(zone_goals[5],zone_ca_goals[5])+"||"+getTableCell(zone_goals[6],zone_ca_goals[6])+"||"+getTableCell(zone_goals[7],zone_ca_goals[7])+"||"+getTableCell(zone_goals[8],zone_ca_goals[8])+"||"+getTableCell(zone_goals[9],zone_ca_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(zone_defended[0],zone_ca_defended[0])+"||"+getTableCell(zone_defended[1],zone_ca_defended[1])+"||"+getTableCell(zone_defended[2],zone_ca_defended[2])+"||"+getTableCell(zone_defended[3],zone_ca_defended[3])+"||"+getTableCell(zone_defended[4],zone_ca_defended[4])+"||"+getTableCell(zone_defended[5],zone_ca_defended[5])+"||"+getTableCell(zone_defended[6],zone_ca_defended[6])+"||"+getTableCell(zone_defended[7],zone_ca_defended[7])+"||"+getTableCell(zone_defended[8],zone_ca_defended[8])+"||"+getTableCell(zone_defended[9],zone_ca_defended[9])+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(zone_blocked[0],zone_ca_blocked[0])+"||"+getTableCell(zone_blocked[1],zone_ca_blocked[1])+"||"+getTableCell(zone_blocked[2],zone_ca_blocked[2])+"||"+getTableCell(zone_blocked[3],zone_ca_blocked[3])+"||"+getTableCell(zone_blocked[4],zone_ca_blocked[4])+"||"+getTableCell(zone_blocked[5],zone_ca_blocked[5])+"||"+getTableCell(zone_blocked[6],zone_ca_blocked[6])+"||"+getTableCell(zone_blocked[7],zone_ca_blocked[7])+"||"+getTableCell(zone_blocked[8],zone_ca_blocked[8])+"||"+getTableCell(zone_blocked[9],zone_ca_blocked[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(zone_out[0],zone_ca_out[0])+"||"+getTableCell(zone_out[1],zone_ca_out[1])+"||"+getTableCell(zone_out[2],zone_ca_out[2])+"||"+getTableCell(zone_out[3],zone_ca_out[3])+"||"+getTableCell(zone_out[4],zone_ca_out[4])+"||"+getTableCell(zone_out[5],zone_ca_out[5])+"||"+getTableCell(zone_out[6],zone_ca_out[6])+"||"+getTableCell(zone_out[7],zone_ca_out[7])+"||"+getTableCell(zone_out[8],zone_ca_out[8])+"||"+getTableCell(zone_out[9],zone_ca_out[9])+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(zone_post[0],zone_ca_post[0])+"||"+getTableCell(zone_post[1],zone_ca_post[1])+"||"+getTableCell(zone_post[2],zone_ca_post[2])+"||"+getTableCell(zone_post[3],zone_ca_post[3])+"||"+getTableCell(zone_post[4],zone_ca_post[4])+"||"+getTableCell(zone_post[5],zone_ca_post[5])+"||"+getTableCell(zone_post[6],zone_ca_post[6])+"||"+getTableCell(zone_post[7],zone_ca_post[7])+"||"+getTableCell(zone_post[8],zone_ca_post[8])+"||"+getTableCell(zone_post[9],zone_ca_post[9])+"||");
            writer.println("            Eficácia||"+getReverseEfficciencyString(zone_goals[0]+zone_ca_goals[0],zone_all[0]+zone_ca_all[0])+"||"+getEfficciencyString(zone_goals[1]+zone_ca_goals[1],zone_all[1]+zone_ca_all[1])+"||"+getEfficciencyString(zone_goals[2]+zone_ca_goals[2],zone_all[2]+zone_ca_all[2])+"||"+getEfficciencyString(zone_goals[3]+zone_ca_goals[3],zone_all[3]+zone_ca_all[3])+"||"+getEfficciencyString(zone_goals[4]+zone_ca_goals[4],zone_all[4]+zone_ca_all[4])+"||"+getEfficciencyString(zone_goals[5]+zone_ca_goals[5],zone_all[5]+zone_ca_all[5])+"||"+getEfficciencyString(zone_goals[6]+zone_ca_goals[6],zone_all[6]+zone_ca_all[6])+"||"+getEfficciencyString(zone_goals[7]+zone_ca_goals[7],zone_all[7]+zone_ca_all[7])+"||"+getEfficciencyString(zone_goals[8]+zone_ca_goals[8],zone_all[8]+zone_ca_all[8])+"||"+getEfficciencyString(zone_goals[9]+zone_ca_goals[9],zone_all[9]+zone_ca_all[9])+"||");
            writer.println("                    ==================================================================================");



            //////////////////////////////ATAQUES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            writer.println("\n                                                  ////Ataques\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(zone_goals[0],0)+"||"+getTableCell(zone_goals[1],0)+"||"+getTableCell(zone_goals[2],0)+"||"+getTableCell(zone_goals[3],0)+"||"+getTableCell(zone_goals[4],0)+"||"+getTableCell(zone_goals[5],0)+"||"+getTableCell(zone_goals[6],0)+"||"+getTableCell(zone_goals[7],0)+"||"+getTableCell(zone_goals[8],0)+"||"+getTableCell(zone_goals[9],0)+"||");
            writer.println("  Remates Defendidos||"+getTableCell(zone_defended[0],0)+"||"+getTableCell(zone_defended[1],0)+"||"+getTableCell(zone_defended[2],0)+"||"+getTableCell(zone_defended[3],0)+"||"+getTableCell(zone_defended[4],0)+"||"+getTableCell(zone_defended[5],0)+"||"+getTableCell(zone_defended[6],0)+"||"+getTableCell(zone_defended[7],0)+"||"+getTableCell(zone_defended[8],0)+"||"+getTableCell(zone_defended[9],0)+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(zone_blocked[0],0)+"||"+getTableCell(zone_blocked[1],0)+"||"+getTableCell(zone_blocked[2],0)+"||"+getTableCell(zone_blocked[3],0)+"||"+getTableCell(zone_blocked[4],0)+"||"+getTableCell(zone_blocked[5],0)+"||"+getTableCell(zone_blocked[6],0)+"||"+getTableCell(zone_blocked[7],0)+"||"+getTableCell(zone_blocked[8],0)+"||"+getTableCell(zone_blocked[9],0)+"||");
            writer.println("        Remates Fora||"+getTableCell(zone_out[0],0)+"||"+getTableCell(zone_out[1],0)+"||"+getTableCell(zone_out[2],0)+"||"+getTableCell(zone_out[3],0)+"||"+getTableCell(zone_out[4],0)+"||"+getTableCell(zone_out[5],0)+"||"+getTableCell(zone_out[6],0)+"||"+getTableCell(zone_out[7],0)+"||"+getTableCell(zone_out[8],0)+"||"+getTableCell(zone_out[9],0)+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(zone_post[0],0)+"||"+getTableCell(zone_post[1],0)+"||"+getTableCell(zone_post[2],0)+"||"+getTableCell(zone_post[3],0)+"||"+getTableCell(zone_post[4],0)+"||"+getTableCell(zone_post[5],0)+"||"+getTableCell(zone_post[6],0)+"||"+getTableCell(zone_post[7],0)+"||"+getTableCell(zone_post[8],0)+"||"+getTableCell(zone_post[9],0)+"||");
            writer.println("            Eficácia||"+getEfficciencyString(zone_goals[0],zone_all[0])+"||"+getEfficciencyString(zone_goals[1],zone_all[1])+"||"+getEfficciencyString(zone_goals[2],zone_all[2])+"||"+getEfficciencyString(zone_goals[3],zone_all[3])+"||"+getEfficciencyString(zone_goals[4],zone_all[4])+"||"+getEfficciencyString(zone_goals[5],zone_all[5])+"||"+getEfficciencyString(zone_goals[6],zone_all[6])+"||"+getEfficciencyString(zone_goals[7],zone_all[7])+"||"+getEfficciencyString(zone_goals[8],zone_all[8])+"||"+getEfficciencyString(zone_goals[9],zone_all[9])+"||");
            writer.println("                    ==================================================================================");



            //////////////////////////////CONTRA ATAQUES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            writer.println("\n                                              ////Contra Ataques\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(0,zone_ca_goals[0])+"||"+getTableCell(0,zone_ca_goals[1])+"||"+getTableCell(0,zone_ca_goals[2])+"||"+getTableCell(0,zone_ca_goals[3])+"||"+getTableCell(0,zone_ca_goals[4])+"||"+getTableCell(0,zone_ca_goals[5])+"||"+getTableCell(0,zone_ca_goals[6])+"||"+getTableCell(zone_goals[7],zone_ca_goals[7])+"||"+getTableCell(0,zone_ca_goals[8])+"||"+getTableCell(0,zone_ca_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(0,zone_ca_defended[0])+"||"+getTableCell(0,zone_ca_defended[1])+"||"+getTableCell(0,zone_ca_defended[2])+"||"+getTableCell(0,zone_ca_defended[3])+"||"+getTableCell(0,zone_ca_defended[4])+"||"+getTableCell(0,zone_ca_defended[5])+"||"+getTableCell(0,zone_ca_defended[6])+"||"+getTableCell(0,zone_ca_defended[7])+"||"+getTableCell(0,zone_ca_defended[8])+"||"+getTableCell(0,zone_ca_defended[9])+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(0,zone_ca_blocked[0])+"||"+getTableCell(0,zone_ca_blocked[1])+"||"+getTableCell(0,zone_ca_blocked[2])+"||"+getTableCell(0,zone_ca_blocked[3])+"||"+getTableCell(0,zone_ca_blocked[4])+"||"+getTableCell(0,zone_ca_blocked[5])+"||"+getTableCell(0,zone_ca_blocked[6])+"||"+getTableCell(0,zone_ca_blocked[7])+"||"+getTableCell(0,zone_ca_blocked[8])+"||"+getTableCell(0,zone_ca_blocked[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(0,zone_ca_out[0])+"||"+getTableCell(0,zone_ca_out[1])+"||"+getTableCell(0,zone_ca_out[2])+"||"+getTableCell(0,zone_ca_out[3])+"||"+getTableCell(0,zone_ca_out[4])+"||"+getTableCell(0,zone_ca_out[5])+"||"+getTableCell(0,zone_ca_out[6])+"||"+getTableCell(0,zone_ca_out[7])+"||"+getTableCell(0,zone_ca_out[8])+"||"+getTableCell(0,zone_ca_out[9])+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(0,zone_ca_post[0])+"||"+getTableCell(0,zone_ca_post[1])+"||"+getTableCell(0,zone_ca_post[2])+"||"+getTableCell(0,zone_ca_post[3])+"||"+getTableCell(0,zone_ca_post[4])+"||"+getTableCell(0,zone_ca_post[5])+"||"+getTableCell(0,zone_ca_post[6])+"||"+getTableCell(0,zone_ca_post[7])+"||"+getTableCell(zone_post[8],zone_ca_post[8])+"||"+getTableCell(0,zone_ca_post[9])+"||");
            writer.println("            Eficácia||"+getEfficciencyString(zone_ca_goals[0],zone_ca_all[0])+"||"+getEfficciencyString(zone_ca_goals[1],zone_ca_all[1])+"||"+getEfficciencyString(zone_ca_goals[2],zone_ca_all[2])+"||"+getEfficciencyString(zone_ca_goals[3],zone_ca_all[3])+"||"+getEfficciencyString(zone_ca_goals[4],zone_ca_all[4])+"||"+getEfficciencyString(zone_ca_goals[5],zone_ca_all[5])+"||"+getEfficciencyString(zone_ca_goals[6],zone_ca_all[6])+"||"+getEfficciencyString(zone_ca_goals[7],zone_ca_all[7])+"||"+getEfficciencyString(zone_ca_goals[8],zone_ca_all[8])+"||"+getEfficciencyString(zone_ca_goals[9],zone_ca_all[9])+"||");
            writer.println("                    ==================================================================================");

            writer.println("\n                                              ////Acções Defensivas\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("              Blocos||"+getTableCell(0, zone_def_block[0])+"||"+getTableCell(0,zone_def_block[1])+"||"+getTableCell(0,zone_def_block[2])+"||"+getTableCell(0,zone_def_block[3])+"||"+getTableCell(0,zone_def_block[4])+"||"+getTableCell(0,zone_def_block[5])+"||"+getTableCell(0,zone_def_block[6])+"||"+getTableCell(zone_goals[7],zone_def_block[7])+"||"+getTableCell(0,zone_def_block[8])+"||"+getTableCell(0,zone_def_block[9])+"||");
            writer.println("            Desarmes||"+getTableCell(0,zone_def_disarm[0])+"||"+getTableCell(0,zone_def_disarm[1])+"||"+getTableCell(0,zone_def_disarm[2])+"||"+getTableCell(0,zone_def_disarm[3])+"||"+getTableCell(0,zone_def_disarm[4])+"||"+getTableCell(0,zone_def_disarm[5])+"||"+getTableCell(0,zone_def_disarm[6])+"||"+getTableCell(0,zone_def_disarm[7])+"||"+getTableCell(0,zone_def_disarm[8])+"||"+getTableCell(0,zone_def_disarm[9])+"||");
            writer.println("        Intercepções||"+getTableCell(0,zone_def_int[0])+"||"+getTableCell(0,zone_def_int[1])+"||"+getTableCell(0,zone_def_int[2])+"||"+getTableCell(0,zone_def_int[3])+"||"+getTableCell(0,zone_def_int[4])+"||"+getTableCell(0,zone_def_int[5])+"||"+getTableCell(0,zone_def_int[6])+"||"+getTableCell(0,zone_def_int[7])+"||"+getTableCell(0,zone_def_int[8])+"||"+getTableCell(0,zone_def_int[9])+"||");
            writer.println("                    ==================================================================================");


            writer.println("\n                                              ////Outras Acções\\\\\\\\");
            writer.println("\n                  Assistências:"+assists);
            writer.println("\n                  Falhas Técnicas:"+tecnichalFail);
            writer.println("\n                  Falhas Técnicas Adversário:"+advTecnichalFail);

            int zone_def_goals[] = new int[10];
            int zone_def_defended[] = new int[10];
            int zone_def_out[] = new int[10];
            int zone_def_post[] = new int[10];
            int zone_def_adv_all[] = new int[10];

            writer.close();
            return filename;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;

    }

    private String createGoalkeeperReport(Goalkeeper gk, Game game){
        PrintWriter writer = null;
        int zone_goals[] = new int[10];
        int zone_out[] = new int[10];
        int zone_blocked[] = new int[10];
        int zone_defended[] = new int[10];
        int zone_post[] = new int[10];
        int zone_all[] = new int[10];

        int zone_ca_goals[] = new int[10];
        int zone_ca_out[] = new int[10];
        int zone_ca_blocked[] = new int[10];
        int zone_ca_defended[] = new int[10];
        int zone_ca_post[] = new int[10];
        int zone_ca_all[] = new int[10];

        int zone_def_block[] = new int[10];
        int zone_def_disarm[] = new int[10];
        int zone_def_int[] = new int[10];
        int zone_def_all[] = new int[10];

        int assists = 0;
        int tecnichalFail = 0;
        int advTecnichalFail = game.getTechnicalFailAdv();

        zone_goals[0] += gk.getAllAtkShotsGoal();
        zone_out[0] += gk.getAllAtkShotsOut();
        zone_blocked[0] += gk.getAllAtkShotsBlocked();
        zone_defended[0] += gk.getAllAtkShotsDefended();
        zone_post[0] += gk.getAllAtkShotsPost();
        zone_all[0] += gk.getAllAtkShots();

        zone_ca_goals[0] += gk.getAllCaShotsGoal();
        zone_ca_out[0] += gk.getAllCaShotsOut();
        zone_ca_blocked[0] += gk.getAllCaShotsBlocked();
        zone_ca_defended[0] += gk.getAllCaShotsDefended();
        zone_ca_post[0] += gk.getAllCaShotsPost();
        zone_ca_all[0] += gk.getAllCaShots();

        zone_def_block[0] = gk.getAllBlocked();
        zone_def_disarm[0] = gk.getAllDisarms();
        zone_def_int[0] = gk.getAllInterceptions();
        zone_def_all[0] = gk.getAllBlocked()+gk.getAllDisarms()+gk.getAllInterceptions();



        for (int i = 1; i<10; i++){
            zone_goals[i] += gk.getZoneAtkGoals(i);
            zone_out[i] += gk.getZoneAtkShotsOut(i);
            zone_blocked[i] += gk.getZoneAtkShotsBlocked(i);
            zone_defended[i] += gk.getZoneAtkShotsDefended(i);
            zone_post[i] += gk.getZoneAtkShotsPost(i);
            zone_all[i] += gk.getZoneAtkShots(i);

            zone_ca_goals[i] += gk.getZoneCAGoals(i);
            zone_ca_out[i] += gk.getZoneCaShotsOut(i);
            zone_ca_blocked[i] += gk.getZoneCaShotsBlocked(i);
            zone_ca_defended[i] += gk.getZoneCaShotsDefended(i);
            zone_ca_post[i] += gk.getZoneCaShotsPost(i);
            zone_ca_all[i] += gk.getZoneCAShots(i);

            zone_def_block[i] = gk.getAllBlocked();
            zone_def_disarm[i] = gk.getAllDisarms();
            zone_def_int[i] = gk.getAllInterceptions();
            zone_def_all[i] = gk.getAllBlocked()+gk.getAllDisarms()+gk.getAllInterceptions();

            assists = gk.getAssistance();
            tecnichalFail = gk.getTechnicalFailure();


        }

        try {
            String filename = game.getMyTeam()+"vs"+game.getOpponent()+" -- "+gk.getName()+".txt";
            writer = new PrintWriter(mActivity.getApplicationContext().getFilesDir().getPath().toString() + filename, "UTF-8");
            writer.println("                    ==================================//Dados Jogo\\\\==================================");
            writer.println("                    "+game.getMyTeam()+" "+game.getScoreMyTeam()+":"+game.getScoreOpponent()+" "+game.getOpponent());
            writer.println("                    Local: "+game.getLocal());
            writer.println("                    Data: "+game.getDate()+" Hora: "+game.getTime());
            writer.println("                    ================================//Estatisticas Jogo\\\\================================");
            writer.println("                    "+gk.getName()+" #"+gk.getNumber());
            writer.println("                                    ==============|| Acções Ofensivas ||==============");

            writer.println("\n                                              ////Ataques e Contra Ataques\\\\\\\\");


            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(zone_goals[0],zone_ca_goals[0])+"||"+getTableCell(zone_goals[1],zone_ca_goals[1])+"||"+getTableCell(zone_goals[2],zone_ca_goals[2])+"||"+getTableCell(zone_goals[3],zone_ca_goals[3])+"||"+getTableCell(zone_goals[4],zone_ca_goals[4])+"||"+getTableCell(zone_goals[5],zone_ca_goals[5])+"||"+getTableCell(zone_goals[6],zone_ca_goals[6])+"||"+getTableCell(zone_goals[7],zone_ca_goals[7])+"||"+getTableCell(zone_goals[8],zone_ca_goals[8])+"||"+getTableCell(zone_goals[9],zone_ca_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(zone_defended[0],zone_ca_defended[0])+"||"+getTableCell(zone_defended[1],zone_ca_defended[1])+"||"+getTableCell(zone_defended[2],zone_ca_defended[2])+"||"+getTableCell(zone_defended[3],zone_ca_defended[3])+"||"+getTableCell(zone_defended[4],zone_ca_defended[4])+"||"+getTableCell(zone_defended[5],zone_ca_defended[5])+"||"+getTableCell(zone_defended[6],zone_ca_defended[6])+"||"+getTableCell(zone_defended[7],zone_ca_defended[7])+"||"+getTableCell(zone_defended[8],zone_ca_defended[8])+"||"+getTableCell(zone_defended[9],zone_ca_defended[9])+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(zone_blocked[0],zone_ca_blocked[0])+"||"+getTableCell(zone_blocked[1],zone_ca_blocked[1])+"||"+getTableCell(zone_blocked[2],zone_ca_blocked[2])+"||"+getTableCell(zone_blocked[3],zone_ca_blocked[3])+"||"+getTableCell(zone_blocked[4],zone_ca_blocked[4])+"||"+getTableCell(zone_blocked[5],zone_ca_blocked[5])+"||"+getTableCell(zone_blocked[6],zone_ca_blocked[6])+"||"+getTableCell(zone_blocked[7],zone_ca_blocked[7])+"||"+getTableCell(zone_blocked[8],zone_ca_blocked[8])+"||"+getTableCell(zone_blocked[9],zone_ca_blocked[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(zone_out[0],zone_ca_out[0])+"||"+getTableCell(zone_out[1],zone_ca_out[1])+"||"+getTableCell(zone_out[2],zone_ca_out[2])+"||"+getTableCell(zone_out[3],zone_ca_out[3])+"||"+getTableCell(zone_out[4],zone_ca_out[4])+"||"+getTableCell(zone_out[5],zone_ca_out[5])+"||"+getTableCell(zone_out[6],zone_ca_out[6])+"||"+getTableCell(zone_out[7],zone_ca_out[7])+"||"+getTableCell(zone_out[8],zone_ca_out[8])+"||"+getTableCell(zone_out[9],zone_ca_out[9])+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(zone_post[0],zone_ca_post[0])+"||"+getTableCell(zone_post[1],zone_ca_post[1])+"||"+getTableCell(zone_post[2],zone_ca_post[2])+"||"+getTableCell(zone_post[3],zone_ca_post[3])+"||"+getTableCell(zone_post[4],zone_ca_post[4])+"||"+getTableCell(zone_post[5],zone_ca_post[5])+"||"+getTableCell(zone_post[6],zone_ca_post[6])+"||"+getTableCell(zone_post[7],zone_ca_post[7])+"||"+getTableCell(zone_post[8],zone_ca_post[8])+"||"+getTableCell(zone_post[9],zone_ca_post[9])+"||");
            writer.println("            Eficácia||"+getReverseEfficciencyString(zone_goals[0] + zone_ca_goals[0], zone_all[0] + zone_ca_all[0])+"||"+getEfficciencyString(zone_goals[1] + zone_ca_goals[1], zone_all[1] + zone_ca_all[1])+"||"+getEfficciencyString(zone_goals[2] + zone_ca_goals[2], zone_all[2] + zone_ca_all[2])+"||"+getEfficciencyString(zone_goals[3] + zone_ca_goals[3], zone_all[3] + zone_ca_all[3])+"||"+getEfficciencyString(zone_goals[4] + zone_ca_goals[4], zone_all[4] + zone_ca_all[4])+"||"+getEfficciencyString(zone_goals[5] + zone_ca_goals[5], zone_all[5] + zone_ca_all[5])+"||"+getEfficciencyString(zone_goals[6] + zone_ca_goals[6], zone_all[6] + zone_ca_all[6])+"||"+getEfficciencyString(zone_goals[7] + zone_ca_goals[7], zone_all[7] + zone_ca_all[7])+"||"+getEfficciencyString(zone_goals[8] + zone_ca_goals[8], zone_all[8] + zone_ca_all[8])+"||"+getEfficciencyString(zone_goals[9] + zone_ca_goals[9], zone_all[9] + zone_ca_all[9])+"||");
            writer.println("                    ==================================================================================");



            //////////////////////////////ATAQUES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            writer.println("\n                                                  ////Ataques\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(zone_goals[0],0)+"||"+getTableCell(zone_goals[1],0)+"||"+getTableCell(zone_goals[2],0)+"||"+getTableCell(zone_goals[3],0)+"||"+getTableCell(zone_goals[4],0)+"||"+getTableCell(zone_goals[5],0)+"||"+getTableCell(zone_goals[6],0)+"||"+getTableCell(zone_goals[7],0)+"||"+getTableCell(zone_goals[8],0)+"||"+getTableCell(zone_goals[9],0)+"||");
            writer.println("  Remates Defendidos||"+getTableCell(zone_defended[0],0)+"||"+getTableCell(zone_defended[1],0)+"||"+getTableCell(zone_defended[2],0)+"||"+getTableCell(zone_defended[3],0)+"||"+getTableCell(zone_defended[4],0)+"||"+getTableCell(zone_defended[5],0)+"||"+getTableCell(zone_defended[6],0)+"||"+getTableCell(zone_defended[7],0)+"||"+getTableCell(zone_defended[8],0)+"||"+getTableCell(zone_defended[9],0)+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(zone_blocked[0],0)+"||"+getTableCell(zone_blocked[1],0)+"||"+getTableCell(zone_blocked[2],0)+"||"+getTableCell(zone_blocked[3],0)+"||"+getTableCell(zone_blocked[4],0)+"||"+getTableCell(zone_blocked[5],0)+"||"+getTableCell(zone_blocked[6],0)+"||"+getTableCell(zone_blocked[7],0)+"||"+getTableCell(zone_blocked[8],0)+"||"+getTableCell(zone_blocked[9],0)+"||");
            writer.println("        Remates Fora||"+getTableCell(zone_out[0],0)+"||"+getTableCell(zone_out[1],0)+"||"+getTableCell(zone_out[2],0)+"||"+getTableCell(zone_out[3],0)+"||"+getTableCell(zone_out[4],0)+"||"+getTableCell(zone_out[5],0)+"||"+getTableCell(zone_out[6],0)+"||"+getTableCell(zone_out[7],0)+"||"+getTableCell(zone_out[8],0)+"||"+getTableCell(zone_out[9],0)+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(zone_post[0],0)+"||"+getTableCell(zone_post[1],0)+"||"+getTableCell(zone_post[2],0)+"||"+getTableCell(zone_post[3],0)+"||"+getTableCell(zone_post[4],0)+"||"+getTableCell(zone_post[5],0)+"||"+getTableCell(zone_post[6],0)+"||"+getTableCell(zone_post[7],0)+"||"+getTableCell(zone_post[8],0)+"||"+getTableCell(zone_post[9],0)+"||");
            writer.println("            Eficácia||"+getEfficciencyString(zone_goals[0],zone_all[0])+"||"+getEfficciencyString(zone_goals[1],zone_all[1])+"||"+getEfficciencyString(zone_goals[2],zone_all[2])+"||"+getEfficciencyString(zone_goals[3],zone_all[3])+"||"+getEfficciencyString(zone_goals[4],zone_all[4])+"||"+getEfficciencyString(zone_goals[5],zone_all[5])+"||"+getEfficciencyString(zone_goals[6],zone_all[6])+"||"+getEfficciencyString(zone_goals[7],zone_all[7])+"||"+getEfficciencyString(zone_goals[8],zone_all[8])+"||"+getEfficciencyString(zone_goals[9],zone_all[9])+"||");
            writer.println("                    ==================================================================================");



            //////////////////////////////CONTRA ATAQUES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            writer.println("\n                                              ////Contra Ataques\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(0,zone_ca_goals[0])+"||"+getTableCell(0,zone_ca_goals[1])+"||"+getTableCell(0,zone_ca_goals[2])+"||"+getTableCell(0,zone_ca_goals[3])+"||"+getTableCell(0,zone_ca_goals[4])+"||"+getTableCell(0,zone_ca_goals[5])+"||"+getTableCell(0,zone_ca_goals[6])+"||"+getTableCell(zone_goals[7],zone_ca_goals[7])+"||"+getTableCell(0,zone_ca_goals[8])+"||"+getTableCell(0,zone_ca_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(0,zone_ca_defended[0])+"||"+getTableCell(0,zone_ca_defended[1])+"||"+getTableCell(0,zone_ca_defended[2])+"||"+getTableCell(0,zone_ca_defended[3])+"||"+getTableCell(0,zone_ca_defended[4])+"||"+getTableCell(0,zone_ca_defended[5])+"||"+getTableCell(0,zone_ca_defended[6])+"||"+getTableCell(0,zone_ca_defended[7])+"||"+getTableCell(0,zone_ca_defended[8])+"||"+getTableCell(0,zone_ca_defended[9])+"||");
            writer.println("  Remates Bloqueados||"+getTableCell(0,zone_ca_blocked[0])+"||"+getTableCell(0,zone_ca_blocked[1])+"||"+getTableCell(0,zone_ca_blocked[2])+"||"+getTableCell(0,zone_ca_blocked[3])+"||"+getTableCell(0,zone_ca_blocked[4])+"||"+getTableCell(0,zone_ca_blocked[5])+"||"+getTableCell(0,zone_ca_blocked[6])+"||"+getTableCell(0,zone_ca_blocked[7])+"||"+getTableCell(0,zone_ca_blocked[8])+"||"+getTableCell(0,zone_ca_blocked[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(0,zone_ca_out[0])+"||"+getTableCell(0,zone_ca_out[1])+"||"+getTableCell(0,zone_ca_out[2])+"||"+getTableCell(0,zone_ca_out[3])+"||"+getTableCell(0,zone_ca_out[4])+"||"+getTableCell(0,zone_ca_out[5])+"||"+getTableCell(0,zone_ca_out[6])+"||"+getTableCell(0,zone_ca_out[7])+"||"+getTableCell(0,zone_ca_out[8])+"||"+getTableCell(0,zone_ca_out[9])+"||");
            writer.println(" Remates Poste/Trave||"+getTableCell(0,zone_ca_post[0])+"||"+getTableCell(0,zone_ca_post[1])+"||"+getTableCell(0,zone_ca_post[2])+"||"+getTableCell(0,zone_ca_post[3])+"||"+getTableCell(0,zone_ca_post[4])+"||"+getTableCell(0,zone_ca_post[5])+"||"+getTableCell(0,zone_ca_post[6])+"||"+getTableCell(0,zone_ca_post[7])+"||"+getTableCell(zone_post[8],zone_ca_post[8])+"||"+getTableCell(0,zone_ca_post[9])+"||");
            writer.println("            Eficácia||"+getEfficciencyString(zone_ca_goals[0],zone_ca_all[0])+"||"+getEfficciencyString(zone_ca_goals[1],zone_ca_all[1])+"||"+getEfficciencyString(zone_ca_goals[2],zone_ca_all[2])+"||"+getEfficciencyString(zone_ca_goals[3],zone_ca_all[3])+"||"+getEfficciencyString(zone_ca_goals[4],zone_ca_all[4])+"||"+getEfficciencyString(zone_ca_goals[5],zone_ca_all[5])+"||"+getEfficciencyString(zone_ca_goals[6],zone_ca_all[6])+"||"+getEfficciencyString(zone_ca_goals[7],zone_ca_all[7])+"||"+getEfficciencyString(zone_ca_goals[8],zone_ca_all[8])+"||"+getEfficciencyString(zone_ca_goals[9],zone_ca_all[9])+"||");
            writer.println("                    ==================================================================================");

            writer.println("\n                                              ////Acções Defensivas\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("              Blocos||"+getTableCell(0, zone_def_block[0])+"||"+getTableCell(0,zone_def_block[1])+"||"+getTableCell(0,zone_def_block[2])+"||"+getTableCell(0,zone_def_block[3])+"||"+getTableCell(0,zone_def_block[4])+"||"+getTableCell(0,zone_def_block[5])+"||"+getTableCell(0,zone_def_block[6])+"||"+getTableCell(zone_goals[7],zone_def_block[7])+"||"+getTableCell(0,zone_def_block[8])+"||"+getTableCell(0,zone_def_block[9])+"||");
            writer.println("            Desarmes||"+getTableCell(0,zone_def_disarm[0])+"||"+getTableCell(0,zone_def_disarm[1])+"||"+getTableCell(0,zone_def_disarm[2])+"||"+getTableCell(0,zone_def_disarm[3])+"||"+getTableCell(0,zone_def_disarm[4])+"||"+getTableCell(0,zone_def_disarm[5])+"||"+getTableCell(0,zone_def_disarm[6])+"||"+getTableCell(0,zone_def_disarm[7])+"||"+getTableCell(0,zone_def_disarm[8])+"||"+getTableCell(0,zone_def_disarm[9])+"||");
            writer.println("        Intercepções||"+getTableCell(0,zone_def_int[0])+"||"+getTableCell(0,zone_def_int[1])+"||"+getTableCell(0,zone_def_int[2])+"||"+getTableCell(0,zone_def_int[3])+"||"+getTableCell(0,zone_def_int[4])+"||"+getTableCell(0,zone_def_int[5])+"||"+getTableCell(0,zone_def_int[6])+"||"+getTableCell(0,zone_def_int[7])+"||"+getTableCell(0,zone_def_int[8])+"||"+getTableCell(0,zone_def_int[9])+"||");
            writer.println("                    ==================================================================================");


            writer.println("\n                                              ////Outras Acções\\\\\\\\");
            writer.println("\n                  Assistências:"+assists);
            writer.println("\n                  Falhas Técnicas:"+tecnichalFail);
            writer.println("\n                  Falhas Técnicas Adversário:"+advTecnichalFail);

            int zone_def_goals[] = new int[10];
            int zone_def_defended[] = new int[10];
            int zone_def_out[] = new int[10];
            int zone_def_post[] = new int[10];
            int zone_def_adv_all[] = new int[10];
            for(int i = 1; i<10; i++){

                zone_def_out[0] += zone_def_out[i] = gk.getZoneOutShots(i);
                zone_def_post[0] += zone_def_post[i] = gk.getZonePostShots(i);
                for (int j = 1; j<10; j++){
                    zone_def_goals[0] +=gk.getZoneAllGoals(i,j);
                    zone_def_goals[i] += gk.getZoneAllGoals(i,j);
                    zone_def_defended[0] += gk.getZoneAllDefended(i,j);
                    zone_def_defended[i] += gk.getZoneAllDefended(i,j);
                }

                zone_def_adv_all[i] = zone_def_goals[i]+zone_def_defended[i]+zone_def_post[i]+zone_def_out[i];
            }


            zone_def_adv_all[0] = zone_def_goals[0]+zone_def_defended[0]+zone_def_post[0]+zone_def_out[0];

            writer.println("\n                                              ////Acções Guarda Redes por Zona\\\\\\\\");

            writer.println("                    ==================================================================================");
            writer.println("                    ||Total ||Zona 1||Zona 2||Zona 3||Zona 4||Zona 5||Zona 6||Zona 7||Zona 8||Zona 9||");
            writer.println("               Golos||"+getTableCell(0, zone_def_goals[0])+"||"+getTableCell(0,zone_def_goals[1])+"||"+getTableCell(0,zone_def_goals[2])+"||"+getTableCell(0,zone_def_goals[3])+"||"+getTableCell(0,zone_def_goals[4])+"||"+getTableCell(0,zone_def_goals[5])+"||"+getTableCell(0,zone_def_goals[6])+"||"+getTableCell(zone_goals[7],zone_def_goals[7])+"||"+getTableCell(0,zone_def_goals[8])+"||"+getTableCell(0,zone_def_goals[9])+"||");
            writer.println("  Remates Defendidos||"+getTableCell(0,zone_def_defended[0])+"||"+getTableCell(0,zone_def_defended[1])+"||"+getTableCell(0,zone_def_defended[2])+"||"+getTableCell(0,zone_def_defended[3])+"||"+getTableCell(0,zone_def_defended[4])+"||"+getTableCell(0,zone_def_defended[5])+"||"+getTableCell(0,zone_def_defended[6])+"||"+getTableCell(0,zone_def_defended[7])+"||"+getTableCell(0,zone_def_defended[8])+"||"+getTableCell(0,zone_def_defended[9])+"||");
            writer.println("       Remates Poste||"+getTableCell(0,zone_def_out[0])+"||"+getTableCell(0,zone_def_out[1])+"||"+getTableCell(0,zone_def_out[2])+"||"+getTableCell(0,zone_def_out[3])+"||"+getTableCell(0,zone_def_out[4])+"||"+getTableCell(0,zone_def_out[5])+"||"+getTableCell(0,zone_def_out[6])+"||"+getTableCell(0,zone_def_out[7])+"||"+getTableCell(0,zone_def_out[8])+"||"+getTableCell(0,zone_def_out[9])+"||");
            writer.println("        Remates Fora||"+getTableCell(0,zone_def_post[0])+"||"+getTableCell(0,zone_def_post[1])+"||"+getTableCell(0,zone_def_post[2])+"||"+getTableCell(0,zone_def_post[3])+"||"+getTableCell(0,zone_def_post[4])+"||"+getTableCell(0,zone_def_post[5])+"||"+getTableCell(0,zone_def_post[6])+"||"+getTableCell(0,zone_def_post[7])+"||"+getTableCell(0,zone_def_post[8])+"||"+getTableCell(0,zone_def_post[9])+"||");
            writer.println("            Eficácia||"+getReverseEfficciencyString(zone_def_goals[0], zone_def_adv_all[0])+"||"+getReverseEfficciencyString(zone_def_goals[1], zone_def_adv_all[1])+"||"+getReverseEfficciencyString(zone_def_goals[2], zone_def_adv_all[2])+"||"+getReverseEfficciencyString(zone_def_goals[3], zone_def_adv_all[3])+"||"+getReverseEfficciencyString(zone_def_goals[4], zone_def_adv_all[4])+"||"+getReverseEfficciencyString(zone_def_goals[5], zone_def_adv_all[5])+"||"+getReverseEfficciencyString(zone_def_goals[6], zone_def_adv_all[6])+"||"+getReverseEfficciencyString(zone_def_goals[7], zone_def_adv_all[7])+"||"+getReverseEfficciencyString(zone_def_goals[8], zone_def_adv_all[8])+"||"+getReverseEfficciencyString(zone_def_goals[9], zone_def_adv_all[9])+"||");
            writer.println("                    ==================================================================================");

            for (int i = 1; i<10; i++){
                zone_def_goals[i] = 0;
                zone_def_defended[i] = 0;
            }
            for(int i = 1; i<10; i++) {
                for (int j = 1; j < 10; j++) {
                    zone_def_goals[j] += gk.getZoneAllGoals(i, j);
                    zone_def_defended[j] += gk.getZoneAllDefended(i, j);

                    zone_def_adv_all[j] = zone_def_goals[j]+zone_def_defended[j];
                }


            }



            writer.println("                                                      ||Todas as Zonas||");
            writer.println("                                                  ==========================");
            writer.println("                                                  ||"+getGoalCellString(zone_def_goals[1],zone_def_adv_all[1])+"||"+getGoalCellString(zone_def_goals[2],zone_def_adv_all[2])+"||"+getGoalCellString(zone_def_goals[3],zone_def_adv_all[3])+"||");
            writer.println("                                                  ||"+getReverseEfficciencyString(zone_def_goals[1],zone_def_adv_all[1])+"||"+getReverseEfficciencyString(zone_def_goals[2],zone_def_adv_all[2])+"||"+getReverseEfficciencyString(zone_def_goals[3],zone_def_adv_all[3])+"||");
            writer.println("                                                  ==========================");
            writer.println("                                                  ||"+getGoalCellString(zone_def_goals[4],zone_def_adv_all[4])+"||"+getGoalCellString(zone_def_goals[5],zone_def_adv_all[5])+"||"+getGoalCellString(zone_def_goals[6],zone_def_adv_all[6])+"||");
            writer.println("                                                  ||"+getReverseEfficciencyString(zone_def_goals[4],zone_def_adv_all[4])+"||"+getReverseEfficciencyString(zone_def_goals[5],zone_def_adv_all[5])+"||"+getReverseEfficciencyString(zone_def_goals[6],zone_def_adv_all[6])+"||");
            writer.println("                                                  ==========================");
            writer.println("                                                  ||"+getGoalCellString(zone_def_goals[7],zone_def_adv_all[7])+"||"+getGoalCellString(zone_def_goals[8],zone_def_adv_all[8])+"||"+getGoalCellString(zone_def_goals[9],zone_def_adv_all[9])+"||");
            writer.println("                                                  ||"+getReverseEfficciencyString(zone_def_goals[7],zone_def_adv_all[7])+"||"+getReverseEfficciencyString(zone_def_goals[8],zone_def_adv_all[8])+"||"+getReverseEfficciencyString(zone_def_goals[9],zone_def_adv_all[9])+"||");
            writer.println("                                                  ==========================");



            for(int i = 1; i<10; i++){
                for (int j = 1; j<10; j++){
                    zone_def_goals[j] = gk.getZoneAllGoals(i,j);
                    zone_def_defended[j] = gk.getZoneAllDefended(i,j);

                    zone_def_adv_all[j] = zone_def_goals[j]+zone_def_defended[j];
                }


                zone_def_adv_all[i] = zone_def_goals[i]+zone_def_defended[i];
                writer.println("                                                    ||Zona "+i+"||");
                writer.println("                                            ==========================");
                writer.println("                                            ||"+getGoalCellString(zone_def_goals[1],zone_def_adv_all[1])+"||"+getGoalCellString(zone_def_goals[2],zone_def_adv_all[2])+"||"+getGoalCellString(zone_def_goals[3],zone_def_adv_all[3])+"||");
                writer.println("                                            ||"+getReverseEfficciencyString(zone_def_goals[1],zone_def_adv_all[1])+"||"+getReverseEfficciencyString(zone_def_goals[2],zone_def_adv_all[2])+"||"+getReverseEfficciencyString(zone_def_goals[3],zone_def_adv_all[3])+"||");
                writer.println("                                            ==========================");
                writer.println("                                            ||"+getGoalCellString(zone_def_goals[4],zone_def_adv_all[4])+"||"+getGoalCellString(zone_def_goals[5],zone_def_adv_all[5])+"||"+getGoalCellString(zone_def_goals[6],zone_def_adv_all[6])+"||");
                writer.println("                                            ||"+getReverseEfficciencyString(zone_def_goals[4],zone_def_adv_all[4])+"||"+getReverseEfficciencyString(zone_def_goals[5],zone_def_adv_all[5])+"||"+getReverseEfficciencyString(zone_def_goals[6],zone_def_adv_all[6])+"||");
                writer.println("                                            ==========================");
                writer.println("                                            ||"+getGoalCellString(zone_def_goals[7],zone_def_adv_all[7])+"||"+getGoalCellString(zone_def_goals[8],zone_def_adv_all[8])+"||"+getGoalCellString(zone_def_goals[9],zone_def_adv_all[9])+"||");
                writer.println("                                            ||"+getReverseEfficciencyString(zone_def_goals[7],zone_def_adv_all[7])+"||"+getReverseEfficciencyString(zone_def_goals[8],zone_def_adv_all[8])+"||"+getReverseEfficciencyString(zone_def_goals[9],zone_def_adv_all[9])+"||");
                writer.println("                                            ==========================");
            }

            writer.close();
            return filename;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTableCell(int a, int b){
        String text;
        int value = a+b;

        if(value < 10){
            return text = "   "+value+"  ";
        }else if(value < 100){
            return text = "  "+value+"  ";
        }else{
            return text = " "+value+"  ";
        }
    }

   private String getEfficciencyString(int goals, int total) {
       String text;
       int effectiveness = 0;
       if (total > 0) {
           effectiveness = Math.round((goals / ((float) total)) * 100);
           if (effectiveness < 10) {
               return text = "   " + effectiveness + "% ";
           } else if (effectiveness < 100) {
               return text = "  " + effectiveness + "% ";
           } else {
               return text = " " + effectiveness + "% ";
           }
       } else {
           return text = "  --  ";
       }
   }

    private String getReverseEfficciencyString(int goals, int total) {
        String text;
        int effectiveness = 0;
        if (total > 0) {
            effectiveness = 100 - Math.round((goals / ((float) total)) * 100);
            if (effectiveness < 10) {
                return text = "   " + effectiveness + "% ";
            } else if (effectiveness < 100) {
                return text = "  " + effectiveness + "% ";
            } else {
                return text = " " + effectiveness + "% ";
            }
        } else {
            return text = "  --  ";
        }
    }

    private String getGoalCellString(int goal, int defended){
        String text;

        if(goal < 10&&defended<10){
            return text = " "+goal+"/"+defended+"  ";
        }else if((goal < 10 && defended>10)||(goal > 10 && defended<10)){
            return text = " "+goal+"/"+defended+" ";
        }else{
            return text = ""+goal+"/"+defended+" ";
        }
    }




}
