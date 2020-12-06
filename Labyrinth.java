import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Labyrinth {

    int[] keys;
    char[][] labyrinth;
    Graph graph;
    int start;
    int exit;
    int CORRIDOR = -1;
    int WALL = -2;

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

            // Initialize graph, nuymber of nodes is length x width
            this.graph = new Graph(width * length);

            // Convert the line for the keys to an array of strings and subsequently to an array of integers.

            String[] array_str_keys = str_keys.split(" ");
            int[] array_int_keys = new int[array_str_keys.length];

            for (int i = 0; i < array_str_keys.length; i++) {
                array_int_keys[i] = Integer.parseInt(array_str_keys[i]);
            }

            // Store array of int keys in an instance variable
            this.keys = array_int_keys;

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
            this.setStartAndExitNodes();
            this.insertHorizontalEdges();
            this.insertVerticalEdges();
        }
    }

    private void setStartAndExitNodes(){
        // Find name of starting and ending nodes
        int name = 0;

        for (char[] a: this.labyrinth){
            for (char c: a){
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
            for (char c: this.labyrinth[i]){
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

            // Keep track of node names
            int name = i/2;

            // Analyzes every element in the column by iterating all the rows now
            for (int j =0; j<this.labyrinth.length; j=j+1) {

                char c = this.labyrinth[j][i];

                if (c=='s'){
                    name=name+5;
                }
                else if (c=='x'){
                    name=name+5;
                }
                else if (c=='i'){
                    name=name+5;
                }
                else if (c=='c'){

                    // the edge type for a corridor is -1
                    // The edge is between the current name for the node node and the next name for the node in the
                    // following row(which is name +4 -- 4 nodes forward gets us the node in the next row but in the
                    // same col as the current node)).

                    this.graph.insertEdge(this.graph.getNode(name-5),this.graph.getNode(name), this.CORRIDOR);

                }
                else if (Character.isDigit(c)){

                    // The edge is between the current name for the node node and the next name for the node in the
                    // following row(which is name +4 -- 4 nodes forward gets us the node in the next row but in the
                    // same col as the current node)).

                    this.graph.insertEdge(this.graph.getNode(name-5),this.graph.getNode(name), Integer.parseInt(String.valueOf(c)));
                }
            }
        }
    }










    public void printLabyrinth(){

        for(char[] a : this.labyrinth) {
            System.out.println(Arrays.toString(a));
        }
        System.out.println(this.start+" "+this.exit);
    }

    public void printGraph(){

        int count = 1;
        for (LinkedList<Edge> linkedList : this.graph.E){
            System.out.println("Linked List: "+count);
            for (Edge e: linkedList){

                System.out.println("("+e.firstEndpoint().getName()+", "+e.secondEndpoint().getName()+", "+e.getType()+")");
            }
            count++;
        }
    }

    public static void main(String[] args) throws IOException, LabyrinthException, GraphException {
        Labyrinth labyrinth = new Labyrinth("lab1");
        labyrinth.printLabyrinth();
        labyrinth.printGraph();

    }

}
