package tomasulo;

public class ROBentry implements ROBentryInterface{
    // instruction data
    private String label;
    private int opCode;
    private int instID;
    private int dest;
    private float value;

    // state of instruction in it
    private boolean done;

    // represents rob entry state
    private boolean free;

    public ROBentry(){
        label = "";
        value = 0;
        done = false;
        free = true;
        opCode = -1;
        instID = -1;
        dest = -1;
    }

    @Override
    public void assignToInst(instruction inst) throws Exception {
        instID = inst.getInstID();
        opCode = inst.getOpCode();
        free = false;
        done = false;
        if(opCode == 3 || opCode == 4) label = inst.getTarget(); // branch
        else dest = inst.getDest();
    }

    @Override
    public void FreeEntry(){
        label = "";
        value = 0;
        done = false;
        free = true;
        opCode = -1;
        instID = -1;
        dest = -1;
    }

    @Override
    public int getDest()throws Exception{
        if(free) throw new Exception("ROBentry\\getDest() : Entry is free");
        if(opCode == 3)throw new Exception("ROBentry\\getDest() : this is a branch insruction");
        return dest;
    }

    @Override
    public String getLabel()throws Exception{
        if(free) throw new Exception("ROBentry\\getLabel() : Entry is free");
        if(opCode != 3 && opCode != 4)throw new Exception("ROBentry\\getLabel() : this is not a branch insruction");
        return label;
    }

    @Override
    public float getValue()throws Exception{
        if(free) throw new Exception("ROBentry\\getValue() : Entry is free");
        return value;
    }

    @Override
    public boolean isDone()throws Exception{
        if(free) throw new Exception("ROBentry\\isDone() : Entry is free");
        return done;
    }

    @Override
    public void markDone(float val)throws Exception{
        if(free) throw new Exception("ROBentry\\markDone() : Entry is free");
        done = true;
        value = val;
    }

    @Override
    public int getInstID(){
        return instID;
    }

    @Override
    public boolean isFree(){
        return free;
    }

    @Override
    public int getOpCode(){
        return opCode;
    }

    @Override
    public ROBentryView getROBentryView(){
        ROBentryView rv = new ROBentryView();
        rv.dest = dest;
        rv.done = done;
        rv.free = free;
        rv.instID = instID;
        rv.label = label;
        rv.opCode = opCode;
        rv.value = value;
        return rv;
    }
}
