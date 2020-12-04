public class Edge {

    private Node u,v;
    private int type;
    private String label;

    public Edge(Node u, Node v, int type){
        this.u = u;
        this.v = v;
        this.type = type;
    }

    public Edge(Node u, Node v, int type, String label){
        this.u = u;
        this.v = v;
        this.type = type;
        this.label = label;
    }

    public Node firstEndpoint() {
        return this.u;
    }

    public Node secondEndpoint() {
        return this.v;
    }

    public int getType(){
        return this.type;
    }

    public void setType(int newType){
        this.type = newType;
    }

    public String getLabel(){
        return this.label;
    }

    public void setLabel(String newLabel){
        this.label = label;
    }
}
