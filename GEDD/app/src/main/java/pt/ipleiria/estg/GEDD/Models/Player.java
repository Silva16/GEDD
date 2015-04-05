package pt.ipleiria.estg.GEDD.Models;

/**
 * Created by Andre on 27/03/2015.
 */
public class Player {

    int number;

    //Disciplina
    private boolean yellowCard;
    private boolean redCard;

    //teste
    private String teste;

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
    private int interception;
    private int block;
    private int disarm;

    //Outros
    private int technicalFailure;
    private int assistance;

    public Player(int number) {
        this.number = number;
        this.yellowCard = false;
        this.redCard = false;
        this.assistance = 0;
        this.block = 0;
        this.disarm = 0;
        this.interception = 0;
        this.technicalFailure = 0;

        setTeste("Ultima Ação");
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

    public int getInterception() {
        return interception;
    }

    public void setInterception() {
        this.interception++;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock() {
        this.block++;
    }

    public int getDisarm() {
        return disarm;
    }

    public void setDisarm() {
        this.disarm++;
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

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }

    public void refreshPlayer(String finalization, String zone, String offAction){
        if(finalization=="btn_ca"){
            switch (offAction)
            {

            }
        }
    }

    public int getAllShots(int[] shots){
        int sum = 0;
        for (int i =0;i<9;i++){
            sum += shots[i];
        }
        return sum;
    }

    public int getAllCaShots(){
        return getAllShots(this.getCaShotBlocked()) + getAllShots(this.getCaShotPost()) + getAllShots(this.getCaShotGoal()) + getAllShots(this.getCaShotDefended()) + getAllShots(this.getCaShotOut());
    }

    public int getAllSixShots(){
        return getAllShots(this.getSixShotBlocked()) + getAllShots(this.getSixShotPost()) + getAllShots(this.getSixShotGoal()) + getAllShots(this.getSixShotDefended()) + getAllShots(this.getSixShotOut());
    }

    public int getAllSevenShots(){
        return getAllShots(this.getSevenShotBlocked()) + getAllShots(this.getSevenShotPost()) + getAllShots(this.getSevenShotGoal()) + getAllShots(this.getSevenShotDefended()) + getAllShots(this.getSevenShotOut());
    }

    public int getAllNineShots(){
        return getAllShots(this.getNineShotBlocked()) + getAllShots(this.getNineShotPost()) + getAllShots(this.getNineShotGoal()) + getAllShots(this.getNineShotDefended()) + getAllShots(this.getNineShotOut());
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
        return getAllShots(nineShotGoal)+getAllShots(sixShotGoal)+getAllShots(sevenShotGoal)+getAllShots(caShotGoal);
    }

    public int getAllShotOut(){
        return getAllShots(nineShotOut)+getAllShots(sixShotOut)+getAllShots(sevenShotOut)+getAllShots(caShotOut);
    }

    public int getAllShotPost(){
        return getAllShots(nineShotPost)+getAllShots(sixShotPost)+getAllShots(sevenShotPost)+getAllShots(caShotPost);
    }

    public int getAllShotDefended(){
        return getAllShots(nineShotDefended)+getAllShots(sixShotDefended)+getAllShots(sevenShotDefended)+getAllShots(caShotDefended);
    }

    public int getAllBlocked(){
        return getAllShots(nineShotBlocked)+getAllShots(sixShotBlocked)+getAllShots(sevenShotBlocked)+getAllShots(caShotBlocked);
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
}
