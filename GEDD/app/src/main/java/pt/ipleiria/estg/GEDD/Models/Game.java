package pt.ipleiria.estg.GEDD.Models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by Silva16 on 13-04-2015.
 */
public class Game implements Serializable {

    private static final long serialVersionUID = 4038121090021350738L;

    private String opponent;
    private String myTeam;
    private int scoreMyTeam;
    private int scoreOpponent;
    private int minutes;
    private int seconds;
    private int technicalFailAdv;
    private Boolean started;
    private Boolean closed;

    private String local;

    private int gameDay;
    private int gameMonth;
    private int gameYear;

    private int gameHour;
    private int gameMinute;

    private UUID ID;

    private LinkedList<Player> players;
    private LinkedList<Goalkeeper> gks;
    private LinkedList<Action> actions;

    public Game(String myTeam, String advTeam, int hour, int minute, int day, int month, int year, String local) {
        ID = new UUID(123,254).randomUUID();
        setOpponent(advTeam);
        setMyTeam(myTeam);
        setGameDay(day);
        setGameHour(hour);
        setGameMinute(minute);
        setGameMonth(month);
        setGameYear(year);
        setLocal(local);
        this.scoreMyTeam = 0;
        this.scoreOpponent = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.technicalFailAdv = 0;
        this.started = false;
        this.closed = false;
        actions = new LinkedList<Action>();
    }

    public UUID getID() {
        return ID;
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

    public int getGameDay() {
        return gameDay;
    }

    public void setGameDay(int gameDay) {
        this.gameDay = gameDay;
    }

    public int getGameMonth() {
        return gameMonth;
    }

    public void setGameMonth(int gameMonth) {
        this.gameMonth = gameMonth;
    }

    public int getGameYear() {
        return gameYear;
    }

    public void setGameYear(int gameYear) {
        this.gameYear = gameYear;
    }

    public int getGameHour() {
        return gameHour;
    }

    public void setGameHour(int gameHour) {
        this.gameHour = gameHour;
    }

    public int getGameMinute() {
        return gameMinute;
    }

    public void setGameMinute(int gameMinute) {
        this.gameMinute = gameMinute;
    }

    public String getTime(){
        int hour;
        int minute;
        String time;
        if((hour = getGameHour())<10){
            time = "0"+String.valueOf(hour);
        }else{
            time = String.valueOf(hour);
        }

        if((minute = getGameMinute())<10){
            time += ":0"+String.valueOf(minute);
        }else{
            time += ":"+String.valueOf(minute);
        }

        return time;
    }

    public String getDate(){
        int day;
        int month;
        int year;
        String date;

        if((day = getGameDay())<10){
            date = "0"+String.valueOf(day);
        }else{
            date = String.valueOf(day);
        }

        if((month = getGameMonth())<10){
            date += "/0"+String.valueOf(month);
        }else{
            date += "/"+String.valueOf(month);
        }

        date += "/"+String.valueOf(getGameYear());

        return date;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setStarted(){
        this.started = true;
    }

    public Boolean isClosed(){
        return this.closed;
    }

    public void setClosed(){
        this.closed = true;
    }

    public Boolean isStarted(){
        return this.started;
    }

    public String getName(){
        return getMyTeam()+" x "+getOpponent();
    }

    public Action getLastAction(){
        if(actions.size()>0)
            return actions.getLast();
        return null;
    }

    public void addAction(Action act){
        actions.add(act);
    }
}
