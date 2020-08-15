package se306.project1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InputFileGenerator {
    public static void main(String[] args) throws IOException {


        int numOfTasks = 10; // max 26
        int weightLimit = 4;
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

        Random rand = new Random(); //instance of random class

        List<String> inputs = new ArrayList<>(Arrays.asList(alphabet));

        for (int i = 0; i < numOfTasks; i++){
            for (int j = i + 1; j < numOfTasks; j++){
                if (rand.nextInt(5) == 1) {
                    inputs.add(alphabet[i] + " -> " + alphabet[j]);
                }
            }
        }

        File file = new File("INPUT0.dot");
        if (file.exists()){ file.delete(); }

        // Writing to the new file
        FileWriter myWriter = new FileWriter("INPUT0.dot");
        myWriter.write(String.format("digraph \""+ "autogenerated" +"\" {"+"%n"));

        for (String input : inputs) {
            // Formatted to align the columns
            myWriter.write(String.format("\t%-15s%-15s%n", input, "[Weight=" + (rand.nextInt(weightLimit) + 1) + "];"));
        }
        myWriter.write("}");
        myWriter.close();

    }
}