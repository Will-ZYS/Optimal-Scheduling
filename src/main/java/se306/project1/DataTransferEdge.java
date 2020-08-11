package se306.project1;

public class DataTransferEdge {
    private TaskNode _sourceTaskNode;
    private TaskNode _desTaskNode;
    private int _dataTransferTime;

    public DataTransferEdge(TaskNode sourceTaskNode, TaskNode desTaskNode, int dataTransferTime) {
        _sourceTaskNode = sourceTaskNode;
        _desTaskNode = desTaskNode;
        _dataTransferTime = dataTransferTime;
    }

    public TaskNode getSourceNode() {
        return _sourceTaskNode;
    }

    public TaskNode getDestinationNode() {
        return _desTaskNode;
    }
}
