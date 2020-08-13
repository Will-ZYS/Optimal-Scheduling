package se306.project1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, SOFTENG306 project 1!");

        // Check the number of command line argument is greater than 2
        if (args.length < 2) {
            System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [OPTION]");
            System.exit(1);
        }

        // check if the first argument is a valid path to a .dot file
        int args0Length = args[0].length();
        if (args0Length < 4 || !args[0].substring(args0Length - 4).equals(".dot")) {
            System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [OPTION]");
            System.exit(1);
        }

        // check if the second argument is a positive integer
        boolean isPositiveInt = false;
        int numOfProcessor = 0;
        try {
            numOfProcessor = Integer.parseInt(args[1]);
            isPositiveInt = numOfProcessor > 0;
        } catch (NumberFormatException e) {

        }
        if (!isPositiveInt) {
            System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [OPTION]");
            System.exit(1);
        }

        // loop through option and check stuffs
        String outputName = "output";
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("-v")) {
                System.out.println("Sorry, the visualiser has not been implemented yet");
            } else if (args[i].equals("-p")) {
                System.out.println("Sorry, the parallel version has not been implemented yet");
                i++;
            } else if (args[i].equals("-o")) {
                if (i == args.length - 1) {
                    System.err.println("Usage: java -jar scheduler.jar INPUT.dot P [OPTION]");
                    System.exit(1);
                }
                outputName = args[i + 1];
                i++;
            }
        }

        // create input reader...
        try {
            InputReader inputFile = new InputReader(args[0], numOfProcessor);
            SolutionTree solutionTree = inputFile.readInputFile();

            SolutionNode bestSolution = solutionTree.findOptimalSolution();
            SolutionNode.printSolutionNode(bestSolution);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
