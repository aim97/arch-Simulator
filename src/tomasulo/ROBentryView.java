package tomasulo;

public class ROBentryView {
    public String label;
    public int opCode;
    public int instID;
    public int dest;
    public float value;
    public boolean done;
    public boolean free;

    @Override
    public String toString(){
        String cur = "";
        cur += ("Entry State        : " + ((free)?"FREE":"BUSY") + "\n");
        cur += ("instruction id     : " + instID + "\n");
        cur += ("instruction opcode : " + opCode + "\n");
        cur += ("instruction dest   : " + dest + "\n");
        cur += ("instruction result : " + value + "\n");
        cur += ("instruction state  : " + ((done)?"DONE":"NOT DONE") + "\n");
        if(opCode == 3 || opCode == 4)cur += ("instruction label : " + label + "\n");
        return cur;
    }
}
