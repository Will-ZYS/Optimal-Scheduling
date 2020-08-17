package main;

import algorithm.SolutionNode;
import algorithm.SolutionTree;
import input.InputReader;
import output.OutputGenerator;

import java.io.IOException;

public class Scheduler {
	private static int _numOfProcessor = 0;
	private static String _outputName;

	public static void main(String[] args) {

		Scheduler scheduler = new Scheduler();
		scheduler.readUserInput(args);

		try {
			// read the input file and return it as a solutionTree object
			InputReader inputFile = new InputReader(args[0], _numOfProcessor);

			SolutionTree solutionTree = inputFile.readInputFile();

			// get the graphName from the input file
			String graphName = inputFile.getGraphName();

			SolutionNode bestSolution = solutionTree.findOptimalSolution();

			// Generating output
			OutputGenerator outputGenerator = new OutputGenerator(bestSolution, _outputName,
					                                              inputFile.getInputRowsRaw(), graphName);
			outputGenerator.writeOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function processes user's command inputs
	 *
	 * @param args user inputs
	 */
	private void readUserInput(String[] args) {
		// Check the number of command line argument is greater than 2
		if (args.length < 2) {
			System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [-p N] [-v] [-o OUTPUT]");
			System.exit(1);
		}

		// check if the first argument is a valid path to a .dot file
		int args0Length = args[0].length();
		if (args0Length < 4 || ! args[0].substring(args0Length - 4).equals(".dot")) {
			System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [-p N] [-v] [-o OUTPUT]");
			System.err.println("INPUT.dot needs to be a valid path to the input file with .dot extension");
			System.exit(1);
		}
		String inputName = args[0].substring(0, args0Length - 4);

		// check if the second argument is a positive integer
		boolean isPositiveInt = false;
		try {
			_numOfProcessor = Integer.parseInt(args[1]);
			isPositiveInt = _numOfProcessor > 0;
		} catch (NumberFormatException ignored) {

		}
		if (! isPositiveInt) {
			System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [-p N] [-v] [-o OUTPUT]");
			System.err.println("The number of processors, P, needs to be a positive integer");
			System.exit(1);
		}

		// loop through option and check stuffs
		_outputName = inputName + "-output";
		for (int i = 2; i < args.length; i++) {
			switch (args[i]) {
				case "-v":
					System.out.println("Sorry, the visualiser has not been implemented yet, you can find the " +
                                       "result in the output file");
					break;
				case "-p":
					System.out.println("Sorry, the parallel version has not been implemented yet, " +
							           "the algorithm will be run in sequential");
					i++;
					break;
				case "-o":
					if (i == args.length - 1) {
						System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [-p N] [-v] [-o OUTPUT]");
						System.err.println("You need to specify the output file name");
						System.exit(1);
					}
					_outputName = args[i + 1];
					i++;
					break;
			}
		}

	}
}
