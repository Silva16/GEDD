package pt.ipleiria.estg.GEDD.Models;

/**
 * Created by Silva16 on 06-04-2015.
 */
public class Goalkeeper extends Player {

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

    public int[][] getDefended() {
        return defended;
    }

    public void setDefended(int zone, int baliza) {
        this.defended[zone-1][baliza-1]++;
    }

    public int[] getPost() {
        return post;
    }

    public void setPost(int zone) {
        this.post[zone-1]++;
    }

    public int[] getOut() {
        return out;
    }

    public void setOut(int zone) {
        this.out[zone-1]++;
    }

    public int getZoneGoalShots(int zone, int baliza){
        return goal[zone-1][baliza - 1]+defended[zone-1][baliza - 1];
    }

    public int getZoneOutShots(int zone){
        return post[zone-1]+out[zone-1];
    }

    public int getZoneAllGoals(int zone, int baliza){
        return goal[zone-1][baliza - 1];
    }

    public int getZoneAllDefended(int zone, int baliza){
        return defended[zone-1][baliza - 1];
    }
}
