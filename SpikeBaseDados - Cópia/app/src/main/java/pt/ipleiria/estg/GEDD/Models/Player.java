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
    private int[] sixShotGoal;
    private int[] sixShotBlocked;
    private int[] sixShotOut;
    private int[] sixShotPost;
    private int[] sixShotDefended;

    private int[] caShotGoal;
    private int[] caShotBlocked;
    private int[] caShotOut;
    private int[] caShotPost;
    private int[] caShotDefended;

    private int[] sevenShotGoal;
    private int[] sevenShotBlocked;
    private int[] sevenShotOut;
    private int[] sevenShotPost;
    private int[] sevenShotDefended;

    private int[] nineShotGoal;
    private int[] nineShotBlocked;
    private int[] nineShotOut;
    private int[] nineShotPost;
    private int[] nineShotDefended;

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

        setTeste("teste 1");
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
        this.sixShotGoal[zone] ++;
    }

    public int[] getSixShotBlocked() {
        return sixShotBlocked;
    }

    public void setSixShotBlocked(int zone) {
        this.sixShotBlocked[zone]++;
    }

    public int[] getSixShotOut() {
        return sixShotOut;
    }

    public void setSixShotOut(int zone) {
        this.sixShotOut[zone]++;
    }

    public int[] getSixShotPost() {
        return sixShotPost;
    }

    public void setSixShotPost(int zone) {
        this.sixShotPost[zone]++;
    }

    public int[] getSixShotDefended() {
        return sixShotDefended;
    }

    public void setSixShotDefended(int zone) {
        this.sixShotDefended[zone]++;
    }

    public int[] getCaShotGoal() {
        return caShotGoal;
    }

    public void setCaShotGoal(int zone) {
        this.caShotGoal[zone]++;
    }

    public int[] getCaShotBlocked() {
        return caShotBlocked;
    }

    public void setCaShotBlocked(int zone) {
        this.caShotBlocked[zone]++;
    }

    public int[] getCaShotOut() {
        return caShotOut;
    }

    public void setCaShotOut(int zone) {
        this.caShotOut[zone]++;
    }

    public int[] getCaShotPost() {
        return caShotPost;
    }

    public void setCaShotPost(int zone) {
        this.caShotPost[zone]++;
    }

    public int[] getCaShotDefended() {
        return caShotDefended;
    }

    public void setCaShotDefended(int zone) {
        this.caShotDefended[zone]++;
    }

    public int[] getSevenShotGoal() {
        return sevenShotGoal;
    }

    public void setSevenShotGoal(int zone) {
        this.sevenShotGoal[zone]++;
    }

    public int[] getSevenShotBlocked() {
        return sevenShotBlocked;
    }

    public void setSevenShotBlocked(int zone) {
        this.sevenShotBlocked[zone]++;
    }

    public int[] getSevenShotOut() {
        return sevenShotOut;
    }

    public void setSevenShotOut(int zone) {
        this.sevenShotOut[zone]++;
    }

    public int[] getSevenShotPost() {
        return sevenShotPost;
    }

    public void setSevenShotPost(int zone) {
        this.sevenShotPost[zone]++;
    }

    public int[] getSevenShotDefended() {
        return sevenShotDefended;
    }

    public void setSevenShotDefended(int zone) {
        this.sevenShotDefended[zone]++;
    }

    public int[] getNineShotGoal() {
        return nineShotGoal;
    }

    public void setNineShotGoal(int zone) {
        this.nineShotGoal[zone]++;
    }

    public int[] getNineShotBlocked() {
        return nineShotBlocked;
    }

    public void setNineShotBlocked(int zone) {
        this.nineShotBlocked[zone]++;
    }

    public int[] getNineShotOut() {
        return nineShotOut;
    }

    public void setNineShotOut(int zone) {
        this.nineShotOut[zone]++;
    }

    public int[] getNineShotPost() {
        return nineShotPost;
    }

    public void setNineShotPost(int zone) {
        this.nineShotPost[zone]++;
    }

    public int[] getNineShotDefended() {
        return nineShotDefended;
    }

    public void setNineShotDefended(int zone) {
        this.nineShotDefended[zone]++;
    }

    public int getInterception() {
        return interception;
    }

    public void setInterception(int interception) {
        this.interception = interception;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getDisarm() {
        return disarm;
    }

    public void setDisarm(int disarm) {
        this.disarm = disarm;
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
}
