package input;

import algorithm.DataTransferEdge;
import algorithm.Processor;
import algorithm.SolutionTree;
import algorithm.TaskNode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputReader {

	private final String PATH_TO_DOT_FILE;
	private final int NUM_OF_PROCESSOR;
	private String _graphName = "exampleGraph";

	// storing the order of lines of the input file into a linked hash map for output
	private final LinkedHashMap<String, String> INPUT_ROWS_RAW = new LinkedHashMap<>();

	public InputReader(String path, int processors) {
		PATH_TO_DOT_FILE = path;
		NUM_OF_PROCESSOR = processors;
	}

	/**
	 * Reading the input .dot file
	 *
	 * @return a SolutionTree object that contains a list of processors and tasks
	 * @throws IOException when the given input file path is invalid
	 */
	public SolutionTree readInputFile() throws IOException {

		// read the file with provided path
		FileReader dotFile = null;
		try {
			dotFile = new FileReader(PATH_TO_DOT_FILE);
		} catch (FileNotFoundException fnfe) {
			System.err.println("Error: Cannot find the file " + PATH_TO_DOT_FILE + ", please enter the correct file name");
			System.exit(1);
		}

		BufferedReader brFile = new BufferedReader(dotFile);

		// map between task name and its TaskNode object
		Map<String, TaskNode> taskNodeMap = new HashMap<>();

		// read the file line by line and get desired information from each line
		String line;
		while ((line = brFile.readLine()) != null) {

			// if the line is the first line which contains the graph name
			if (line.contains("\"") && line.contains("digraph")) {
				Pattern doubleQuotes = Pattern.compile("\"([^\"]*)\"");
				Matcher findDoubleQuotes = doubleQuotes.matcher(line);
				while (findDoubleQuotes.find()) {
					_graphName = findDoubleQuotes.group(1);
				}
				continue;
			}

			// check if the line contains actual information of a node or a edge
			String nodePattern = "\\s*\\w+\\s*\\[Weight=\\d+\\];";
			String edgePattern = "\\s*\\w+\\s->\\s\\w+\\s*\\[Weight=\\d+\\];";
			if (!line.matches(nodePattern) && !line.matches(edgePattern)) {
				continue;
			}

			// get the weight and task node name from lines
			String[] lineArray = line.split("\\[");
			String attributeInfo = lineArray[0].trim();
			String weightInfo = lineArray[1].trim().split("=")[1];
			int weight = Integer.parseInt(weightInfo.substring(0, weightInfo.length() - 2));

			if (attributeInfo.contains("->")) { // process as edge
				// Store as an edge in LinkedMap (preserved order)
				INPUT_ROWS_RAW.put(attributeInfo, "[" + lineArray[1]);

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
					taskNodeMap.put(destinationName, destinationNode);
				}

				// create the DataTransferEdge and write to adjacency list
				DataTransferEdge incomingEdge = new DataTransferEdge(sourceNode, weight);
				destinationNode.addIncomingEdge(incomingEdge);

				DataTransferEdge outgoingEdge = new DataTransferEdge(destinationNode, weight);
				sourceNode.addOutgoingEdge(outgoingEdge);

			} else {
				// process the current line as a task node
				// Store as an node in LinkedMap (preserved order)
				String nodeData = lineArray[1].trim().replace("];", "");
				INPUT_ROWS_RAW.put(attributeInfo, nodeData);

				if (! taskNodeMap.containsKey(attributeInfo)) {
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

		// sort all tasks in topological order
		taskList = topologicalSort(taskList);

		// get all tasks without incoming edges
		for (TaskNode taskNode : taskList) {
			if (taskNode.getIncomingEdges().isEmpty()) {
				calculateBottomLevel(taskNode);
			} else {
				break;
			}
		}

		// new a solution tree object which will be used later
		return new SolutionTree(taskList, generateProcessors());
	}

	/**
	 * Sort all taskNodes into its topological order
	 * @param taskNodes original list of taskNodes
	 * @return a list of sorted taskNodes
	 */
	private List<TaskNode> topologicalSort(List<TaskNode> taskNodes) {
		int totalNumberOfTasks = taskNodes.size();
		List<TaskNode> taskListInTopologicalOrder = new ArrayList<>();
		Map<TaskNode, Integer> tasks = new HashMap<>(); // Integer represents the number of incoming edges of a task

		for (TaskNode taskNode : taskNodes) {
			if (taskNode.getIncomingEdges().isEmpty()) {
				taskListInTopologicalOrder.add(taskNode);
			} else {
				tasks.put(taskNode, taskNode.getIncomingEdges().size());
			}
		}

		for (int i = 0; i < totalNumberOfTasks; i++) {
			TaskNode taskNode = taskListInTopologicalOrder.get(i);

			List<DataTransferEdge> outgoingEdges = taskNode.getOutgoingEdges();
			for (DataTransferEdge outgoingEdge : outgoingEdges) {
				// find all its child taskNodes
				TaskNode childTaskNode = outgoingEdge.getSourceNode();
				int numberOfIncomingEdges = tasks.get(childTaskNode) - 1;

				if (numberOfIncomingEdges == 0) {
					taskListInTopologicalOrder.add(childTaskNode);
					tasks.remove(childTaskNode);
				} else {
					// update the number of incoming edges
					tasks.replace(childTaskNode, numberOfIncomingEdges);
				}
			}
		}
		return taskListInTopologicalOrder;
	}

	private int calculateBottomLevel(TaskNode taskNode) {
		List<DataTransferEdge> outgoingEdges = taskNode.getOutgoingEdges();
		int weightOfTask = taskNode.getWeight();
		if (outgoingEdges.isEmpty()) {
			// leaf task node reached, update the maximum bottom level
			taskNode.setBottomLevel(weightOfTask);
			return weightOfTask;
		} else {
			int bottomLevel = weightOfTask;
			for (DataTransferEdge outgoingEdge : outgoingEdges) {
				TaskNode destinationTask = outgoingEdge.getSourceNode();
				int weightOfChildrenTasks = calculateBottomLevel(destinationTask);
				if (weightOfChildrenTasks + weightOfTask > bottomLevel) {
					bottomLevel = weightOfChildrenTasks + weightOfTask;
				}
			}
			taskNode.setBottomLevel(bottomLevel);
			return bottomLevel;
		}
	}

	/**
	 * Generate a list of empty processors, the id starts with 1
	 *
	 * @return a list of processors
	 */
	private List<Processor> generateProcessors() {

		List<Processor> processorList = new ArrayList<>();

		for (int i = 1; i <= NUM_OF_PROCESSOR; i++) {
			processorList.add(new Processor(i));
		}

		return processorList;
	}

	public LinkedHashMap<String, String> getInputRowsRaw() {
		return INPUT_ROWS_RAW;
	}

	public String getGraphName() {
		return _graphName;
	}

}
