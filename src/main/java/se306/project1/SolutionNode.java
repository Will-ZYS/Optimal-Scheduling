package se306.project1;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionNode {
    private int _processorId;
    private int _maxEndTime;
    private TaskNode _currentTask;

//    private Map<TaskNode, List<TaskNode>> _unvisitedTasksAndParents;

    private List<TaskNode> _unvisitedTaskNodes;

    private List<SolutionNode> _childNodes;
    private List<SolutionNode> _parentNodes;

    public SolutionNode(List<TaskNode> unvisitedTaskNodes, int processorId, TaskNode currentTask, ) {
        _unvisitedTaskNodes = unvisitedTaskNodes;
        _processorId = processorId;
        _currentTask = currentTask;
    }

    public boolean hasChild() {
        return false;
    }

    public int getLowerBound(TaskNode task, int numberOfProcessors) {
        int sumOfUnvisitedNodeWeight = 0;
        for (TaskNode unvisitedNode : _unvisitedTaskNodes) {
            sumOfUnvisitedNodeWeight += unvisitedNode.getWeight();
        }

        return ((_maxEndTime + sumOfUnvisitedNodeWeight) / numberOfProcessors);
    }

    public void createChildNodes() {
        // loop through edges of current task
        List<DataTransferEdge> outgoingEdges = _currentTask.getOutgoingEdges();
        for (DataTransferEdge outgoingEdge : outgoingEdges) {
            TaskNode desTaskNode = outgoingEdge.getDestinationNode();
            List<DataTransferEdge> incomingEdges = desTaskNode.getIncomingEdges();

            boolean hasUnvisitedParentNode = false;
            for (DataTransferEdge incomingEdge : incomingEdges) {
                TaskNode parentTaskNode = incomingEdge.getSourceNode();
                if (_unvisitedTaskNodes.contains(parentTaskNode)) {
                    hasUnvisitedParentNode = true;
                    break;
                }
            }

            if (!hasUnvisitedParentNode) {
                // create child solution node based on this desTaskNode
                _childNodes.add( new SolutionNode(  ) );
            }

        }

    }

    public List<SolutionNode> getChild() {
        return new ArrayList<>();
    }

    public int getTime() {
        return 0;
    }

    public TaskNode getTask() {
        return _currentTask;
    }

//    public Map<TaskNode, List<TaskNode>> getUnvisitedTasksAndParents() {
//        // return a copy of the hashmap
//        return new HashMap<>(_unvisitedTasksAndParents);
//    }
}
