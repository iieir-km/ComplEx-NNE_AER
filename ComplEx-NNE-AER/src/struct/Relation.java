package struct;

public class Relation {
    private int rid;
    private int dir;
    public Relation(int rid, int dir){
        this.rid = rid;
        this.dir = dir;
    }
    public int rid(){
        return rid;
    }
    public int direction(){
        return dir;
    }
}
