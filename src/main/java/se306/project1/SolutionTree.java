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

    public SolutionNode DFSBranchAndBound () {

        // get the root
        if ( _root.hasChild() ) {
            algorithm(_root);
        } else {
            _bestTime = 0;
            _bestSolution = _root;
        }

        return _bestSolution;

    }

    private void algorithm( SolutionNode node ) {
        // check the lower bound (estimation) of this node
        if ( node.getLowerBound() < _bestTime ) {
            // create its child nodes
            node.createChildNodes();
            // check if this node has child
            if ( node.hasChild() ) {
                for ( int i = 0; i < node.getChildNodes().size(); i++ ) {
                    algorithm( node.getChildNodes().get(i) );
                }
            } else {
                // compare the actual time of the leaf to the best time
                if ( node.getEndTime() < _bestTime ) {
                    _bestSolution = node;
                    _bestTime = node.getEndTime();
                }
            }
        }
    }
}
