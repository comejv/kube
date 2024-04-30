package kube.model;

public class Mountain {
    private int[] m;
    private int base_size;
    private static final int nbBitPerColor = 7;

    public Mountain(int size){
        base_size = size;
        m = new int[base_size];
    }

    public Color getCase(int x, int y){
        return Color.getColor((m[x] >> (y * nbBitPerColor)) & 127);
    }

    public void setCase(int x, int y, Color c){
        m[x] = m[x] & (~127 << ((y-1 * nbBitPerColor)));
        m[x] = m[x] & (c.getColorCode() << (y * nbBitPerColor));
    }
}
