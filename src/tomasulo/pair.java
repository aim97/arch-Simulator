package tomasulo;

public class pair<T1, T2> {
    public T1 first;
    public T2 second;
    
    public pair(T1 f, T2 s){
        first = f;
        second = s;
    }

    @Override
    public String toString(){
        return first + " , " + second;
    }
}
