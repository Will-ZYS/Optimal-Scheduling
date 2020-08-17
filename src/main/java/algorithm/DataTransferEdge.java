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
}
