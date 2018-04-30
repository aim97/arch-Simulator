package tomasulo;

public interface ROBentryInterface {
    public void assignToInst(instruction inst) throws Exception;
    public void FreeEntry();
    public int getDest()throws Exception;
    public String getLabel()throws Exception;
    public float getValue()throws Exception;
    public boolean isDone()throws Exception;
    public void markDone(float val)throws Exception;
    public int getInstID();
    public boolean isFree();
    public int getOpCode();
    public ROBentryView getROBentryView();
}
