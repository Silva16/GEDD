package pt.ipleiria.estg.GEDD;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import pt.ipleiria.estg.GEDD.Models.Player;

/**
 * Created by Andre on 11/04/2015.
 */
public class JsonUtil {
    public static String playerToJSon(ArrayList<Player> players) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();

            JSONArray jsonPlayers = new JSONArray();

            for (Player player : players) {
                JSONObject jsonPlayer = new JSONObject();
                jsonPlayer.put("name", player.getName()); // Set the first name/pair
                jsonPlayer.put("number", player.getNumber());


                jsonPlayers.put(jsonPlayer);
            }

            jsonObj.put("player", jsonPlayers);

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }

    public JSONObject getJsonObj(String data) {

        try {
            JSONObject jsonObj = new JSONObject(data);
            return jsonObj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LinkedList<Player> getPlayersList(JSONObject jObj){
        JSONArray jArr = null;
        LinkedList<Player> players = new LinkedList<Player>();
        try {
            jArr = jObj.getJSONArray("player");

            Log.i("jArr", Integer.valueOf(jArr.length()).toString());
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                players.add(new Player(obj.getInt("number"),obj.getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return players;

    }

    public JSONObject readFile(Context mContext){
        FileInputStream fis = null;

        StringBuffer fileContent = new StringBuffer("");

        try{
            fis = mContext.openFileInput("GEDDTeamData");

            byte[] buffer = new byte[1024];

            int n;

            while ((n = fis.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = fileContent.toString();

        JsonUtil jsonUtil = new JsonUtil();

        JSONObject jsonObj = jsonUtil.getJsonObj(text);

        return jsonObj;
    }


}

