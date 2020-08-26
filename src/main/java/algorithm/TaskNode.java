package algorithm;

import java.util.ArrayList;
import java.util.List;

public class TaskNode {
	private int _weight;
	private final String NAME;
	private final List<DataTransferEdge> INCOMING_EDGES;
	private final List<DataTransferEdge> OUTGOING_EDGES;

	public TaskNode(String name) {
		NAME = name;
		INCOMING_EDGES = new ArrayList<>();
		OUTGOING_EDGES = new ArrayList<>();
	}

	public TaskNode(int weight, String name) {
		_weight = weight;
		NAME = name;
		INCOMING_EDGES = new ArrayList<>();
		OUTGOING_EDGES = new ArrayList<>();
	}

	public void addIncomingEdge(DataTransferEdge edge) {
		INCOMING_EDGES.add(edge);
	}

	public void addOutgoingEdge(DataTransferEdge edge) {
		OUTGOING_EDGES.add(edge);
	}

	public int getWeight() {
		return _weight;
	}

	public void setWeight(int weight) {
		_weight = weight;
	}

	public List<DataTransferEdge> getIncomingEdges() {
		return INCOMING_EDGES;
	}

	public List<DataTransferEdge> getOutgoingEdges() {
		return OUTGOING_EDGES;
	}

	public String getName() {
		return NAME;
	}
}
