package se306.project1;

public class SolutionTree {
    private int _bestTime = Integer.MAX_VALUE; // best time
    private SolutionNode _bestSolution;
    private SolutionNode _root;

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
        if ( node.getLowerBound() >= _bestTime ) {
            System.out.print("Ignored" + node);
        } else {
            // create its child nodes
            node.createChildNodes();
            // check if this node has child
            if ( node.hasChild() ) {
                for ( int i = 0; i < node.getChild().size(); i++ ) {
                    algorithm( node.getChild().get(i) );
                }
            } else {
                // compare the actual time of the leaf to the best time
                if ( node.getTime() < _bestTime ) {
                    _bestSolution = node;
                    _bestTime = getTotalTime( node );
                }
            }
        }
    }

    private int getTotalTime(SolutionNode node) {
        return 0;
    }

}
