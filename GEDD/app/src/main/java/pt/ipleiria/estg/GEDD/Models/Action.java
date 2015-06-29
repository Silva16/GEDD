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
    private int minutes;
    private int seconds;

    public Action(String type, int minutes, int seconds){
        this.type = type;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Action(Player player, String type, int minutes, int seconds){
        this.player = player;
        this.type = type;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Action(Player player, String type, String finalization, int zone, int goalZone, int minutes, int seconds) {
        this.player = player;
        this.type = type;
        this.finalization = finalization;
        this.zone = zone;
        this.goalZone = goalZone;
        this.ts = new Timestamp(System.currentTimeMillis());
        this.minutes = minutes;
        this.seconds = seconds;
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

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
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
            case "assistance": text = "Acção: Assistência ";
                break;
            case "tech_fail": text = "Acção: Falha Técnica ";
                break;
            case "red_card": text = "Acção: Cartão Vermelho ";
                break;
            case "yellow_card": text = "Acção: Cartão Amarelo ";
                break;
            case "2min_card": text = "Acção: Suspensão de dois minutos ";
                break;
            case "adv_technical_fail": text = "Acção: Falha Técnica Adversária ";
                break;
            default: text="Acção: "+type;
        }

        if(finalization != null){
            if(finalization == "btn_ca"){
                text += " numa jogada de contra-ataque";
            }else{
                text += " numa jogada de ataque";
            }
        }
        if(zone != 0){
            text += " da zona "+zone;
        }
        if(goalZone != 0){
            text += " na zona "+goalZone+" da baliza ";
        }

        if(type == "btn_gk_goal" || type =="btn_gk_defended"|| type =="btn_gk_out"|| type =="btn_gk_post"){
            text += "defendida ";
        }
        if(player != null) {
            text += "por " + player.getName() + " #" + player.getNumber();
        }

        return text;
    }

}
