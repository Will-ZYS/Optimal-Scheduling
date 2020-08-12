package se306.project1;

import java.util.ArrayList;
import java.util.List;

public class TaskNode {
    private int _weight;
    private String _name;
    private List<DataTransferEdge> _incomingEdges;
    private List<DataTransferEdge> _outgoingEdges;

    public TaskNode(int weight, String name) {
        _weight = weight;
        _name = name;
        _incomingEdges = new ArrayList<>();
        _outgoingEdges = new ArrayList<>();
    }

    public TaskNode(String name) {
        _name = name;
        _incomingEdges = new ArrayList<>();
        _outgoingEdges = new ArrayList<>();
    }

    public void addIncomingEdge(DataTransferEdge edge) {
        _incomingEdges.add(edge);
    }

    public void addOutgoingEdge(DataTransferEdge edge) {
        _outgoingEdges.add(edge);
    }

    public int getWeight() {
        return _weight;
    }

    public void setWeight(int weight) {
        _weight = weight;
    }

    public String getName() {
        return _name;
    }
}
