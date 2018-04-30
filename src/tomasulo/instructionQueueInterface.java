package tomasulo;

import java.util.ArrayList;

public interface instructionQueueInterface {
    public void setProgram(ArrayList<String> insts) throws Exception;
    public void setPC(String label);
    public instruction getNextInstruction() throws Exception;
    public int getNextInstructionType() throws Exception;
    public boolean isProgramOver();
    public int getInstCount();
    public int getProgramCounter();
}
