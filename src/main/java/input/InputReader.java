package input;

import algorithm.DataTransferEdge;
import algorithm.Processor;
import algorithm.SolutionTree;
import algorithm.TaskNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputReader {

    private final String _pathToDotFile;
    private final int _numOfProcessor;
    private String _graphName = "exampleGraph";

    private final LinkedHashMap<String, String> _inputRowsRaw = new LinkedHashMap<>();

    public InputReader ( String path, int processors ){
        _pathToDotFile = path;
        _numOfProcessor = processors;
    }

    public SolutionTree readInputFile() throws IOException {

        // read the file with provided path
        FileReader dotFile = new FileReader( _pathToDotFile );
        BufferedReader brFile = new BufferedReader(dotFile);

        Map<String, TaskNode> taskNodeMap = new HashMap<>(); // map between task name and its TaskNode object

        // read the file line by line and get desired information from each line
        String line;
        while ((line = brFile.readLine()) != null) {

            // ignore the line without the symbol '['
            if (!line.contains("[")) {
                // if the line is the first line which contains the graph name
                if (line.contains("\"")) {
                    Pattern doubleQuotes = Pattern.compile("\"([^\"]*)\"");
                    Matcher findDoubleQuotes = doubleQuotes.matcher(line);
                    while (findDoubleQuotes.find()) {
                        _graphName = findDoubleQuotes.group(1);
                    }
                }
                continue;
            }

            // get the weight and task node name from lines
            String[] lineArray = line.split("\\[");
            String attributeInfo = lineArray[0].trim();
            String weightInfo = lineArray[1].trim().split("=")[1];
            int weight = Integer.parseInt(weightInfo.substring(0, weightInfo.length()-2));

            if (attributeInfo.contains("->")) { // process as edge
                // Store as an edge in LinkedMap (preserved order)
                _inputRowsRaw.put(attributeInfo, "["+lineArray[1]);

                String[] nodeInfo = attributeInfo.split("->");
                String sourceName = nodeInfo[0].trim();
                String destinationName = nodeInfo[1].trim();

                // get or create the source and destination node
                TaskNode sourceNode;
                TaskNode destinationNode;
                if (taskNodeMap.containsKey(sourceName) && taskNodeMap.containsKey(destinationName)) {
                    sourceNode = taskNodeMap.get(sourceName);
                    destinationNode = taskNodeMap.get(destinationName);
                } else if (taskNodeMap.containsKey(sourceName)) {
                    sourceNode = taskNodeMap.get(sourceName);
                    destinationNode = new TaskNode(destinationName);
                    taskNodeMap.put(destinationName, destinationNode);
                } else if (taskNodeMap.containsKey(destinationName)) {
                    sourceNode = new TaskNode(sourceName);
                    taskNodeMap.put(sourceName, sourceNode);
                    destinationNode = taskNodeMap.get(destinationName);
                } else {
                    sourceNode = new TaskNode(sourceName);
                    destinationNode = new TaskNode(destinationName);
                    taskNodeMap.put(sourceName, sourceNode);
                    taskNodeMap.put(destinationName,destinationNode);
                }

                // create the DataTransferEdge and write to adjacency list
                DataTransferEdge edge = new DataTransferEdge(sourceNode, weight);
                destinationNode.addIncomingEdge(edge);

            } else { // process as node
                // Store as an node in LinkedMap (preserved order)
                String nodeData = lineArray[1].trim().replace("];","");
                _inputRowsRaw.put(attributeInfo,nodeData);

                if (!taskNodeMap.containsKey(attributeInfo)) {
                    TaskNode node = new TaskNode(weight, attributeInfo);
                    taskNodeMap.put(attributeInfo, node);
                } else {
                    taskNodeMap.get(attributeInfo).setWeight(weight);
                }
            }

        }
        dotFile.close();

        // list of tasks
        List<TaskNode> taskList = new ArrayList<>(taskNodeMap.values());

        // new a solution tree object which will be used later
        return new SolutionTree(taskList, generateProcessors());
    }

    // generate a list of processors, the id starts with 1
    private List<Processor> generateProcessors() {

        List<Processor> processorList = new ArrayList<>();

        for ( int i = 1; i<= _numOfProcessor; i++ ){
            processorList.add(new Processor(i));
        }

        return processorList;
    }

    public LinkedHashMap<String, String> getInputRowsRaw() {
        return _inputRowsRaw;
    }

    public String getGraphName() {
        return _graphName;
    }

}
