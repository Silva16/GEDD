package pt.ipleiria.estg.GEDD.Models;

/**
 * Created by Silva16 on 06-04-2015.
 */
public class Goalkeeper extends Player {

    private static final long serialVersionUID = -6929496304898697467L;

    public Goalkeeper(int number, String name) {
        super(number, name);
    }

    private int[][] goal = new int[9][9];
    private int[][] defended = new int[9][9];
    private int[] post = new int[9];
    private int[] out = new int[9];

    public int[][] getGoal() {
        return goal;
    }

    public void setGoal(int zone, int baliza) {
        this.goal[zone-1][baliza-1]++;
    }

    public void removeGoal(int zone, int baliza) {
        this.goal[zone-1][baliza-1]--;
    }

    public int[][] getDefended() {
        return defended;
    }

    public void setDefended(int zone, int baliza) {
        this.defended[zone-1][baliza-1]++;
    }

    public void removeDefended(int zone, int baliza) {
        this.defended[zone-1][baliza-1]--;
    }


    public int[] getPost() {
        return post;
    }

    public void setPost(int zone) {
        this.post[zone-1]++;
    }

    public void removePost(int zone) {
        this.post[zone-1]--;
    }

    public int[] getOut() {
        return out;
    }

    public void setOut(int zone) {
        this.out[zone-1]++;
    }

    public void removeOut(int zone) {
        this.out[zone-1]--;
    }

    public int getZoneGoalShots(int zone, int baliza){
        return goal[zone-1][baliza - 1]+defended[zone-1][baliza - 1];
    }

    public int getZoneOutShots(int zone){
        return out[zone-1];
    }

    public int getZonePostShots(int zone){
        return post[zone-1];
    }

    public int getZoneAllGoals(int zone, int baliza){
        return goal[zone-1][baliza - 1];
    }

    public int getZoneAllDefended(int zone, int baliza){
        return defended[zone-1][baliza - 1];
    }
}
