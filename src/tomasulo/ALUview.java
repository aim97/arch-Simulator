package tomasulo;

import java.util.Arrays;

public class ALUview {
    public int instID, opcode, counter;
    public float val[];

    public ALUview(){
        val = new float[2];
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "ALU state          : " + ((counter < 0)?"FREE":"NOT FREE") + "\n";
        ret += "instruction opcode : " + opcode + "\n";
        ret += "instruction ID     : " + instID + "\n";
        ret += "CPU counter        : " + counter + "\n";
        ret += "parameters         : " + Arrays.toString(val);
        return ret;
    }
}
