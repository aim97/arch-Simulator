package tomasulo;

// todo lw and sw are yet to be handled
public class ALU implements ALUinterface{
    private int opcode, ROBid, instID;
    private final Float val[];
    
    private int counter;
    private final peripherals p;
    private final ROB rob;
    
    public ALU(peripherals p, ROB rob){
        val = new Float[2];
        val[0] = 0.0f;
        val[1] = 0.0f;
        counter = -1;
        this.p = p;
        this.rob = rob;
    }
    
    private void setCounter(){
        // those numbers are random they can be actually anything
        // those numbers represent the number of cycles it takes to execute each type of instructions
        if(opcode == 0)counter = 1;   // mov
        if(opcode == 1)counter = 1;   // add
        if(opcode == 2)counter = 1;   // sub
        if(opcode == 3)counter = 3;   // beq
        if(opcode == 4)counter = 3;   // bne

        if(opcode == 10)counter = 4;  // mul
        if(opcode == 11)counter = 4;  // div

        if(opcode == 20)counter = 5;  // lw
        if(opcode == 21)counter = 5;  // sw
    }
    
    private float calcAnswer() throws Exception{
        // the mov instruction is probably removed now
        if(opcode == 1) return val[0] + val[1];
        if(opcode == 2) return val[0] - val[1];
        if(opcode == 3){
            // this a beq instruction : we branch if the difference is 0 or close to it
            if(Math.abs(val[0] - val[1]) < 1e-6)return 100;
            else return 0;
        }
        if(opcode == 4){
            // this a bne instruction : we branch if the difference is not (0 or close to it)
            if(Math.abs(val[0] - val[1]) < 1e-6)return 0;
            else return 100;
        }

        if(opcode == 10) return val[0] * val[1];
        if(opcode == 11) return val[0] / val[1];

        // todo lw and sw are yet to be handled here
        if(opcode == 20) return val[0];
        if(opcode == 21) return val[0];
        throw new Exception("insupported opcode");
    }
    
    @Override
    public boolean isFree(){
        return counter == -1;
    }
    
    @Override
    public boolean execute(boolean f) throws Exception{
        if(counter > 0){
            counter --;
            return false;
        }else if(counter == 0 && !f){
            rob.markDone(instID, ROBid, calcAnswer());
            counter = -1;
            return true;
        }else return false;
    }
    
    @Override
    public void addInstruction(reservationStation rs)throws Exception{
        if(!isFree())throw new Exception("this ALU is already executing");
        opcode = rs.getInstOpCode();
        instID = rs.getInstID();
        ROBid = rs.getROBid();
        setCounter();
        val[0] = rs.getVals().first;
        val[1] = rs.getVals().second;
    }
    
    @Override
    public int getInstID(){
        return instID;
    }

    @Override
    public ALUview getALUview(){
        ALUview vu = new ALUview();
        vu.counter = counter;
        vu.instID = instID;
        vu.opcode = opcode;
        vu.val[0] = val[0];
        vu.val[1] = val[1];
        return vu;
    }
}
