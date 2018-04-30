package tomasulo;

public interface peripheralsInterface {
    // registers related interface
    public void markRegister(int reg, int ROBid) throws Exception;
    public void setRegister(int reg, float val, int ROBid) throws Exception;
    public pair<Integer, Float> getRegister(int reg) throws Exception;
    
    // memory related interface
    public void markMemory(int address, int ROBid) throws Exception;
    public void writeMemory(int address, float val) throws Exception;
    public pair<Integer, Float> readMemory(int address) throws Exception;

    // peripherals view
    public peripheralView getPeriperalView();
}
/**
 * markRegister(int reg, int ROBid):
 *      this function is to be used be ROB to mark the value in some register as invalid
 *      and the value of ROBid is given instead
 * 
 * setRegister(reg, val):
 *      this function is to be sued be the ROB to commit the instuction whn writting to the register file
 * 
 * getRegister(reg):
 *      this function is to be used by the RS to get the values for the instruction to get ready to execute
 *      it returns 
 *      1 - int : ROBid (if -1 then value is ready)
 *      2 - float : the value
 *      
 */