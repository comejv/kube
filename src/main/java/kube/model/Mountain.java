package kube.model;

public class Mountain {
    private int[] m;
    private int base_size;
    private static final int nbPerColor = 7;
    Mountain(int size){
        base_size = size;
        m = new int[base_size];
    }

    public Color getCase(int x, int y){
        return Color.getColor((m[x] >> ((y - 1) * nbPerColor)) & 127);
    }

    public void setCase(int x, int y, Color c){
        m[x] = m[x] & (~127 << ((y-1 * nbPerColor)));
        m[x] = m[x] & (c.getColorCode() << ((y-1 * nbPerColor)));
    }
}
