package tomasulo;

public class peripheralView {
    public final float regsFile[];
    public final int RAT[];
    public final float memory[];

    public peripheralView(){
        regsFile = new float[16];
        RAT = new int[16];
        memory = new float[1000];
    }
}
