package se306.project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InputReader {

    private String _pathToDotFile;
    private int _numOfProcessor;

    private LinkedHashMap<String, String> _inputRowsRaw = new LinkedHashMap<String, String>();

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
                DataTransferEdge edge = new DataTransferEdge(sourceNode, destinationNode, weight, attributeInfo);
                sourceNode.addOutgoingEdge(edge);
                destinationNode.addIncomingEdge(edge);

            } else { // process as node
                // Store as an node in LinkedMap (preserved order)
                String nodeData = lineArray[1].replace("];","");
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
        SolutionTree solutionTree = new SolutionTree(taskList, generateProcessors());
        return solutionTree;
    }

    // generate a list of processors, the id starts with 1
    private List<Processor> generateProcessors() {

        List<Processor> processorList = new ArrayList<Processor>();

        for ( int i = 1; i<= _numOfProcessor; i++ ){
            processorList.add(new Processor(i));
        }

        return processorList;
    }

    public LinkedHashMap<String, String> getInputRowsRaw() {
        return _inputRowsRaw;
    }

}
