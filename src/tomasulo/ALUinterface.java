package tomasulo;

public interface ALUinterface {
    public boolean isFree();
    public boolean execute(boolean f) throws Exception;
    public void addInstruction(reservationStation rs)throws Exception;
    public int getInstID();
    public ALUview getALUview();
}
