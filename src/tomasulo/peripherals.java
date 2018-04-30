package tomasulo;


public class peripherals implements peripheralsInterface{
    private final int REGS_COUNT;
    private final int MEM_SIZE;

    private final float regsFile[];
    private final int RAT[];

    private final int memRAT[];
    private final float memory[];
    
    public peripherals(){
        REGS_COUNT = 16;
        MEM_SIZE = 1000;

        regsFile = new float[REGS_COUNT];
        RAT = new int[REGS_COUNT];
        setRegs();
        
        memory = new float[MEM_SIZE];
        memRAT = new int[MEM_SIZE];
    }
    
    private void setRegs(){
        for(int i = 0;i < REGS_COUNT;i++){
            regsFile[i] = 0.0f;
            RAT[i] = -1;
        }
    }

    @Override
    public void markRegister(int reg, int ROBid) throws Exception{
        int sz = regsFile.length;
        if(reg >= 0 && reg < sz){
            RAT[reg] = ROBid;
        }else throw new Exception("register " + reg + " doesn't exist");
    }

    @Override
    public void setRegister(int reg, float val, int ROBid) throws Exception{
        if(reg >= 0 && reg < MEM_SIZE){
            if(ROBid == RAT[reg]) {
                regsFile[reg] = val;
                RAT[reg] = -1;
            }
        }else throw new Exception("register " + reg + " doesn't exist");
    }

    @Override
    public pair<Integer, Float> getRegister(int reg) throws Exception{
        int sz = regsFile.length;
        if(reg >= 0 && reg < sz){
            float f = RAT[reg];
            if(f == -1.0f)return new pair<>(-1, regsFile[reg]);
            else return new pair<>(RAT[reg], -1.0f);
        }else {
            throw new Exception("register " + reg + " doesn't exist");
        }
    }

    @Override
    public void markMemory(int address, int ROBid) throws Exception{
        if(address >= 0 && address < MEM_SIZE){
            memRAT[address] = ROBid;
        }else throw new Exception("memory address " + address + " doesn't exist");
    }

    @Override
    public void writeMemory(int address, float val) throws Exception{
        if(address >= 0 && address < MEM_SIZE){
            memory[address] = val;
            memRAT[address] = -1;
        }else throw new Exception("Memeory address doesn't exist");
    }

    @Override
    public pair<Integer, Float> readMemory(int address) throws Exception{
        if(address >= 0 && address < MEM_SIZE){
            float f = RAT[address];
            if(f == -1.0f)return new pair<>(-1, memory[address]);
            else return new pair<>(memRAT[address], -1.0f);
        }else throw new Exception("register " + address + " doesn't exist");
    }

    @Override
    public peripheralView getPeriperalView(){
        peripheralView pv = new peripheralView();
        System.arraycopy(regsFile, 0, pv.regsFile, 0, regsFile.length);
        System.arraycopy(RAT, 0, pv.RAT,0, RAT.length);
        System.arraycopy(memory, 0, pv.memory, 0, memory.length);
        return pv;
    }
}
