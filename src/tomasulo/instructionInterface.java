package tomasulo;

public interface instructionInterface {
    int getInsturctionType() throws Exception;
    int getOpCode();
    int getInstID();
    pair<Float, Integer> getArg(int i)throws Exception;
    int getDest();
    String getTarget()throws Exception; // returns the target to jump to if instruction is a branch
}

/**
 * note : constructor will need to be updated to include the jump address for BEQ instruction
 * 
 * getInsturctionType():string
 *      returns if the insturction type : the ID of ALU it should execute on
 *      mov, add, sub, beq > 0;
 *      mul, div > 1
 *      lw, sw > 2
 *      
 * getOpCode():int
 *      returns the opcode of instructions
 *      mov 0
 *      add 1
 *      sub 2
 *      beq 3
 * 
 *      mul 10
 *      div 11
 * 
 *      lw  20
 *      sw  21
 * 
 * getInstID():int
 *         returns the id of the instruction
 * 
 * getArgs(int i):pair< Float, int>
 *         it returns the value of of the ith argument
 *         Float : the register number, or the immediate
 *         int   : type of argument
 *         
 * getDest():int
 *         it returns the register or memory address to store the value to.
 * 
 * 
 * possible instruction formats
 * mov dest, src
 * add dest, src
 * sub dest, src
 * beq arg1, arg2, instructionID // still not completely supported
 * 
 * mul dest, src
 * div dest, src
 * 
 * lw dest, src
 * sw dest, src
 */