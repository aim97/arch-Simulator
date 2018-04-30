package tomasulo;

public class ROB implements ROBinterface{
    private final ROBentry rob[];
    private int issuePtr, commitPtr, sz;
    private final peripherals p;

    private String nextInst;

    public ROB(int numberOfEntries, peripherals p){
        rob = new ROBentry[numberOfEntries]; // set the number of entries in the ROB to n
        for(int i = 0;i < numberOfEntries;i++)rob[i] = new ROBentry();

        // at beginning both issuePtr and commitPtr point to the same entry
        issuePtr = 0;
        commitPtr = 0;
        sz = 0;
        nextInst = "";  // this is the label of next instruction to execute if the last commited instruction was a branch
        this.p = p;
    }

    private int commitNormal(int ROBid) throws Exception{
        // for add, sub, div, mul, ld
        p.setRegister(rob[ROBid].getDest(), rob[ROBid].getValue(), ROBid); // store the value in the register
        int instID = rob[ROBid].getInstID();
        rob[ROBid].FreeEntry(); // mark the rob entry as free
        return instID;
    }

    private int commitSW(int ROBid)throws Exception{
        // for sw
        p.writeMemory(rob[ROBid].getDest(), rob[ROBid].getValue());
        int instID = rob[ROBid].getInstID();
        rob[ROBid].FreeEntry();
        return instID;
    }

    private void flush(){
        // go through all invalid ROB entries
        while(((issuePtr - 1 + rob.length) % rob.length) != commitPtr){
            rob[issuePtr].FreeEntry(); // free the entry
            issuePtr = (issuePtr - 1 + rob.length) % rob.length; // ge to the next one
        }
        sz = 1;// the rob is now emtpy.
    }

    private int commitBranch(int ROBid) throws Exception{
        // for beq
        int instID = rob[ROBid].getInstID();
        if(Math.abs(rob[ROBid].getValue()) > 10){
            nextInst = rob[ROBid].getLabel();
            flush();
        }
        rob[ROBid].FreeEntry();
        return instID;
    }


    @Override
    public boolean hasFreeEntry() {
        return sz < rob.length;
    }

    @Override
    public int issue(instruction inst) throws Exception{
        // the next cell can be either
        // 1 - invalid or free so we will just use it
        // 2 - pointed at by the commitPtr; which means the rob is full and we simply can't issue
        if(hasFreeEntry()){
            rob[issuePtr].assignToInst(inst); // let the instrction use the entry
            int ROBid = issuePtr;   // store the robid to return it
            issuePtr += 1;          // point at the next place to issue in
            issuePtr %= rob.length; // account for overflow
            sz += 1;                // increase the current sz by 1
            return ROBid;           // return the roBid to the listener
        }else throw new Exception("ROB\\isuue(): there are no free entries");
    }

    @Override
    public void markDone(int instID, int ROBentryID, float val) throws Exception{
        // todo: this function may need to be updated to account for BRANCH caused conditions
        if(ROBentryID < 0 || ROBentryID >= rob.length)throw new Exception("ROB\\markDone(): no such entry");
        if(rob[ROBentryID].isFree())throw new Exception("ROB\\markDone(): this ROB entry is free");
        // if an instruction is overwritten don't allow it to access the ROB
        if(rob[ROBentryID].getInstID() == instID)rob[ROBentryID].markDone(val);
    }

    @Override
    public int commit() throws Exception{
        nextInst = "";  // set the next instruction label to ""
        // if next instruction to commit is done
        if(!rob[commitPtr].isFree() && rob[commitPtr].isDone()){
            int instID;
            int opcode = rob[commitPtr].getOpCode();
            if(opcode == 3 || opcode == 4){
                // a branch
                instID = commitBranch(commitPtr);
            }else{
                // not a branch
                if(opcode == 21){
                    // SW
                    instID = commitSW(commitPtr);
                }else{
                    // not SW
                    instID = commitNormal(commitPtr);
                }
            }
            commitPtr += 1;
            commitPtr %= rob.length;
            sz -= 1;
            return instID;
        }
        return -1;
    }

    @Override
    public pair<Boolean, Float> getVal(int ROBentryID) throws Exception{
        if(ROBentryID < 0 || ROBentryID >= rob.length)throw new Exception("ROB\\getVal(): no such entry");
        return new pair<>(rob[ROBentryID].isDone(), rob[ROBentryID].getValue());
    }

    @Override
    public String getLabel(){
        return nextInst;
    }

    @Override
    public ROBview getROBview(){
        ROBview rv = new ROBview(rob.length);
        for(int i = 0;i < rob.length;i++){
            rv.roBentryViews[i] = rob[i].getROBentryView();
        }
        rv.nextInst = nextInst;
        rv.issuePtr = issuePtr;
        rv.commitPtr = commitPtr;
        return rv;
    }

    @Override
    public boolean isOver(){
        return sz == 0;
    }
}
