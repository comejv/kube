package kube.model;

public class Mountain {
    private int[] m;
    private int base_size;
    private static final int nbBitPerColor = 7;

    public Mountain(int size){
        base_size = size;
        m = new int[base_size];
    }

    // Setter
    public void setBaseSize(int size){
        base_size = size;
    }

    public void setMountain(int[] mountain){
        m = mountain;
    }

    // Getter
    public int getBaseSize(){
        return base_size;
    }

    public int[] getMountain(){
        return m;
    }

    public Color getCase(int x, int y){
        return Color.getColor((m[x] >> (y * nbBitPerColor)) & 127);
    }

    public void setCase(int x, int y, Color c){
        m[x] = m[x] & (~127 << ((y-1 * nbBitPerColor)));
        m[x] = m[x] & (c.getColorCode() << (y * nbBitPerColor));
    }

    public void remove(int x, int y){
        setCase(x, y, Color.EMPTY);
    }
}
