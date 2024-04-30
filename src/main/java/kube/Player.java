package kube;

public class Player {
    
    int id;
    int[][] montain;
    int[][] additional;

    public Player(int id, int[][] additional) {
        this.id = id;
        this.montain = new int[][];
        this.additional = additional;
    }
}
