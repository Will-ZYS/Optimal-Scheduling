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

    /**
     * This function is used to generate dummy data before InputReader is implemented
     */
    private void generateDummyData() {
        // a,b,c,d four tasks
        TaskNode a = new TaskNode(2, "a");
        TaskNode b = new TaskNode(3, "b");
        TaskNode c = new TaskNode(3, "c");
        TaskNode d = new TaskNode(2, "d");

        _tasks.add(a);
        _tasks.add(b);
        _tasks.add(c);
        _tasks.add(d);

        // a->b: 1
        // a->c: 2
        // b->d: 2
        // c->d: 1
        DataTransferEdge aTob = new DataTransferEdge(a, b, 1);
        DataTransferEdge aToc = new DataTransferEdge(a, c, 2);
        DataTransferEdge bTod = new DataTransferEdge(b, d, 2);
        DataTransferEdge cTod = new DataTransferEdge(c, d, 1);

        List<DataTransferEdge> adjacentEdgeForNodeA = new ArrayList<DataTransferEdge>(Arrays.asList(aTob, aToc));
        List<DataTransferEdge> adjacentEdgeForNodeB = new ArrayList<DataTransferEdge>(Arrays.asList(bTod));
        List<DataTransferEdge> adjacentEdgeForNodeC = new ArrayList<DataTransferEdge>(Arrays.asList(cTod));
        List<DataTransferEdge> adjacentEdgeForNodeD = new ArrayList<DataTransferEdge>();

        _adjacentList.put(a, adjacentEdgeForNodeA);
        _adjacentList.put(b, adjacentEdgeForNodeB);
        _adjacentList.put(c, adjacentEdgeForNodeC);
        _adjacentList.put(d, adjacentEdgeForNodeD);
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
