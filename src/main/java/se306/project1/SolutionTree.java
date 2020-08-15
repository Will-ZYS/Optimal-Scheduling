package se306.project1;

import java.util.*;

public class SolutionTree {
    private int _bestTime = Integer.MAX_VALUE; // best time
    private SolutionNode _bestSolution;
    private final List<TaskNode> _tasks;
    private final SolutionNode _root;
    private int _numberOfProcessors;

    public SolutionTree(List<TaskNode> allTasks, List<Processor> processors) {
        _root = new SolutionNode(processors, allTasks);
        _tasks = allTasks;
        _numberOfProcessors = processors.size();
    }

    /**
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

            // get the unvisited nodes of this solutionNode
            List<TaskNode> unvisitedTaskNodes = solutionNode.getUnvisitedTaskNodes();

            // if this solutionNode still has unvisited task node
            if (!unvisitedTaskNodes.isEmpty()) {
                // loop through all the unvisited task nodes
                for (TaskNode taskNode: unvisitedTaskNodes) {
                    // if this task node can be used to create a partial solution
                    if (solutionNode.canCreateNode(taskNode)) {
                        // find the possible start time for this taskNode
                        solutionNode.calculateStartTime(taskNode);

                        // loop through all processors
                        for (int i = 0; i < _numberOfProcessors; i++) {
                            // call create child nodes by giving the id of processor as a parameter
                            // get the returned child solutionNodes
                            SolutionNode childSolutionNode = solutionNode.createChildNode(taskNode, i);

                            // call algorithm based on this child solutionNodes
                            DFSBranchAndBoundAlgorithm(childSolutionNode);
                        }

                    }
                }
            } else {
                SolutionNode.printSolutionNode(solutionNode);
                // compare the actual time of the leaf to the best time
                if (solutionNode.getEndTime() < _bestTime) {
                    _bestSolution = solutionNode;
//                    SolutionNode.printSolutionNode(solutionNode);
                    _bestTime = solutionNode.getEndTime();
                }
            }

        }
//        else {
//            System.out.println("Ignored " + solutionNode.getLowerBound());
//        }
//        System.gc();
    }
}
