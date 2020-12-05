import java.io.*;
import java.util.Arrays;

public class Labyrinth {

    int[] keys;
    char[][] labyrinth;
    Graph graph;
    int start;
    int exit;

    public Labyrinth(String inputFile) throws IOException, LabyrinthException, NumberFormatException {

        File file = new File(inputFile);

        // If the file is empty we throw a LabyrinthException
        if (file.length() == 0){
            throw new LabyrinthException("ERROR: File is empty.");
        }
        else{

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
            this.graph = new Graph(width*length);

            // Convert the line for the keys to an array of strings and subsequently to an array of integers.

            String[] array_str_keys = str_keys.split(" ");
            int[] array_int_keys = new int[array_str_keys.length];

            for (int i =0; i<array_str_keys.length; i++){
                array_int_keys[i] = Integer.parseInt(array_str_keys[i]);
            }

            // Store array of int keys in an instance variable
            this.keys = array_int_keys;

            // Initialize a char[][] array with the right size, multiply each dimension by 2 and subtract by 1 because there is an edge
            // between each node that is also stored in a char in char[][] except the last node in each row and column

            this.labyrinth = new char[2*length-1][2*width-1];


            // Get the rest of the lines in the file and insert them in the char[][] labyrinth
            String line = bufferedReader.readLine();

            int line_index = 0;

            while (line != null){

                // Checks to see if the given labyrinth can fit in the our char[][]. Otherwise, throws LabyrinthException
                if (line_index >= 2*length-1){
                    throw new LabyrinthException("ERROR: Incorrect file format. Too many rows in labyrinth");
                }

                // Process each char in the line at add it to the appropriate place in the  char[][]
                for (int i = 0; i < line.length(); i++){

                    // Checks to see if the given labyrinth can fit in the our char[][]. Otherwise, throws LabyrinthException
                    if (i >= 2*width-1){
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

            /// DO REST FROM HERE

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
    }

    public void printLabyrinth(){

        for(char[] a : this.labyrinth) {
            System.out.println(Arrays.toString(a));
        }
        System.out.println(this.start+" "+this.exit);
    }

    public static void main(String[] args) throws IOException, LabyrinthException {
        Labyrinth labyrinth = new Labyrinth("lab1");
        labyrinth.printLabyrinth();

    }

}
