package se306.project1;

public class DataTransferEdge {
    private TaskNode _sourceTaskNode;
    private TaskNode _desTaskNode;
    private int _dataTransferTime;
    private String _name;

    public DataTransferEdge(TaskNode sourceTaskNode, TaskNode desTaskNode, int dataTransferTime, String name) {
        _sourceTaskNode = sourceTaskNode;
        _desTaskNode = desTaskNode;
        _dataTransferTime = dataTransferTime;
        _name = name;
    }

    public TaskNode getSourceNode() {
        return _sourceTaskNode;
    }

    public TaskNode getDestinationNode() {
        return _desTaskNode;
    }

    public int getDataTransferTime() {
        return _dataTransferTime;
    }
}
