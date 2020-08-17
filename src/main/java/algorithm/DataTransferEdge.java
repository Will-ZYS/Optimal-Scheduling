package algorithm;

public class DataTransferEdge {
    private final TaskNode _sourceTaskNode;
    private final int _dataTransferTime;

    public DataTransferEdge(TaskNode sourceTaskNode, int dataTransferTime) {
        _sourceTaskNode = sourceTaskNode;
        _dataTransferTime = dataTransferTime;
    }

    public TaskNode getSourceNode() {
        return _sourceTaskNode;
    }

    public int getDataTransferTime() {
        return _dataTransferTime;
    }
}
