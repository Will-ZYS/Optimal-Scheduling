package algorithm;

import java.util.ArrayList;
import java.util.List;

public class TaskNode {
    private int _weight;
    private final String _name;
    private final List<DataTransferEdge> _incomingEdges;

    public TaskNode(String name) {
        _name = name;
        _incomingEdges = new ArrayList<>();
    }

    public TaskNode(int weight, String name) {
        _weight = weight;
        _name = name;
        _incomingEdges = new ArrayList<>();
    }

    public void addIncomingEdge(DataTransferEdge edge) {
        _incomingEdges.add(edge);
    }

    public int getWeight() {
        return _weight;
    }

    public void setWeight(int weight) {
        _weight = weight;
    }

    public List<DataTransferEdge> getIncomingEdges() {
        return _incomingEdges;
    }

    public String getName() {
        return _name;
    }
}
