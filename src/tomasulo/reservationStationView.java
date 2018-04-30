package tomasulo;

import java.util.Arrays;

public class reservationStationView {
    public boolean free;
    public int opcode, instID;
    public final int[] tag;
    public final float[] val;
    public final boolean[] valid;

    public reservationStationView(){
        tag = new int[2];
        val = new float[2];
        valid = new boolean[2];
    }

    @Override
    public String toString() {
        return "reservationStationView State : " + ((free)?"FREE" : "NOT FREE") + "\n" +
                " opcode : " + opcode + "\n" +
                " instID : " + instID + "\n" +
                " tag    : " + Arrays.toString(tag) + "\n" +
                " val    : " + Arrays.toString(val) + "\n" +
                " valid  : " + Arrays.toString(valid) + "\n";
    }
}
