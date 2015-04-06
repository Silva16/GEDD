package pt.ipleiria.estg.GEDD.Models;

/**
 * Created by Silva16 on 06-04-2015.
 */
public class Goalkeeper extends Player {

    public Goalkeeper(int number) {
        super(number);
    }

    private int[][] goal = new int[9][9];
    private int[][] defended = new int[9][9];
    private int[] post = new int[9];
    private int[] out = new int[9];

    public int[][] getGoal() {
        return goal;
    }

    public void setGoal(int baliza, int zone) {
        this.goal[baliza-1][zone-1]++;
    }

    public int[][] getDefended() {
        return defended;
    }

    public void setDefended(int baliza, int zone) {
        this.defended[baliza-1][zone-1]++;
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
}
