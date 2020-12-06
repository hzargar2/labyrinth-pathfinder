import java.util.Iterator;
import java.util.LinkedList;

public class Graph implements GraphADT{

    // Initializes a graph using adjacency list
    public Node[] V;
    // THis is an array of linked lists that store Edge objects
    public LinkedList<Edge>[] E;

    public Graph(int n){

        this.V = new Node[n];

        // Initializes all the nodes with the right names between 0 and n-1
        for (int i = 0; i<n; i++){
            this.V[i] = new Node(i);
        }

        this.E = new LinkedList[n];

        // Initializes all Linked Lists in E
        for (int i = 0; i<n; i++){
            this.E[i] = new LinkedList<Edge>();
        }
    }

    // Checks to see if a node already exists in the graph and returns a boolean
    private boolean nodeExists(Node u){

        // Iterates through nodes in V and checks to see if any of their name is the same as the name of node u
        for (Node node : this.V) {
            if (node.getName() == u.getName()) {
                return true;
            }
        }
        return false;
    }

    // Checks to see if an edge already exists in the graph between 2 nodes
    private boolean edgeExists(Node u, Node v){

        // Gets all the edges for 1 of the nodes since either linkedlist should have their edge if it exists
        LinkedList<Edge> linkedlist = this.E[u.getName()];

        // Iterates through all the linked lists

        for (Edge edge : linkedlist) {

            // Checks to see if the (first endpoint is the same as u and the second endpoint is the same as v) or
            // (first endpoint is the same as v and the second endpoint is the same as u), in case their position are
            // swapped since an Edge (1,2) is still the same as Edge(2,1)

            if ((edge.firstEndpoint().getName() == u.getName() && edge.secondEndpoint().getName() == v.getName()) ||
                    (edge.firstEndpoint().getName() == v.getName() && edge.secondEndpoint().getName() == u.getName())) {
                return true;
            }
        }

        // Otherwise returns false
        return false;
    }

    // Inserts an edge into the graph
    public void insertEdge(Node u, Node v, int edgeType, String label) throws GraphException {

        // If either node doesn't exist or if there is already an edge for these nodes, then we throw a graph exception
        if (nodeExists(u) == false || nodeExists(v) == false) {
            throw new GraphException("ERROR: One of the nodes doesn't exist.");
        }
        else if (edgeExists(u,v)==true){
            throw new GraphException("ERROR: The edge already exists for this set of nodes.");
        }

        // Otherwise, we add the edge to the graph
        else {

            Edge new_edge = new Edge(u,v,edgeType,label);

            // We add the edge to both linkedlist of edges of both nodes since each node needs to have the edge info
            LinkedList<Edge> linkedlist_edges_of_nodeu = this.E[u.getName()];
            LinkedList<Edge> linkedlist_edges_of_nodev = this.E[v.getName()];

            linkedlist_edges_of_nodeu.add(new_edge);
            linkedlist_edges_of_nodev.add(new_edge);

        }
    }

    public void insertEdge(Node u, Node v, int edgeType) throws GraphException{

        // If either node doesn't exist or if there is already an edge for these nodes, then we throw a graph exception
        if (nodeExists(u) == false || nodeExists(v) == false) {
            throw new GraphException("ERROR: One of the nodes doesn't exist.");
        }
        else if (edgeExists(u,v)==true){
            throw new GraphException("ERROR: The edge already exists for this set of nodes.");
        }

        // Otherwise, we add the edge to the graph
        else {

            Edge new_edge = new Edge(u,v,edgeType);

            // We add the edge to both linkedlist of edges of both nodes since each node needs to have the edge info
            LinkedList<Edge> linkedlist_edges_of_nodeu = E[u.getName()];
            LinkedList<Edge> linkedlist_edges_of_nodev = E[v.getName()];

            linkedlist_edges_of_nodeu.add(new_edge);
            linkedlist_edges_of_nodev.add(new_edge);

        }
    }

    public Node getNode(int name) throws GraphException {

        // Iterates through nodes in V and checks to see if any of their name is the same as the name of node u
        for (Node node : this.V) {
            if (node.getName() == name) {
                return node;
            }
        }
        throw new GraphException("ERROR: No node found with this name.");
    }

    public Iterator<Edge> incidentEdges(Node u) throws GraphException {

        // If node doesn't exist in the graph it throws a GraphException
        if (nodeExists(u) == false){
            throw new GraphException("ERROR: Node doesn't exist");
        }

        // Gets the linkedlist containing all the edges for the nodes
        LinkedList<Edge> linkedlist_of_edges_for_nodeu = this.E[u.getName()];

        // If the linked list is empty, then the node has no edges incident on it so we return null
        if (linkedlist_of_edges_for_nodeu.isEmpty()){
            return null;
        }
        // Otherwise, it has at least 1 edge and we return an iterator of the linked list
        else {
            return linkedlist_of_edges_for_nodeu.iterator();
        }

    }

    // Gets the Edge object between 2 nodes in the graph
    public Edge getEdge(Node u, Node v) throws GraphException {

        // If either node doesn't exist or if there is no edge for these nodes, then we throw a graph exception
        if (nodeExists(u) == false || nodeExists(v) == false) {
            throw new GraphException("ERROR: One of the nodes doesn't exist in the graph.");
        }

        // Gets all the edges for 1 of the nodes since either linkedlist should have their edge
        LinkedList<Edge> linkedlist = this.E[u.getName()];

        // Iterates through all the linked lists

        for (Edge edge : linkedlist) {

            // Checks to see if the (first endpoint is the same as u and the second endpoint is the same as v) or
            // (first endpoint is the same as v and the second endpoint is the same as u), in case their position are
            // swapped since an Edge (1,2) is still the same as Edge(2,1)

            if ((edge.firstEndpoint().getName() == u.getName() && edge.secondEndpoint().getName() == v.getName()) ||
                    (edge.firstEndpoint().getName() == v.getName() && edge.secondEndpoint().getName() == u.getName())) {
                return edge;
            }
        }

        // Throws a GraphException if no edge is found which is the case if the function doesn't return an edge in
        // the for loop. Therefore, we reach this line of code that we otherwise wouldn't have reached.
        throw new GraphException("ERROR: No edge exists for this set of nodes.");

    }

    public boolean areAdjacent(Node u, Node v) throws GraphException {

        // If either node doesn't exist then we throw a graph exception
        if (nodeExists(u) == false || nodeExists(v) == false) {
            throw new GraphException("ERROR: One of the nodes doesn't exist in the graph.");
        }

        // If an edge exists between these 2 nodes then they are adjacent and it returns true, otherwise, no edge
        // exists and the nodes aren;t adjacent so it returns false.
        return edgeExists(u,v);
    }

}
