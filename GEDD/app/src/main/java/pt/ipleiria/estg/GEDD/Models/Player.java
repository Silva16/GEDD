package pt.ipleiria.estg.GEDD.Models;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Andre on 27/03/2015.
 */
public class Player implements Serializable{

    private int number;
    private String name;

    private Boolean playing;

    //Disciplina
    private int twoMin;
    private boolean twoMinOut;
    private boolean yellowCard;
    private boolean redCard;

    //teste
    private String lastAction;

    //Remates
    private int[] atkShotGoal = new int[9];
    private int[] atkShotBlocked = new int[9];
    private int[] atkShotOut = new int[9];
    private int[] atkShotPost = new int[9];
    private int[] atkShotDefended = new int[9];

    private int[] caShotGoal = new int[9];
    private int[] caShotBlocked = new int[9];
    private int[] caShotOut = new int[9];
    private int[] caShotPost = new int[9];
    private int[] caShotDefended = new int[9];

    //Ações Defensivas
    private int[] interception = new int[9];
    private int[] block = new int[9];
    private int[] disarm = new int[9];

    //Outros
    private int technicalFailure;
    private int assistance;

    public Player(int number, String name) {
        this.number = number;
        this.name = name;
        this.twoMin = 0;
        this.twoMinOut = false;
        this.yellowCard = false;
        this.redCard = false;
        this.assistance = 0;
        this.technicalFailure = 0;
        this.playing = false;

        setLastAction("Ultima Ação");
    }

    public int getTwoMin() {
        return twoMin;
    }

    public void setTwoMin() {
        this.twoMin++;
    }

    public boolean isTwoMinOut() {
        return twoMinOut;
    }

    public void setTwoMinOut() {
        this.twoMinOut = true;
    }

    public boolean isYellowCard() {
        return yellowCard;
    }

    public void setYellowCard() {
        this.yellowCard = true;
    }

    public boolean isRedCard() {
        return redCard;
    }

    public void setRedCard() {
        this.redCard = true;
    }

    public int[] getAtkShotGoal() {
        return atkShotGoal;
    }

    public void setAtkShotGoal(int zone) {
        this.atkShotGoal[zone-1] ++;
    }

    public int[] getAtkShotBlocked() {
        return atkShotBlocked;
    }

    public void setAtkShotBlocked(int zone) {
        this.atkShotBlocked[zone-1]++;
    }

    public int[] getAtkShotOut() {
        return atkShotOut;
    }

    public void setAtkShotOut(int zone) {
        this.atkShotOut[zone-1]++;
    }

    public int[] getAtkShotPost() {
        return atkShotPost;
    }

    public void setAtkShotPost(int zone) {
        this.atkShotPost[zone-1]++;
    }

    public int[] getAtkShotDefended() {
        return atkShotDefended;
    }

    public void setAtkShotDefended(int zone) {
        this.atkShotDefended[zone-1]++;
    }

    public int[] getCaShotGoal() {
        return caShotGoal;
    }

    public void setCaShotGoal(int zone) {
        this.caShotGoal[zone-1]++;
    }

    public int[] getCaShotBlocked() {
        return caShotBlocked;
    }

    public void setCaShotBlocked(int zone) {
        this.caShotBlocked[zone-1]++;
    }

    public int[] getCaShotOut() {
        return caShotOut;
    }

    public void setCaShotOut(int zone) {
        this.caShotOut[zone-1]++;
    }

    public int[] getCaShotPost() {
        return caShotPost;
    }

    public void setCaShotPost(int zone) {
        this.caShotPost[zone-1]++;
    }

    public int[] getCaShotDefended() {
        return caShotDefended;
    }

    public void setCaShotDefended(int zone) {
        this.caShotDefended[zone-1]++;
    }

    public int[] getInterception() {
        return interception;
    }

    public void setInterception(int zone) {
        this.interception[zone-1]++;
    }

    public int[] getBlock() {
        return block;
    }

    public void setBlock(int zone) {
        this.block[zone-1]++;
    }

    public int[] getDisarm() {
        return disarm;
    }

    public void setDisarm(int zone) {
        this.disarm[zone-1]++;
    }

    public int getTechnicalFailure() {
        return technicalFailure;
    }

    public void setTechnicalFailure(int technicalFailure) {
        this.technicalFailure = technicalFailure;
    }

    public int getAssistance() {
        return assistance;
    }

    public void setAssistance(int assistance) {
        this.assistance = assistance;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public void refreshPlayer(String finalization, String zone, String offAction){
        if(finalization=="btn_ca"){
            switch (offAction)
            {

            }
        }
    }

    public int getAllActions(int[] shots){
        int sum = 0;
        for (int i =0;i<9;i++){
            sum += shots[i];
        }
        return sum;
    }

    public int getAllDefensiveBlocks(){
        return getAllActions(this.getBlock());
    }

    public int getAllDisarms(){
        return getAllActions(this.getDisarm());
    }

    public int getAllInterceptions(){
        return getAllActions(this.getInterception());
    }

    public int getAllCaShots(){
        return getAllActions(this.getCaShotBlocked()) + getAllActions(this.getCaShotPost()) + getAllActions(this.getCaShotGoal()) + getAllActions(this.getCaShotDefended()) + getAllActions(this.getCaShotOut());
    }

    public int getAllAtkShots(){
        return getAllActions(this.getAtkShotBlocked()) + getAllActions(this.getAtkShotPost()) + getAllActions(this.getAtkShotGoal()) + getAllActions(this.getAtkShotDefended()) + getAllActions(this.getAtkShotOut());
    }

    public void refreshPlayerStats(String finalization, int zone, String offAction){
        if(finalization=="btn_ca"){
            switch (offAction)
            {
                case "btn_goal": setCaShotGoal(zone);
                    break;
                case "btn_goalpost": setCaShotPost(zone);
                    break;
                case "btn_out": setCaShotOut(zone);
                    break;
                case "btn_defense": setCaShotDefended(zone);
                    break;
                case "btn_block_atk": setCaShotBlocked(zone);
                    break;
            }
        }

        if(finalization=="btn_atk"){
            switch (offAction)
            {
                case "btn_goal": setAtkShotGoal(zone);
                    break;
                case "btn_goalpost": setAtkShotPost(zone);
                    break;
                case "btn_out": setAtkShotOut(zone);
                    break;
                case "btn_defense": setAtkShotDefended(zone);
                    break;
                case "btn_block_atk": setAtkShotBlocked(zone);
                    break;
            }
        }

    }

    public int getZoneShots(int zone){
        return atkShotGoal[zone-1]+ atkShotBlocked[zone-1]+ atkShotDefended[zone-1]+ atkShotOut[zone-1]+ atkShotPost[zone-1]+caShotGoal[zone-1]+caShotOut[zone-1]+caShotBlocked[zone-1]+caShotDefended[zone-1]+caShotPost[zone-1];
    }

    public int getZoneGoals(int zone){
        return atkShotGoal[zone-1]+caShotGoal[zone-1];
    }

    public int getAllShotGoals(){
        return getAllActions(atkShotGoal) + getAllActions(caShotGoal);
    }

    public int getAllShotOut(){
        return getAllActions(atkShotOut)+ getAllActions(caShotOut);
    }

    public int getAllShotPost(){
        return getAllActions(atkShotPost) + getAllActions(caShotPost);
    }

    public int getAllShotDefended(){
        return getAllActions(atkShotDefended)+ getAllActions(caShotDefended);
    }

    public int getAllBlocked(){
        return getAllActions(atkShotBlocked)+ getAllActions(caShotBlocked);
    }

    public int getNumber() {
        return number;
    }

    public void addAssistance(){
       this.assistance ++;
    }

    public void addTechFail(){
        this.technicalFailure++;
    }

    //public void selectFinalizationResult(result)


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
        Log.i("setPlaying",String.valueOf(playing));
    }
}
