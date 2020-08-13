package se306.project1;

import java.util.*;

public class SolutionTree {
    private int _bestTime = Integer.MAX_VALUE; // best time
    private SolutionNode _bestSolution;
    private List<TaskNode> _tasks;
    private SolutionNode _root;

    public SolutionTree(List<TaskNode> allTasks, List<Processor> processors) {
        _root = new SolutionNode(processors, allTasks, null);
        _tasks = allTasks;
    }

    private void createChildren( SolutionNode root ) {
        List<TaskNode> children = new ArrayList<>();
        TaskNode parentTask = root.getTask();
        Map<TaskNode, List<TaskNode>> unvisitedTasksAndParents = root.getUnvisitedTasksAndParents();

        // find all executable child tasks and remove the parent tasks
        for (Map.Entry<TaskNode, List<TaskNode>> entry : unvisitedTasksAndParents.entrySet()) {
            List<TaskNode> parents = entry.getValue();
            TaskNode child = entry.getKey();

            parents.remove(parentTask);
            if (parents.isEmpty()) {
                // this child task is executable
                children.add(child);

                // remove this entry from the map to save memory space
                unvisitedTasksAndParents.remove(child);
            }
        }
    }

    /*
     * DFS branch and bound algorithm used to find the best solution
     * @return the optimal partial solution
     */
    public SolutionNode findOptimalSolution() {

        if (!_tasks.isEmpty()) {
            DFSBranchAndBoundAlgorithm(_root);
        } else {
            _bestTime = 0;
            _bestSolution = _root;
        }

        return _bestSolution;
    }

    private void DFSBranchAndBoundAlgorithm(SolutionNode solutionNode) {
        // check the lower bound (estimation) of this node
        if (solutionNode.getLowerBound() < _bestTime) {
            // create its child nodes
            solutionNode.createChildNodes();
            // check if this node has child
            if (solutionNode.hasChild()) {
                for (int i = 0; i < solutionNode.getChildNodes().size(); i++) {
                    DFSBranchAndBoundAlgorithm(solutionNode.getChildNodes().get(i));
                }
            } else {
                // compare the actual time of the leaf to the best time
                if (solutionNode.getEndTime() < _bestTime) {
                    _bestSolution = solutionNode;
                    _bestTime = solutionNode.getEndTime();
                }
            }
        }
    }
}
