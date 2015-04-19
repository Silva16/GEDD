package pt.ipleiria.estg.GEDD.Models;

import java.io.Serializable;

/**
 * Created by Andre on 27/03/2015.
 */
public class Player implements Serializable{

    private int number;
    private String name;

    private Boolean playing;

    //Disciplina
    private boolean yellowCard;
    private boolean redCard;

    //teste
    private String lastAction;

    //Remates
    private int[] sixShotGoal = new int[9];
    private int[] sixShotBlocked = new int[9];
    private int[] sixShotOut = new int[9];
    private int[] sixShotPost = new int[9];
    private int[] sixShotDefended = new int[9];

    private int[] caShotGoal = new int[9];
    private int[] caShotBlocked = new int[9];
    private int[] caShotOut = new int[9];
    private int[] caShotPost = new int[9];
    private int[] caShotDefended = new int[9];

    private int[] sevenShotGoal= new int[9];
    private int[] sevenShotBlocked = new int[9];
    private int[] sevenShotOut = new int[9];
    private int[] sevenShotPost = new int[9];
    private int[] sevenShotDefended = new int[9];

    private int[] nineShotGoal = new int[9];
    private int[] nineShotBlocked = new int[9];
    private int[] nineShotOut = new int[9];
    private int[] nineShotPost = new int[9];
    private int[] nineShotDefended = new int[9];

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
        this.yellowCard = false;
        this.redCard = false;
        this.assistance = 0;
        this.technicalFailure = 0;
        this.playing = false;

        setLastAction("Ultima Ação");
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

    public int[] getSixShotGoal() {
        return sixShotGoal;
    }

    public void setSixShotGoal(int zone) {
        this.sixShotGoal[zone-1] ++;
    }

    public int[] getSixShotBlocked() {
        return sixShotBlocked;
    }

    public void setSixShotBlocked(int zone) {
        this.sixShotBlocked[zone-1]++;
    }

    public int[] getSixShotOut() {
        return sixShotOut;
    }

    public void setSixShotOut(int zone) {
        this.sixShotOut[zone-1]++;
    }

    public int[] getSixShotPost() {
        return sixShotPost;
    }

    public void setSixShotPost(int zone) {
        this.sixShotPost[zone-1]++;
    }

    public int[] getSixShotDefended() {
        return sixShotDefended;
    }

    public void setSixShotDefended(int zone) {
        this.sixShotDefended[zone-1]++;
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

    public int[] getSevenShotGoal() {
        return sevenShotGoal;
    }

    public void setSevenShotGoal(int zone) {
        this.sevenShotGoal[zone-1]++;
    }

    public int[] getSevenShotBlocked() {
        return sevenShotBlocked;
    }

    public void setSevenShotBlocked(int zone) {
        this.sevenShotBlocked[zone-1]++;
    }

    public int[] getSevenShotOut() {
        return sevenShotOut;
    }

    public void setSevenShotOut(int zone) {
        this.sevenShotOut[zone-1]++;
    }

    public int[] getSevenShotPost() {
        return sevenShotPost;
    }

    public void setSevenShotPost(int zone) {
        this.sevenShotPost[zone-1]++;
    }

    public int[] getSevenShotDefended() {
        return sevenShotDefended;
    }

    public void setSevenShotDefended(int zone) {
        this.sevenShotDefended[zone-1]++;
    }

    public int[] getNineShotGoal() {
        return nineShotGoal;
    }

    public void setNineShotGoal(int zone) {
        this.nineShotGoal[zone-1]++;
    }

    public int[] getNineShotBlocked() {
        return nineShotBlocked;
    }

    public void setNineShotBlocked(int zone) {
        this.nineShotBlocked[zone-1]++;
    }

    public int[] getNineShotOut() {
        return nineShotOut;
    }

    public void setNineShotOut(int zone) {
        this.nineShotOut[zone-1]++;
    }

    public int[] getNineShotPost() {
        return nineShotPost;
    }

    public void setNineShotPost(int zone) {
        this.nineShotPost[zone-1]++;
    }

    public int[] getNineShotDefended() {
        return nineShotDefended;
    }

    public void setNineShotDefended(int zone) {
        this.nineShotDefended[zone-1]++;
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

    public int getAllSixShots(){
        return getAllActions(this.getSixShotBlocked()) + getAllActions(this.getSixShotPost()) + getAllActions(this.getSixShotGoal()) + getAllActions(this.getSixShotDefended()) + getAllActions(this.getSixShotOut());
    }

    public int getAllSevenShots(){
        return getAllActions(this.getSevenShotBlocked()) + getAllActions(this.getSevenShotPost()) + getAllActions(this.getSevenShotGoal()) + getAllActions(this.getSevenShotDefended()) + getAllActions(this.getSevenShotOut());
    }

    public int getAllNineShots(){
        return getAllActions(this.getNineShotBlocked()) + getAllActions(this.getNineShotPost()) + getAllActions(this.getNineShotGoal()) + getAllActions(this.getNineShotDefended()) + getAllActions(this.getNineShotOut());
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


        if(finalization=="btn_7m"){
            switch (offAction)
            {
                case "btn_goal": setSevenShotGoal(zone);
                    break;
                case "btn_goalpost": setSevenShotPost(zone);
                    break;
                case "btn_out": setSevenShotOut(zone);
                    break;
                case "btn_defense": setSevenShotDefended(zone);
                    break;
                case "btn_block_atk": setSevenShotBlocked(zone);
                    break;
            }
        }


        if(finalization=="btn_9m"){
            switch (offAction)
            {
                case "btn_goal": setNineShotGoal(zone);
                    break;
                case "btn_goalpost": setNineShotPost(zone);
                    break;
                case "btn_out": setNineShotOut(zone);
                    break;
                case "btn_defense": setNineShotDefended(zone);
                    break;
                case "btn_block_atk": setNineShotBlocked(zone);
                    break;
            }
        }

        if(finalization=="btn_6m"){
            switch (offAction)
            {
                case "btn_goal": setSixShotGoal(zone);
                    break;
                case "btn_goalpost": setSixShotPost(zone);
                    break;
                case "btn_out": setSixShotOut(zone);
                    break;
                case "btn_defense": setSixShotDefended(zone);
                    break;
                case "btn_block_atk": setSixShotBlocked(zone);
                    break;
            }
        }

    }

    public int getZoneShots(int zone){
        return nineShotBlocked[zone-1]+nineShotDefended[zone-1]+nineShotGoal[zone-1]+nineShotOut[zone-1]+nineShotPost[zone-1]+sixShotGoal[zone-1]+sixShotBlocked[zone-1]+sixShotDefended[zone-1]+sixShotOut[zone-1]+sixShotPost[zone-1]+sevenShotBlocked[zone-1]+sevenShotDefended[zone-1]+sevenShotGoal[zone-1]+sevenShotOut[zone-1]+sevenShotPost[zone-1]+caShotGoal[zone-1]+caShotOut[zone-1]+caShotBlocked[zone-1]+caShotDefended[zone-1]+caShotPost[zone-1];
    }

    public int getZoneGoals(int zone){
        return nineShotGoal[zone-1]+sixShotGoal[zone-1]+sevenShotGoal[zone-1]+caShotGoal[zone-1];
    }

    public int getAllShotGoals(){
        return getAllActions(nineShotGoal)+ getAllActions(sixShotGoal)+ getAllActions(sevenShotGoal)+ getAllActions(caShotGoal);
    }

    public int getAllShotOut(){
        return getAllActions(nineShotOut)+ getAllActions(sixShotOut)+ getAllActions(sevenShotOut)+ getAllActions(caShotOut);
    }

    public int getAllShotPost(){
        return getAllActions(nineShotPost)+ getAllActions(sixShotPost)+ getAllActions(sevenShotPost)+ getAllActions(caShotPost);
    }

    public int getAllShotDefended(){
        return getAllActions(nineShotDefended)+ getAllActions(sixShotDefended)+ getAllActions(sevenShotDefended)+ getAllActions(caShotDefended);
    }

    public int getAllBlocked(){
        return getAllActions(nineShotBlocked)+ getAllActions(sixShotBlocked)+ getAllActions(sevenShotBlocked)+ getAllActions(caShotBlocked);
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
    }
}
