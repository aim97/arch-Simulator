package tomasulo;

public class reservationStation implements reservationStationInterface{
    private boolean free; // holds if the RS is free or not
    
    private int ROBid, instID; 
    // the ROBid : we need this here to know where we will write in the ROB after execution
    // instID    : needed for simulation to tell the GUI which instruction is currently in this RS
    
    private int instOpCode;        // opCode for instruction : stored to tell the Processing Unit what to do with it
    private final int[] tag;       // tag array stores : the ROBid of needed value
    private final float[] val;     // tag array 
    private final boolean[] ready; // holds booleans to tell us if current value is valid or not(value is ready or not yet)
    
    private final peripherals myPeripherals;
    private final ROB rob; // this should be uncommented when the ROB code is over
    
    reservationStation(peripherals p, ROB rob){
        tag = new int[2];
        val = new float[2];
        ready = new boolean[2];
        free = ready[0] = ready[1] = true; // the RS is initially free
        myPeripherals = p;
        this.rob = rob;
    }
    
    private void setVals(pair<Float, Integer> data, int i) throws Exception{
        /*
         * pair< float, int> data : < value , type>
         * this function is used to get the values need for instruction computation from the instruction or register file
         * when the instruction is 1st sent to this RS
         */
        if(data.second == 1){ // if data is immediate
            ready[i] = true;  // then we already have the needed value
            val[i] = data.first; // so store it directly in values
        }else{
            // if the data is not immediate then it must be a register
            int reg = data.first.intValue();
            pair<Integer, Float> a = myPeripherals.getRegister(reg);
            if(a.first == -1){ // if the register is available in the regFile
                ready[i] = true;    // then we can get it and continue
                val[i] = a.second;
            }else{
                ready[i] = false;   // if not, then data is not available
                tag[i] = a.first; // just store the ROBid for needed data to be used latter
            }
        }
    }
    
    @Override
    public boolean isFree() {
        return free; // not comment needed
    }

    @Override
    public void fill(int robID, instruction inst) throws Exception{
        if(!free)throw new Exception("RESERVATIONSTATION\\fill() : this reservation station is full!");
        
        free = false;  // mark the RS as used
        ROBid = robID; // get the ROBid for current instruction
        instID = inst.getInstID(); // get the instruction id for current instruction
        
        instOpCode = inst.getOpCode(); // get the opCode for current instruction
        
        // get the values of required fields
        for(int i = 1;i < 3; i++){
            pair<Float, Integer> a = inst.getArg(i);
            setVals(a, i - 1);
            // todo lw and sw are yet to be handled here
            if(instOpCode >= 20)break; // if lw or sw we only need src parameter (actually there is not 3rd one)
        }
    }

    @Override
    public void free() throws Exception{
        if(free)throw new Exception("RESERVATIONSTATION\\free() : this reservation station is already free");
        free = ready[0] = ready[1] = true; // mark all as free
    }

    @Override
    public boolean isReady(){
        if(free)return false;
        // todo lw and sw are yet to be handled here
        if(instOpCode >= 20)return ready[0]; // instruction is load then we only need 2nd parameter, the 1st one is the destiantion address (same for store)
        else return ready[0] && ready[1];   // both values must be their for instructions other than load
    }

    @Override
    public void updateVals() throws Exception{
        if(free)throw new Exception("RESERVATIONSTATION\\updateVals() : this reservation station is already free");

        for(int i = 0;i < val.length;i++){
            if(!ready[i]){
                pair<Boolean, Float> v = rob.getVal(tag[i]);
                if(v.first){
                    val[i] = v.second;
                    ready[i] = true;
                }
            }
            // todo lw and sw are yet to be handled here
            if(instOpCode >= 20)break; // if lw or sw: we only have first parameter
        }
    }

    @Override
    public int getInstOpCode() {
        return instOpCode;
    }

    @Override
    public pair<Float, Float> getVals() {
        return new pair<>(val[0], val[1]);
    }

    @Override
    public int getROBid() {
        return ROBid;
    }

    @Override
    public int getInstID() {
        return instID;
    }

    @Override
    public reservationStationView getRSview(){
        reservationStationView rs = new reservationStationView();
        rs.free = free;
        rs.instID = instID;
        rs.opcode = instOpCode;
        System.arraycopy(tag, 0, rs.tag, 0, tag.length);
        System.arraycopy(val, 0, rs.val, 0, val.length);
        System.arraycopy(ready, 0, rs.valid, 0, ready.length);
        return rs;
    }
}
