package se306.project1;

import java.io.*;
import java.util.*;

public class OutputGenerator {
    private SolutionNode _bestSolution;
    private List<Processor> _processors;
    private String _outputName;
    private String[] _outputStrings;
    private LinkedHashMap<String, String> _inputRowsRaw;


    public OutputGenerator (SolutionNode bestSolution, String outputName, LinkedHashMap<String, String> inputRowsRaw){
        _bestSolution = bestSolution;
        _outputName = outputName;
        _processors = _bestSolution.getProcessors();
        _inputRowsRaw = inputRowsRaw;
        if (!preCheckSolution()){
            System.err.println("Something went wrong, the solution wasn't valid");
        }
        preparingStringOutput();
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
        Map<TaskNode, Integer> tasks;
        for (Processor processor : _processors){
            tasks = processor.getTasks();
            for (Map.Entry<TaskNode, Integer> entry : tasks.entrySet()) {
                Integer startTime = entry.getValue();
                TaskNode task = entry.getKey();
                System.out.println(startTime);
                System.out.println(task.getName());
            }

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
