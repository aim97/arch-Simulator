package tomasulo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class arch implements ArchInterface{
    /**
     * this class should be the one responsible for calling every thing and handling it with GUI
     * =============================================================
     * Fields
     * ______
     * 1 - IQ
     * 2 - 2 processing units
     * 3 - ROB
     *
     * methods
     * _______
     * - nextStep():state
     *      rule : this function is the main of this class
     *             it handels all steps in the architecture work.
     *
     * - setCode(array of instructions):
     *      rule : takes an array of instructions to execute.
     *
     * - issue():void
     *      this function does the issue process for the instructions from the IQ.
     *
     * - execute():void
     *      this function does the calling of function to execute the job for each unit.
     *
     * - update():void
     *
     *
     * - commit():
     *      this function calls the ROB commit to select an instruction to commit.
     */
    private int CLOCK_CYCLE_COUNT;
    private final int ROBSIZE = 8;
    private BufferedWriter BW;

    private instructionQueue IQ;
    private peripherals p;
    private ROB rob;
    private processingUnit[] PUs;
    private boolean isOver;

    public arch() throws Exception{
        CLOCK_CYCLE_COUNT = 0;

        isOver = false;
        BW = new BufferedWriter(new FileWriter("arch.log"));

        IQ = new instructionQueue();
        p = new peripherals();
        rob = new ROB(ROBSIZE, p);
        
        PUs = new processingUnit[3];
        for(int i = 0;i < 3;i++){
            PUs[i] = new processingUnit(3, p, rob);
        }
    }
    
    private void issue() throws Exception{
        if(IQ.isProgramOver())return;
        if(rob.hasFreeEntry()){                             // we can only issue if ROB has free entries
            int neededALU = IQ.getNextInstructionType();    // specify th ALU to do the job depeding on instruction type

            if(PUs[neededALU].hasFreeRS()){                 // if this ALU has free area in it
                instruction inst = IQ.getNextInstruction(); // get the instruction from the IQ
                int ROBid = rob.issue(inst);                // issue the instruction in the ROB and remember the ROBid
                PUs[neededALU].issue(ROBid, inst);          // issue the instruction into the reservation station
                if(inst.getDest() >= 0)p.markRegister(inst.getDest(), ROBid);      // mark the destination register with the ROBid
            }
        }
    }
    
    private void execute() throws Exception{
        boolean flag = false;
        for(processingUnit PU : PUs){
            flag |= PU.execute(flag);
        }
    }

    private int commit() throws Exception{
        int instID = rob.commit();
        String label = rob.getLabel();
        if(!label.equals(""))
            IQ.setPC(label);
        return instID;
    }

    private void update()throws Exception{
        for(int i = 0;i < PUs.length;i++){
            PUs[i].update();
        }
    }

    private machineState getCurrentState() throws Exception{
        machineState ms = new machineState(ROBSIZE);
        ms.programCounter = IQ.getProgramCounter();
        ms.CurrentCycle = CLOCK_CYCLE_COUNT;
        ms.pv = p.getPeriperalView();
        ms.rv = rob.getROBview();
        for(int i = 0;i < 3;i++){
            ms.puvs[i] = PUs[i].getPUview();
        }
        BW.write(ms.toString() + "\n");
        BW.flush();
        return ms;
    }

    @Override
    public machineState setProgram(ArrayList<String> insts) throws Exception{
        isOver = false;
        IQ.setProgram(insts);
        return getCurrentState();
    }

    @Override
    public boolean isOver() {
        return (IQ.isProgramOver() && rob.isOver());
    }

    @Override
    public machineState nextStep() throws Exception{
        // todo : update this to returns the result after each step, i mean each issue, commit, update, ..etc
        // todo : update this to make it act like tomasulo architecture sequence.
        issue();
        update();
        execute();
        update();
        int instID = commit();
        if(instID == (IQ.getInstCount() - 1)) isOver = true;
        CLOCK_CYCLE_COUNT += 1;
        return getCurrentState();
    }
}