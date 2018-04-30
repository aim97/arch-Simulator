package tomasulo;

public class processingUnit implements processingUnitInterface{
    private final reservationStation rss[];
    private final ALU computingUnit;
    
    processingUnit(int n, peripherals p, ROB rob){
        rss = new reservationStation[n];
        for(int i = 0;i < n;i++)rss[i] = new reservationStation(p, rob);
        computingUnit = new ALU(p, rob);
    }
    
    private int findFreeRS() throws Exception{
        for(int i = 0;i < rss.length;i++){
            if(rss[i].isFree())return i;
        }
        throw new Exception("there are no free RS");
    }
    
    private void dispatch() throws Exception{
        if(computingUnit.isFree()) {
            for (int i = 0;i < rss.length;i++) {
                if (rss[i].isReady()) {
                    computingUnit.addInstruction(rss[i]);
                    rss[i].free();
                    break;
                }
            }
        }
    }

    @Override
    public void update() throws Exception {
        for(int i = 0;i < rss.length;i++){
            if(!rss[i].isFree())rss[i].updateVals();
        }
    }

    @Override
    public boolean hasFreeRS() {
        for (reservationStation rs : rss) 
            if (rs.isFree()) return true;
        return false;
    }

    @Override
    public void issue(int robid, instruction inst) throws Exception {
        if(!hasFreeRS())throw new Exception("there are no Free RS");
        int RSid = findFreeRS();
        rss[RSid].fill(robid, inst);
    }

    @Override
    public boolean execute(boolean flag) throws Exception{
        dispatch();
        return computingUnit.execute(flag);
    }

    @Override
    public boolean isFree() {
        return computingUnit.isFree();
    }

    public pair<ALUview, reservationStationView[]> getPUview(){
        pair<ALUview, reservationStationView[]> puv = new pair<>(new ALUview(), new reservationStationView[3]);
        for(int i = 0;i < 3;i++){
            // : set Reservation Station View
            puv.second[i] = rss[i].getRSview();
        }
        // : ALU view
        puv.first = computingUnit.getALUview();
        return puv;
    }

    @Override
    public String toString(){
        String ret = "-----------------------PROCESSING UNIT STATE----------------------\n";
        ret += "ALU state\n----------\n" + computingUnit.getALUview() + "\n";
        ret += "RESERVATION STATIONS STATE\n-----------------------\n";
        for(int i = 0;i < rss.length;i++){
            ret += "RESERVATION STATION " + i + ":\n-----------------------\n";
            ret += rss[i].getRSview() + "\n";
        }
        return ret + "---------------------------------------------";
    }
}
