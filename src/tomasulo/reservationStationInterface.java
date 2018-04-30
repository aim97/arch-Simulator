package tomasulo;
public interface reservationStationInterface {
    public boolean isFree();
    public void fill(int robID, instruction inst) throws Exception;
    public void free() throws Exception;
    public boolean isReady();
    public void updateVals() throws Exception;
    
    public int getInstOpCode();
    public pair<Float, Float> getVals();
    public int getROBid();
    public int getInstID();
    public reservationStationView getRSview();
}
/**
 * isFree(): 
 *          returns if the reseravtion station is used for an instruction or not
 *          true : free, false : used
 * 
 * fill(robID, instruction):
 *          it adds the instruction with given reservation station to this reservation station
 *          throws exception IF reservation station already used
 * 
 * free(): 
 *          frees the reservation station.
 * 
 * isReady():
 *          returns if the values in the reservation station are ready for use
 *          throws exception IF reservation station is free
 * 
 * updateVals():
 *          this function checks the ROB to see if values are ready or not;
 *          and update current values if they are
 * 
 * getInstOpCode(): int
 *          it returns the opcod of instrucion currently in the RS
 * 
 * getVals(): pair<Float, Float> 
 *          returns the values of the instructions parameters : val0, val1
 * 
 * getROBid(): int
 *          returns the ROBid of the 
 * getInstID(): int
 *          returns the instruction id
 */