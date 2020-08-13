package se306.project1;

import java.io.*;
import java.util.*;

public class OutputGenerator {
    private SolutionNode _bestSolution;
    private List<Processor> _processors;
    private String _outputName;
    private String[] _outputStrings;
    private LinkedHashMap<String, String> _outputRowsRaw;


    public OutputGenerator (SolutionNode bestSolution, String outputName, LinkedHashMap<String, String> inputRowsRaw) throws IOException {
        _bestSolution = bestSolution;
        _outputName = outputName;
        _processors = _bestSolution.getProcessors();
        _outputRowsRaw = inputRowsRaw;
        if (!preCheckSolution()){
            System.err.println("Something went wrong, the solution wasn't valid");
        }
        preparingStringOutput();
        writeOutput();
    }

    public Boolean preCheckSolution(){
        if (_bestSolution.getUnvisitedTaskNodes().size() != 0 ) {
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
                String data = "["+_outputRowsRaw.get(task.getName())+",Start="+startTime+",Processor="+ processor.getID()+"];";
                _outputRowsRaw.replace(task.getName(), data);
            }

        }
    }

    private void writeOutput() throws IOException {
        // Deleting the pre-existing file
        File file = new File(_outputName+".dot");
        if (file.exists()){ file.delete(); }

        // Writing to the new file
        FileWriter myWriter = new FileWriter(_outputName+".dot");
        myWriter.write(String.format("digraph \""+_outputName+"\" {"+"%n"));

        for (Map.Entry<String, String> entry : _outputRowsRaw.entrySet()) {
            myWriter.write(String.format("\t%-15s%-15s%n", entry.getKey(), entry.getValue()));
        }
        myWriter.write("}");
        myWriter.close();

    }

}
