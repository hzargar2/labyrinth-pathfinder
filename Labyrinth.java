import java.io.*;
import java.util.*;

public class Labyrinth {

    private ArrayList<Integer> keys = new ArrayList<>();
    private char[][] labyrinth;
    private Graph graph;
    private int start;
    private int exit;
    private int CORRIDOR = -1;
    private Stack<Node> stack = new Stack<>();
    private int width;
    private int length;

    public Labyrinth(String inputFile) throws IOException, LabyrinthException, NumberFormatException, GraphException {

        File file = new File(inputFile);

        // If the file is empty we throw a LabyrinthException
        if (file.length() == 0) {
            throw new LabyrinthException("ERROR: File is empty.");
        } else {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));

            // won't use this value as its just a scaling value, we read the line so we can go the next line that we want.
            String str_scale_value = bufferedReader.readLine();

            // Get the string representations of the lines
            String str_width = bufferedReader.readLine();
            String str_length = bufferedReader.readLine();
            String str_keys = bufferedReader.readLine();

            // Convert the strings to ints
            int width = Integer.parseInt(str_width);
            int length = Integer.parseInt(str_length);

            this.width = width;
            this.length=length;

            // Initialize graph, nuymber of nodes is length x width
            this.graph = new Graph(width * length);

            // Convert the line for the keys to an array of strings and subsequently to an array of integers.

            String[] array_str_keys = str_keys.split(" ");
            ArrayList<Integer> array_int_keys = new ArrayList<Integer>();


            for (int i = 0; i < array_str_keys.length; i++) {
                array_int_keys.add(Integer.parseInt(array_str_keys[i]));
            }

            // Convert the array_int_keys to an array of all the individual keys. For example, 1 0 0 2 0, would
            // break down to 1 key of type 0 in th array, and 2 keys of type 3 in the array.

            for (int i = 0; i< array_int_keys.toArray().length; i++){
                if (array_int_keys.get(i) != 0){
                    for (int j =0; j<array_int_keys.get(i); j++){
                        this.keys.add(i);
                    }
                }
            }

            Collections.sort(this.keys);

            // Initialize a char[][] array with the right size, multiply each dimension by 2 and subtract by 1 because there is an edge
            // between each node that is also stored in a char in char[][] except the last node in each row and column

            this.labyrinth = new char[2 * length - 1][2 * width - 1];


            // Get the rest of the lines in the file and insert them in the char[][] labyrinth
            String line = bufferedReader.readLine();

            int line_index = 0;

            while (line != null) {

                // Checks to see if the given labyrinth can fit in the our char[][]. Otherwise, throws LabyrinthException
                if (line_index >= 2 * length - 1) {
                    throw new LabyrinthException("ERROR: Incorrect file format. Too many rows in labyrinth");
                }

                // Process each char in the line at add it to the appropriate place in the  char[][]
                for (int i = 0; i < line.length(); i++) {

                    // Checks to see if the given labyrinth can fit in the our char[][]. Otherwise, throws LabyrinthException
                    if (i >= 2 * width - 1) {
                        throw new LabyrinthException("ERROR: Incorrect file format. Too many columns in labyrinth");
                    }

                    // Adds the char in the string to the char[][] labyrinth
                    char c = line.charAt(i);
                    this.labyrinth[line_index][i] = c;
                }

                // Increment line_count
                line_index++;
                // Go to the next line
                line = bufferedReader.readLine();

            }

            // Finds the start adn exit nodes and inserts all the edges into the graph
            this.setStartAndExitNodes();
            this.insertHorizontalEdges();
            this.insertVerticalEdges();
        }
    }

    // Find name of starting and ending nodes
    private void setStartAndExitNodes(){

        // Set counter for the node name
        int name = 0;

        // Iterate through all char in the each row
        for (char[] a: this.labyrinth){
            for (char c: a){

                // If we come across a room, we increment the anme by 1. If we come across an exit or start node, we save
                // the node name in an instance variable
                if (c=='s'){
                    this.start = name;
                    name++;
                }
                else if (c=='x'){
                    this.exit = name;
                    name++;
                }
                else if (c=='i'){
                    name++;
                }
            }
        }
    }

    // Insert all the horizontal edges into the graph
    private void insertHorizontalEdges() throws GraphException {

        // Keep track of node names
        int name = 0;

        // Gets every second row because only finding horizontal edges
        for (int i = 0; i<this.labyrinth.length; i=i+2){

            // Iterates through all the char in that row

            for (char c: this.labyrinth[i]){

                // If the char is a room, we increment the name by 1
                if (c=='s'){
                    name++;
                }
                else if (c=='x'){
                    name++;
                }
                else if (c=='i'){
                    name++;
                }
                else if (c=='c'){

                    // the edge type for a corridor is -1
                    // The edge is between the current name for the node node and the next name for the node (which is name +1).

                    this.graph.insertEdge(this.graph.getNode(name-1),this.graph.getNode(name), this.CORRIDOR);

                }
                else if (Character.isDigit(c)){

                    // The edge is between the current name for the node node and the next name for the node (which is name +1).

                     this.graph.insertEdge(this.graph.getNode(name-1),this.graph.getNode(name), Integer.parseInt(String.valueOf(c)));
                }
            }
        }
    }

    // Insert all the vertical edges into the graph
    private void insertVerticalEdges() throws GraphException {

        // Gets every second col because only finding vertical edges
        for (int i = 0; i<this.labyrinth[0].length; i=i+2){

            // Keep track of node names, which is the i/2 since there every odd i is not a room but the type of edge
            int name = i/2;

            // Analyzes every element in the column by iterating all the rows now
            for (int j =0; j<this.labyrinth.length; j=j+1) {

                char c = this.labyrinth[j][i];

                // If char is a room, we increment name by the number of elements in a row because we want the node
                // in the next row but same column as the current node

                if (c=='s'){
                    name=name+this.width;
                }
                else if (c=='x'){
                    name=name+this.width;
                }
                else if (c=='i'){
                    name=name+this.width;
                }
                else if (c=='c'){

                    // the edge type for a corridor is -1
                    // The edge is between the current name for the node node and the next name for the node in the
                    // following row(which is name +4 -- 4 nodes forward gets us the node in the next row but in the
                    // same col as the current node)).

                    this.graph.insertEdge(this.graph.getNode(name-this.width),this.graph.getNode(name), this.CORRIDOR);

                }
                else if (Character.isDigit(c)){

                    // The edge is between the current name for the node node and the next name for the node in the
                    // following row(which is name +4 -- 4 nodes forward gets us the node in the next row but in the
                    // same col as the current node)).

                    this.graph.insertEdge(this.graph.getNode(name-this.width),this.graph.getNode(name), Integer.parseInt(String.valueOf(c)));
                }
            }
        }
    }

    // Gets the graph for the labyrinth
    public Graph getGraph() throws GraphException{

        // If at least 1 node exists in the graph (i.e. graph is not empty), it will have a name =0. If the graph is empty
        // then node with name 0 won't exist, and Graph.getNode() with throw a GraphException as required.

        Node node = this.graph.getNode(0);

        // Successfully found at least 1 node in the graph so we return the Graph object

        return this.graph;
    }

    private boolean DFS(Node node) throws GraphException {

        // Mark the node and push it onto the stack
        node.setMark(true);
        stack.push(node);


        // Base case: if current node is the exit node then the path has been found
        if (node.getName() == this.exit){
            return true;
        }

        else {

            // Gets Iterator containing all the Edge objects for this Node object
            Iterator<Edge> iterator = this.graph.incidentEdges(node);

            // Iterate through all the edges for this Node
            while (iterator.hasNext()){

                Edge edge = iterator.next();

                Node s = edge.firstEndpoint();
                Node d = edge.secondEndpoint();

                // Makes sure we get the node at the endpoint that is not the current node. i.e node at other side of edge.
                if (node.getName() != s.getName()){

                    // If the node isn't marked
                    if (s.getMark() == false) {

                        // Default minimum key value found
                        int min_key = -1;

                        // If edge needs a key and we still have keys in the array
                        if (edge.getType() >= 0 && this.keys.toArray().length != 0){

                            // Find the smallest key larger than or equal to the edge type to use. Array is sorted so this is the first
                            // match we encounter

                            for (int k: keys){

                                if (k>=edge.getType() && edge.getType()!=-1){
                                    min_key = k;
                                    break;
                                }

                            }
                        }

                        // If no key was found (min_keydefault stays at -1) but the edge requires a key, we skip this
                        // edge. No recursive call for it because we can't traverse it
                        if (min_key==-1 && edge.getType()>=0){
                            continue;
                        }

                        // Otherwise, we remove the key from our set of available keys as its considered "used"
                        // and can't be used again.
                        else {
                            if (min_key!=-1){
                                this.keys.remove((Integer) min_key);
                            }
                        }

                        // Make recursive call to DFS since the continue condition above wasn't met
                        if (DFS(s) == true) {
                            return true;
                        }
                        // If DFS returns false, then no path found from this node so we unmark the node in case
                        // another valid path exists with this node being part of it so we can traverse it
                        // and we also add the key we used back to the list of possible keys so we can use it again in
                        // a new path.
                        else {
                            s.setMark(false);
                            if (min_key!=-1){
                                this.keys.add(min_key);
                                Collections.sort(this.keys);
                            }
                        }
                    }
                }

                else {
                    // Traverses the node that is different from the current node. If the node isn't marked.
                    if (d.getMark()==false) {

                        // Default minimum key value found
                        int min_key = -1;

                        // If edge needs a key and we still have keys in the array
                        if (edge.getType() >= 0 && this.keys.toArray().length != 0){

                            // Find the smallest key larger than or equal to the edge type to use. Array is sorted so this is the first
                            // match we encounter

                            for (int k: keys){

                                if (k>=edge.getType() && edge.getType()!=-1){
                                    min_key = k;
                                    break;
                                }

                            }
                        }

                        // If no key was found (min_keydefault stays at -1) but the edge requires a key, we skip this
                        // edge. No recursive call for it because we can't traverse it
                        if (min_key==-1 && edge.getType()>=0){
                            continue;
                        }

                        // Otherwise, we remove the key from our set of available keys as its considered "used"
                        // and can't be used again.
                        else {
                            if (min_key!=-1){
                                this.keys.remove((Integer) min_key);
                            }
                        }

                        // Make recursive call to DFS since the continue condition above wasn't met
                        if (DFS(d) == true) {
                            return true;
                        }
                        // If DFS returns false, then no path found from this node so we unmark the node in case
                        // another valid path exists with this node being part of it so we can traverse it
                        // and we also add the key we used back to the list of possible keys so we can use it again in
                        // a new path.
                        else {
                            d.setMark(false);
                            if (min_key!=-1){
                                this.keys.add(min_key);
                                Collections.sort(this.keys);
                            }
                        }
                    }
                }
            }

            // No path found from this node we remove it from the stack
            this.stack.pop();

            // Return false if no path found from this node.
            return false;
        }
    }

    // Returns Iterator object containing all the nodes for the path found if a path exists. Otherwise, returns null.
    public Iterator<Node> solve() throws GraphException {

        // Checks to see if graph has anything in it, otherwise, it throws an exception
        Graph graph = this.getGraph();

        if (DFS(graph.getNode(this.start)) == true){
            return this.stack.iterator();
        }
        else {
            return null;
        }
    }
}
