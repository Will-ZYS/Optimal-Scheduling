package se306.project1;

import java.util.List;

public class TaskNode {
    private int _weight;
    private String _name;
    private List<DataTransferEdge> _incomingEdges;
    private List<DataTransferEdge> _outgoingEdges;

    public TaskNode(int weight, String name) {
        _weight = weight;
        _name = name;
    }

    public int getWeight() {
        return _weight;
    }


    public List<DataTransferEdge> getOutgoingEdges() {
        return _outgoingEdges;
    }

    public List<DataTransferEdge> getIncomingEdges() {
        return _incomingEdges;
    }
}
