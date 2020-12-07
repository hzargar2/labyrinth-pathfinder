public class Edge {

    // Instance variables for class

    private Node u,v;
    private int type;
    private String label;

    // Constructor
    public Edge(Node u, Node v, int type){
        this.u = u;
        this.v = v;
        this.type = type;
    }

    // Another constructor
    public Edge(Node u, Node v, int type, String label){
        this.u = u;
        this.v = v;
        this.type = type;
        this.label = label;
    }

    // Gets the first Node enpoint in the edge
    public Node firstEndpoint() {
        return this.u;
    }

    // Gets the second Node endpoint in the edge
    public Node secondEndpoint() {
        return this.v;
    }

    // Gets the type of the edge
    public int getType(){
        return this.type;
    }

    // Sets the type of the edge
    public void setType(int newType){
        this.type = newType;
    }

    // Gets the label of the edge
    public String getLabel(){
        return this.label;
    }

    // Sets the label of the edge
    public void setLabel(String newLabel){
        this.label = label;
    }
}
