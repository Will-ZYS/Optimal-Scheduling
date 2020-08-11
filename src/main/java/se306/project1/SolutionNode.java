package se306.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionNode {
    private Processor _processors;
    private int _maxEndTime;
    private TaskNode _task;

    private Map<TaskNode, List<TaskNode>> _unvisitedTasksAndParents;

    private List<SolutionNode> _childNodes;
    private List<SolutionNode> _parentNodes;

    public SolutionNode(Map<TaskNode, List<TaskNode>> unvisitedTasksAndParents) {
        _unvisitedTasksAndParents = unvisitedTasksAndParents;
    }

    public boolean hasChild() {
        return false;
    }

    public int getLowerBound(TaskNode task, int numberOfProcessors) {
        int sumOfUnvisitedNodeWeight = 0;
        for (Map.Entry<TaskNode, List<TaskNode>> entry : _unvisitedTasksAndParents.entrySet()) {
            TaskNode taskNode = entry.getKey();
            sumOfUnvisitedNodeWeight += taskNode.getWeight();
        }

        return ((_maxEndTime + sumOfUnvisitedNodeWeight) / numberOfProcessors);
    }

    public void createChildNodes() {
    }

    public List<SolutionNode> getChild() {
        return new ArrayList<>();
    }

    public int getTime() {
        return 0;
    }

    public TaskNode getTask() {
        return _task;
    }

    public Map<TaskNode, List<TaskNode>> getUnvisitedTasksAndParents() {
        // return a copy of the hashmap
        return new HashMap<>(_unvisitedTasksAndParents);
    }
}
