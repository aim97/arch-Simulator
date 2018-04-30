package tomasulo;

import java.util.ArrayList;

public class machineState {
    public int CurrentCycle;
    public int programCounter;
    public peripheralView pv;
    public ROBview rv;
    public pair<ALUview, reservationStationView[]> puvs[];


    public machineState(int robLength){
        // set PU view
        puvs = new pair[3]; //(new ALUview(), new reservationStationView[3]);
        for(int i = 0 ;i < 3;i++){
            puvs[i] = new pair<>(new ALUview(), new reservationStationView[3]);
            for(int j = 0;j < 3;j++){
                puvs[i].second[j] = new reservationStationView();
            }
        }

        // set ROB view
        rv = new ROBview(robLength);

        // set peripherial view
        pv = new peripheralView();
    }

    @Override
    public String toString(){
        String ms = "----------------------------------<MACHINE STATE>----------------------------------\n";
        ms += "IQ state\n-----------\n";
        ms += "program counter : " + programCounter + "\n";
        ms += "Current Cycle   : " + CurrentCycle + "\n";
        ms += "________________________________________\n";
        ms += "Peripherals state\n-----------------\n";
        for(int i = 0;i < 4;i++){
            for(int j = 0;j < 4;j++){
                int idx = i * 4 + j;
                ms += "F" + idx + " : " + pv.regsFile[idx] + " , " + pv.RAT[idx] + "\t|\t";
            }
            ms += "\n";
        }
        ms += "________________________________________\n";
        ms += "Processing Units State\n----------------------------\n";
        for(int i = 0;i < puvs.length;i++){
            ms += "Processing unit" + i + "\n-----------------\n";
            ms += "ALU state" + i +"\n----------\n" + puvs[i].first + "\n";
            ms += "RESERVATION STATIONS STATE\n-----------------------\n";
            for(int j = 0;j < puvs[i].second.length;j++){
                ms += "RESERVATION STATION " + j + ":\n-----------------------\n";
                ms += puvs[i].second[j] + "\n";
            }
            ms += "---------------------------------------------\n";
        }
        ms += "________________________________________\n";
        ms += "ROB state\n----------\n";
        ms += "issue pointer  : " + rv.issuePtr + "\n";
        ms += "commit pointer : " + rv.commitPtr + "\n";
        for(int i = 0;i < rv.roBentryViews.length; i++){
            ms += "ROB ENTRY " + i + " : \n" + rv.roBentryViews[i] + "\n";
        }
        ms += "________________________________________\n";
        return ms;
    }
}
