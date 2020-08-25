package algorithm;

public class DataTransferEdge {
	private final TaskNode SOURCE_TASK_NODE;
	private final int DATA_TRANSFER_TIME;

	public DataTransferEdge(TaskNode sourceTaskNode, int dataTransferTime) {
		SOURCE_TASK_NODE = sourceTaskNode;
		DATA_TRANSFER_TIME = dataTransferTime;
	}

	public TaskNode getSourceNode() {
		return SOURCE_TASK_NODE;
	}

	public int getDataTransferTime() {
		return DATA_TRANSFER_TIME;
	}

	public boolean isIdenticalTo(DataTransferEdge other) {
		String sourceA = SOURCE_TASK_NODE.getName();
		String sourceB = other.getSourceNode().getName();

		// parent must be the same
		if (!sourceA.equals(sourceB)) return false;

		// data transfer time must be the same
		if (DATA_TRANSFER_TIME != other.getDataTransferTime()) return false;

		return true;
	}
}
