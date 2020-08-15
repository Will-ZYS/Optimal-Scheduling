package se306.project1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OutputGenerator {
    private SolutionNode _bestSolution;
    private List<Processor> _processors;
    private String _outputName;
    private String _graphName;
    private LinkedHashMap<String, String> _outputRowsRaw;

    // Constructor to make the Output generator
    public OutputGenerator (SolutionNode bestSolution, String outputName, LinkedHashMap<String, String> inputRowsRaw, String graphName) throws IOException {
        _bestSolution = bestSolution;
        _outputName = outputName;
        _processors = _bestSolution.getProcessors();
        _outputRowsRaw = inputRowsRaw;
        if (!preCheckSolution()){
            System.err.println("Something went wrong, the solution wasn't valid");
        }
        preparingStringOutput();

        // the first letter of the graph name will be capitalized
        _graphName = "output" + graphName.substring(0,1).toUpperCase() + graphName.substring(1);
    }

    /**
     * This function validates is if the solution node is a valid solution
     * 1) checking if there are still any unvisited nodes
     * @return boolean true if the solution is valid
     */
    public Boolean preCheckSolution(){
        return _bestSolution.getUnvisitedTaskNodes().size() == 0;
    }

    /**
     * This function prepares the output strings to be written to the file
     */
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

    /**
     * This function writes the prepared strings to the file line by line
     */
    public void writeOutput() throws IOException {
        // Deleting the pre-existing file
        File file = new File(_outputName+".dot");
        if (file.exists()){ file.delete(); }

        // Writing to the new file
        FileWriter myWriter = new FileWriter(_outputName+".dot");
        myWriter.write(String.format("digraph \""+ _graphName +"\" {"+"%n"));

        for (Map.Entry<String, String> entry : _outputRowsRaw.entrySet()) {
            // Formatted to align the columns
            myWriter.write(String.format("\t%-15s%-15s%n", entry.getKey(), entry.getValue()));
        }
        myWriter.write("}");
        myWriter.close();

    }

}
