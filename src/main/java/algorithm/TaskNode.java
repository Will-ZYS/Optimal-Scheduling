package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskNode {
	private int _weight;
	private final String NAME;
	private final List<DataTransferEdge> INCOMING_EDGES;
	private final List<DataTransferEdge> OUTGOING_EDGES;
	private int _bottomLevel;
	private final HashMap<TaskNode, Boolean> IDENTICAL_TO; // allows for O(1) checking if the TaskNode is identical to another

	public TaskNode(String name) {
		NAME = name;
		_bottomLevel = 0;
		INCOMING_EDGES = new ArrayList<>();
		OUTGOING_EDGES = new ArrayList<>();
		IDENTICAL_TO = new HashMap<>();
	}

	public TaskNode(int weight, String name) {
		_weight = weight;
		NAME = name;
		_bottomLevel = 0;
		INCOMING_EDGES = new ArrayList<>();
		OUTGOING_EDGES = new ArrayList<>();
		IDENTICAL_TO = new HashMap<>();
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

	public void setBottomLevel(int bottomLevel) {
		_bottomLevel = bottomLevel;
	}

	public int getBottomLevel() {
		return _bottomLevel;
	}

	public boolean isIdenticalTo(TaskNode other) {
		// Weight of the task node have to be the same
		if (_weight != other.getWeight()) {
			return false;
		}

		// Collect incoming edges from the other task
		List<DataTransferEdge> incomingEdgesTaskB = other.getIncomingEdges();
		List<DataTransferEdge> outgoingEdgesTaskB = other.getOutgoingEdges();

		// identical lists must have the same size
		if (INCOMING_EDGES.size() != incomingEdgesTaskB.size()) {
			return false;
		}

		if (OUTGOING_EDGES.size() != outgoingEdgesTaskB.size()) {
			return false;
		}

		// parents and data transfer times must be the same
		for (int i = 0; i < INCOMING_EDGES.size(); i++) {
			DataTransferEdge edgeA = INCOMING_EDGES.get(i);
			DataTransferEdge edgeB = incomingEdgesTaskB.get(i);

			if (!edgeA.isIdenticalTo(edgeB)) return false;
		}

		// children and data transfer times must be the same
		for (int i = 0; i < OUTGOING_EDGES.size(); i++) {
			DataTransferEdge edgeA = OUTGOING_EDGES.get(i);
			DataTransferEdge edgeB = outgoingEdgesTaskB.get(i);

			if (!edgeA.isIdenticalTo(edgeB)) return false;
		}

		return true;
	}

	public void setIdenticalNode(TaskNode identicalNode) {
		IDENTICAL_TO.put(identicalNode, true);
	}
}
