package tomasulo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class instruction implements instructionInterface{
    private final int instID;
    private final int opcode;
    private final String[] val;
    private final int[] type;

    
    public instruction(int instid, String inst) throws Exception{
        instID = instid;     // store the instruction ID
        val = new String[3];
        type = new int[3];
        
        String op = "";
        int sz = inst.length();
        // store the op code in op
        for(int i = 0 ; i < sz; i++){
            if(inst.charAt(i) == ' ')break;
            else op += inst.charAt(i);
        }
        
        opcode = operationCode(op); // calculate the opcode of instruction
        
        sz = op.length();
        op = inst.substring(sz);    // cut the op code from the instruction string
        String[] a; 
        a = op.split(",");    // separate instruction operands
        
        for(int i = 0;i < a.length;i++){
            a[i] = a[i].trim();
            val[i] = a[i];
            type[i] = getValType(val[i]); // get the type of operand 1
        }
        if(!validateParameters())throw new Exception("wrong parameter format > " + op);
    }
    
    private int operationCode(String op) throws Exception{
        // this function maps the string opcode into a numeric opcode
        // each set of instructions is separated from others to prevent conflict for further possible updates
        if(op.equals("mov"))return 0;
        if(op.equals("add"))return 1;
        if(op.equals("sub"))return 2;
        if(op.equals("beq"))return 3;
        if(op.equals("bne"))return 4;
        
        if(op.equals("mul"))return 10;
        if(op.equals("div"))return 11;
        
        if(op.equals("lw") )return 20;
        if(op.equals("sw") )return 21;
        throw new Exception("unknow intruction opcode!");
    }
    
    private int getValType(String v) throws Exception{
        String pattern = "^F\\d*$";
        Pattern PatternC = Pattern.compile(pattern);
        Matcher MatchPattern = PatternC.matcher(v);
        if(MatchPattern.matches())return 0; // means the value is register
        
        pattern = "^[0-9]+\\.?[0-9]*$";
        PatternC = Pattern.compile(pattern);
        MatchPattern = PatternC.matcher(v);
        if(MatchPattern.matches())return 1; // means the value is immediate value

        pattern = "^[a-zA-Z].*$";
        PatternC = Pattern.compile(pattern);
        MatchPattern = PatternC.matcher(v);
        if(MatchPattern.matches())return 2; // means the value is label
        else throw new Exception("invalid parameter " + v);
    }

    private boolean validateParameters(){
        if(opcode >= 20)return validateMemoryAcess();
        if(opcode == 3 || opcode == 4)return validateBranch();
        return validateNormal();
    }

    private boolean validateNormal(){
        if(type.length != 3)return false;
        if(type[0] != 0)return false;
        if(type[1] > 1 || type[2] > 1)return false;
        return true;
    }

    private boolean validateMemoryAcess(){
        if(type.length != 2)return false;
        if(type[0] != 0)return false;
        if(type[1] > 1)return false;
        return true;
    }

    private boolean validateBranch(){
        if(type.length != 3)return false;
        if(type[0] != 2)return false;
        if(type[1] > 1 || type[2] > 1)return false;
        return true;
    }
    
    /*___________________________________________________________________________________________*/
    
    @Override
    public int getInsturctionType() throws Exception{
        if(opcode < 10)return 0;
        else if(opcode < 20)return 1;
        else if (opcode < 30)return 2;
        throw new Exception("unkown type!");
    }
    
    @Override
    public int getOpCode(){
        return opcode;
    }
    
    @Override
    public pair<Float, Integer> getArg(int i)throws Exception{
        if(type[i] == 0) return new pair<>(Float.valueOf(val[i].substring(1)), type[i]);
        else if(type[i] == 1) return new pair<>(Float.valueOf(val[i]), type[i]);
        throw new Exception(String.format("instruction argument %d (: %s ) is not a number", i, val[i]));
    }

    @Override
    public int getInstID() {
        return instID;
    }

    @Override
    public int getDest() {
        if(opcode == 3 || opcode == 4)return -1;
        String s = val[0].substring(1);
        return Integer.valueOf(s);
    }

    @Override
    public String getTarget()throws Exception{
        if(opcode != 3 && opcode != 4)throw new Exception("this is not a jump instruction");
        return val[0];
    }


    // display instruction contents
    @Override
    public String toString(){
        String ret = "";
        // instruction basic info
        ret += String.format("instruction ID     : %d\n", instID);
        ret += String.format("instruction opcode : %d\n", opcode);
        try {
            ret += String.format("instruction type   : %d\n", this.getInsturctionType());
        }catch(Exception e){System.err.println("printing instruction : " + e);}

        // instruction parameter data
        for(int i = 0;i < val.length;i++) {
            ret += ("parameter : " + i + " , type(0:register/1:value/2:label) " + type[i] + " , value " + val[i] + "\n");
            if(opcode != 3 && i == 0)ret += "(Destination)\n";
        }

        // instruction target
        if(opcode == 3 || opcode == 4)ret += "next intruction label : " + val[0];
        return ret;
    }
}
