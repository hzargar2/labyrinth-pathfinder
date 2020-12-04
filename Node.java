public class Node {

    private int name;

    // All nodes initialized are not visited yet by default
    private boolean mark = false;

    // Constructor
    public Node(int name){
        this.name = name;
    }

    // Gets Node mark
    public boolean getMark(){
        return this.mark;
    }

    // Sets Node mark
    public void setMark(boolean mark){
        this.mark = mark;
    }

    // Gets Node name
    public int getName(){
        return this.name;
    }
}
