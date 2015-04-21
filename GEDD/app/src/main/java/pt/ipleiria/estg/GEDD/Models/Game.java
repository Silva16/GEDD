package pt.ipleiria.estg.GEDD.Models;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Silva16 on 13-04-2015.
 */
public class Game implements Serializable {

    private String opponent;
    private String myTeam;
    private int scoreMyTeam;
    private int scoreOpponent;
    private int minutes;
    private int seconds;
    private int technicalFailAdv;

    private LinkedList<Player> players;
    private LinkedList<Goalkeeper> gks;

    public Game() {
        this.opponent = "";
        this.myTeam = "";
        this.scoreMyTeam = 0;
        this.scoreOpponent = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.technicalFailAdv = 0;
    }

    public int getScoreOpponent() {
        return scoreOpponent;
    }

    public void setScoreOpponent() {
        this.scoreOpponent++;
    }


    public int getScoreMyTeam() {
        return scoreMyTeam;
    }

    public void setScoreMyTeam() {
        this.scoreMyTeam++;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(String myTeam) {
        this.myTeam = myTeam;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public LinkedList<Goalkeeper> getGks() {
        return gks;
    }

    public void setGks(LinkedList<Goalkeeper> gks) {
        this.gks = gks;
    }

    public int getTechnicalFailAdv() {
        return technicalFailAdv;
    }

    public void setTechnicalFailAdv(int technicalFailAdv) {
        this.technicalFailAdv = technicalFailAdv;
    }
}
