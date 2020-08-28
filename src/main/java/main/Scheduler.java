package main;

import JavaFX.Controller;
import algorithm.SolutionNode;
import algorithm.SolutionTree;
import input.InputReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import output.OutputGenerator;

import java.io.IOException;

public class Scheduler extends Application {
	private static String _outputName;
	private static int _numOfProcessor;
	private static SolutionNode _bestSolution = null;
	private static boolean openVisualization = false;
	private static SolutionTree solutionTree;

	public static void main(String[] args) {


		Scheduler scheduler = new Scheduler();
		scheduler.readUserInput(args);

		// read the input file and return it as a solutionTree object
		try {
			// the current branch and bound algorithm cannot handle more than 22 tasks, therefore,
			// all tasks are put into the same processor for milestone 1
			// @todo refactor the algorithm to increase its efficiency
			InputReader inputFile = new InputReader(args[0], _numOfProcessor);

			if(openVisualization){
				launch();
			}

			solutionTree = inputFile.readInputFile();

			// get the graphName from the input file
			String graphName = inputFile.getGraphName();

			SolutionNode bestSolution = solutionTree.findOptimalSolution();
			_bestSolution = bestSolution;

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
			_numOfProcessor  = Integer.parseInt(args[1]);
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

					openVisualization = true;

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

	public SolutionNode getBestSolution() {
		return _bestSolution;
	}

	@Override
	public void start(Stage primaryStage) {
//        Parent root = new Label("welcome");
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();

//		Controller controller = new Controller();
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(getClass().getResource("/JavaFX/Homepage.fxml"));
//		loader.setController(controller);
//		Parent root = null;
//		try {
//			root = loader.load();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {

		Controller controller = new Controller();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/JavaFX/Homepage.fxml"));
		loader.setController(controller);
		controller.setSolutionTree(solutionTree);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

//		try {
//			//root = FXMLLoader.load(getClass().getClassLoader().getResource("/Homepage.fxml"));
//			root = FXMLLoader.load(getClass().getResource("/JavaFX/Homepage.fxml"));
//
//		} catch (IOException e) {
//			throw new RuntimeException();
//		}



	}

	public static int get_numOfProcessor(){
		return _numOfProcessor;
	}

}
