package tomasulo;

public class ROBview {
    public int issuePtr;
    public int commitPtr;
    public String nextInst;
    public ROBentryView roBentryViews[];

    public ROBview(int n){
        roBentryViews = new ROBentryView[n];
    }
}
