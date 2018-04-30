package tomasulo;

import java.util.ArrayList;

public interface ArchInterface {
    public machineState nextStep() throws Exception;
    public boolean isOver();
    public machineState setProgram(ArrayList<String> insts) throws Exception;
}

/**
 * issue():
 *      this function issue instructions from the Instruction Queue
 *      if correct reservation station is found:
 *              place the instruction in it.
 *              place the instruction in the ROB as issued (assuming there is clear place in it).
 *              the ROB should return the entry number for this instruction.
 *              mark the RAT for the distination to point to the entry in the ROB.
 * dispatch():
 *      this function should call all our processing units to:
 *      if not currently executing:
 *          start executing a ready instruction if there are any and free its reservation station.
 *      if currently executing:
 *          decrement the number of cycles to be taken by the currently executing instruction.
 *          if the currently executing instruction is over:
 *              write the value to the CDB along side with the ROB address for it.
 *              mark the processing unit.
 * 
 * update():
 *      this function should call all reseravtion stations and ROB to update them selves
 *      1 - reservation stations:
 *          they should check if any value needed by them is on the CDB currently or not
 *          if found update the value, and mark the value received
 *      2 - ROB:
 *          mark the instruction as done.
 * 
 * reOrder():
 *
 *
 *      void issue();
 *     void dispatch();
 *     void update();
 *     void reOrder();
 *     void commit();
 */