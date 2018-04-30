package tomasulo;

import java.util.ArrayList;

public interface processingUnitInterface {
    public boolean hasFreeRS();
    public void issue(int robid, instruction inst)throws Exception;
    public boolean execute(boolean flag) throws Exception;
    
    public boolean isFree();
    public void update() throws Exception;
}
