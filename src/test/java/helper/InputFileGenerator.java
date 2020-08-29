package helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class automatically generate a valid input file for testing purpose
 */
public class InputFileGenerator {
	public static void main(String[] args) throws IOException {

		int numOfTasks = 5; // max 26
		int weightLimit = 4;
		String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

		Random rand = new Random(); //instance of random class

		List<String> inputs = new ArrayList<>(Arrays.asList(alphabet).subList(0, numOfTasks));

		for (int i = 0; i < numOfTasks; i++) {
			for (int j = i + 1; j < numOfTasks; j++) {
				if (rand.nextInt(5) == 1) {
					inputs.add(alphabet[i] + " -> " + alphabet[j]);
				}
			}
		}

		File file = new File("INPUT0.dot");
		if (file.exists()) {
			if (file.delete()) {
				System.out.println("Old Input File Deleted");
			}
		}

		// Writing to the new file
		FileWriter myWriter = new FileWriter("INPUT0.dot");
		myWriter.write(String.format("digraph \"" + "autogenerated" + "\" {" + "%n"));

		for (String input : inputs) {
			// Formatted to align the columns
			myWriter.write(String.format("\t%-9s%-9s%n", input, "[Weight=" + (rand.nextInt(weightLimit) + 1) + "];"));
		}
		myWriter.write("}");
		myWriter.close();

	}
}
