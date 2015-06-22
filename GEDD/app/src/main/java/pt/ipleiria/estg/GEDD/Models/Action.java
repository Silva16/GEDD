package pt.ipleiria.estg.GEDD.Models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Andre on 12/06/2015.
 */
public class Action implements Serializable {

    private static final long serialVersionUID = 4038121090025789738L;
    private Player player;
    private String type;
    private String finalization;
    private int zone;
    private int goalZone;
    private Timestamp ts;

    public Action(Player player, String type, String finalization, int zone, int goalZone) {
        this.player = player;
        this.type = type;
        this.finalization = finalization;
        this.zone = zone;
        this.goalZone = goalZone;
        this.ts = new Timestamp(System.currentTimeMillis());
    }

    public Player getPlayer() {
        return player;
    }

    public String getType() {
        return type;
    }

    public String getFinalization() {
        return finalization;
    }

    public int getZone() {
        return zone;
    }

    public int getGoalZone() {
        return goalZone;
    }

    public String getText(){
        String text;
        switch(type) {
            case "btn_goal": text = "Acção: Golo";
                break;
            case "btn_out": text = "Acção: Remate Fora";
                break;
            case "btn_goalpost": text = "Acção: Remate ao Poste";
                break;
            case "btn_defense": text = "Acção: Remate Defendido";
                break;
            case "btn_block_atk": text = "Acção: Remate Bloqueado";
                break;
            case "btn_block_def": text = "Acção: Bloqueio";
                break;
            case "btn_disarm": text = "Acção: Desarme";
                break;
            case "btn_interception": text = "Acção: Intercepção";
                break;
            case "btn_gk_goal": text = "Acção: Golo Adversario";
                break;
            case "btn_gk_def": text = "Acção: Remate Adversário Defendido";
                break;
            case "btn_gk_out": text = "Acção: Remate Adversário Fora";
                break;
            case "btn_gk_post": text = "Acção: Remate Adversário ao Poste";
                break;
            case "assistance": text = "Acção: Assistência";
                break;
            default: text="Acção: "+type;
        }

        if(finalization != null){
            text += " com resultado "+finalization;
        }
        if(zone != 0){
            text += " da zona "+zone;
        }
        if(goalZone != 0){
            text += " na zona "+goalZone+" da baliza";
        }

        text += " por "+player.getName()+" #"+player.getNumber();
        return text;
    }

}
