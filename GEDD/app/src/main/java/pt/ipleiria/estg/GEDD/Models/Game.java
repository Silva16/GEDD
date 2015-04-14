package pt.ipleiria.estg.GEDD.Models;

/**
 * Created by Silva16 on 13-04-2015.
 */
public class Game {

    private String opponent;
    private String myTeam;
    private int scoreMyTeam;
    private int scoreOpponent;

    public Game() {
        this.opponent = "";
        this.myTeam = "";
        this.scoreMyTeam = 0;
        this.scoreOpponent = 0;
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


}
