package tomasulo;

public interface ROBinterface {
    public boolean hasFreeEntry();
    public int issue(instruction inst) throws Exception;
    public void markDone(int instID, int ROBentryID, float val) throws Exception;
    public int commit() throws Exception;
    public pair<Boolean, Float> getVal(int ROBentryID) throws Exception;
    public String getLabel();
    public ROBview getROBview();
    public boolean isOver();
}

/**
 * boolean mark
 * float value
 * string jumpLabel
 * instruction id
 * 
 * ROB needs to contact with:
 * 1 - IQ : to issue instructions in it and give back the ROBid of instructions
 * 2 - ALU : to mark instructions as completed
 * 3 - RS : for the broadcast.
 * 4 - with arch : to commit done instructions each cycle if poissible
 * 
 * ROB needs to know
 * 1 - opcode of instruction: to know how to commit it (specially for BEQ, and SW)
 * 2 - Destination of instruction result (this will be the address to store instuction in) (this will be string for registers and integer for memeory)
 * 3 - Done bit : to mark if the value is valid or not
 * 4 - value    : the value to be stored in register or memory when instruction is commited
 * 5 - reference to peripherals : to commit instructions with it(write to register file or memory)
 * 
 * methods
 * 1 - issue(instruction): ROBid
 *          this method attemps to issue an instruction into the ROB circular queue
 *          if there is an op   en place for it.
 *              instruction opcode, destination address are stored.
 *              instruction marked as not done yet.
 *              value in the destination register is marked in the RAT as invalid
 *              increment issue pointer.
 *              then return the ROBid of instruction
 *          else throw an exception "there is not free entries in ROB"
 * 
 * 2 - markDone(ROBid, value):void
 *          if ROBid out of range : throw an exception
 *          mrak entry as done
 *          store the value in it.
 *          
 * 3 - getVal(ROBid):pair< Boolean, Float >
 *          if ROBid out of range : throw an exception
 *          if ROBentry value is DONE: return new pair(true, value);
 *          else return new pair(false, -1);
 * 
 * 4 - commit():void
 *          if next instruction is not done do nothing
 *          else:
 *              if next instruction is not branch:
 *                  commit the instruction based on the its opcode
 *                  (SW will need to store value in memory, while all others will store in a register)
 *              else if instruction is a branch:
 *                  the value computed is 0.0f if branch is not taken, otherwise it's taken
 *                  if branch is taken:
 *                      flush the ROB (get the issue pointer to point to the same as commit pointer)
 *              incement commit pointer
 */
