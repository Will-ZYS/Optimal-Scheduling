package se306.project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputReader {

    private String pathToDotFile;

    public InputReader ( String path ){
        pathToDotFile = path;
    }

    public void readInputFile() throws IOException {

        // read the file with provided path
        FileReader dotFile = new FileReader(pathToDotFile);
        BufferedReader brFile = new BufferedReader(dotFile);

        Map<String, TaskNode> taskNodeMap = new HashMap<>(); // map between task name and its TaskNode object
        Map<TaskNode, List<DataTransferEdge>> adjacencyList = new HashMap<>(); // adjacency list

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

            if (attributeInfo.contains("->")) {
                // process as edge
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
                } else if (taskNodeMap.containsKey(destinationName)) {
                    sourceNode = new TaskNode(sourceName);
                    destinationNode = taskNodeMap.get(destinationName);
                } else {
                    sourceNode = new TaskNode(sourceName);
                    destinationNode = new TaskNode(destinationName);
                }

                // create the DataTransferEdge and write to adjacency list
                DataTransferEdge edge = new DataTransferEdge(sourceNode, destinationNode, weight);
                if (adjacencyList.containsKey(sourceNode)) {
                    adjacencyList.get(sourceNode).add(edge);
                } else {
                    List<DataTransferEdge> newList = new ArrayList<>();
                    newList.add(edge);
                    adjacencyList.put(sourceNode, newList);
                }

            } else {
                // process as node
                if (!taskNodeMap.containsKey(attributeInfo)) {
                    TaskNode node = new TaskNode(weight, attributeInfo);
                    taskNodeMap.put(attributeInfo, node);
                    adjacencyList.put(node, new ArrayList<>());
                } else {
                    taskNodeMap.get(attributeInfo).setWeight(weight);
                }
            }

        }

        dotFile.close();

        List<TaskNode> taskList = new ArrayList<>(taskNodeMap.values()); // list of tasks

    }
}
