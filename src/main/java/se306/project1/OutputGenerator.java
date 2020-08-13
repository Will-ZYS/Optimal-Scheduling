package se306.project1;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputGenerator {
    private SolutionNode _bestSolution;
    private List<Processor> _processors;
    private String _outputName;
    private String[] _outputStrings;

    public OutputGenerator (SolutionNode bestSolution, String outputName){
        _bestSolution = bestSolution;
        _outputName = outputName;
        _processors = _bestSolution.getProcessors();
        if (!preCheckSolution()){
            System.err.println("Something went wrong, the solution wasn't valid");
        }
    }

    public Boolean preCheckSolution(){
        if (_bestSolution.get_unvisitedTaskNodes().size() != 0 ) {
            return false;
        } else if (false){
            return false;
        }
        return true;
    }

    private void preparingStringOutput(){
        for (Processor processor : _processors){
            processor.getTasks();
        }
    }

    private void writeOutput() throws IOException {
        // Deleting the pre-existing file
        File file = new File(_outputName+".dot");
        if (file.exists()){ file.delete(); }

        // Writing to the new file
        FileWriter myWriter = new FileWriter(_outputName+".dot");

        // Writing the prepared strings to the new file
        for (String line: _outputStrings){
            myWriter.write(String.format(line+"%n"));
        }
        myWriter.close();
    }
}
