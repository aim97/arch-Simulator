package tomasulo;

import java.util.ArrayList;
import java.util.HashMap;

public class instructionQueue implements instructionQueueInterface{
    private final ArrayList<instruction> instructions;
    private int programCounter;
    private final HashMap<String, Integer> labels;
 
    instructionQueue(){
        labels = new HashMap<>();
        instructions = new ArrayList<>();
        programCounter = 0;
    }
    
    @Override
    public void setProgram(ArrayList<String> insts) throws Exception{
        instructions.clear();
        labels.clear();
        int sz = insts.size();
        // initialize instruction in the Queue
        for(int i = 0;i < sz;i++){
            String a[] = insts.get(i).split(":");
            labels.put(a[0], i);
            if(a.length == 1) instructions.add(new instruction(i, a[0]));
            else instructions.add(new instruction(i, a[1]));
        }
    }
    
    @Override
    public void setPC(String label){
        programCounter = labels.get(label);
    }

    @Override
    public instruction getNextInstruction() throws Exception{
        programCounter += 1;
        if(programCounter - 1 == instructions.size())throw new Exception("instructions are over");
        return instructions.get(programCounter - 1);
    }

    @Override
    public int getNextInstructionType() throws Exception{
        if(programCounter == instructions.size())throw new Exception("instructions are over");
        return instructions.get(programCounter).getInsturctionType();
    }

    @Override
    public boolean isProgramOver() {
        return programCounter == instructions.size();
    }

    @Override
    public int getInstCount(){return instructions.size();}
    // IQ view

    @Override
    public int getProgramCounter(){
        return programCounter;
    }
}
