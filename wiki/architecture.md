# SOFTENG 306 Project 1 Documentation - Architecture

**Tools:**
- Development tools:
    - IntelliJ IDEA
    - Git
    - GitHub 
    - Gradle
 - Project management tools:
    - Google Drive:   
      To store meeting notes, pseudocode, UML diagram, etc
    - Trello 
- Communication tools:
    - Facebook massager group
    - Zoom

**Operating System:**
- Windows
- Linux

**Algorithm:**
- DFS Branch and Bound Algorithm   
    - Algorithm description:
      1. Perform a DFS search to get one valid solution, note down the end time
      2. Bonce one level up and generate another child partial solution
      3. Estimate the lower bound of this partial solution
      4. If the lower bound is smaller than current end time, keep constructing its child partial solutions
      5. If the lower bound is larger, ignore this partial solution and go to the next partial solution
    
    - pseudocode for the algorithm: 
        ```
        DFS branch and bound algorithm:
        public class SolutionNode {
            List<Processor> _processors;
            List<SolutionNode> _childNode;
        }
        
        public class SolutionTree {
            private int _bestTime = Integer.MAX_VALUE; // best time 
            private SolutionNode _bestSolution;
            private SolutionNode _root;
        
            public Solution DFSBranchAndBound () {
        
                // get the root 
                if ( _root.hasChild() ) {
                    for ( int i = 0; i < root.getChild().length; i++ ) {
                        algorithm( root.getChild[ i ] );
                    }
                } else {
                    _bestTime = 0;
                    _bestSolution = root;
                }
        
                return _bestSolution; 
                
            }
        
            private void algorithm( SolutionNode node ) {
                // check the lower bound (estimation) of this node 
                if ( node.getLowerBound() >= _bestTime ) {
                    return;
                } else {
                    // create its child nodes 
                    node.createChildNodes();
                    
                    // check if this node has child 
                    if ( node.hasChild() ) {
                        for ( int i = 0; i < node.getChild().length; i++ ) {
                            algorithm( root.getChild[ i ] );
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
        }
        ```
- [Class diagram](https://drive.google.com/file/d/11M0STuVmsQmt-jP08tXwZP_CkoLoFpfj/view?usp=sharing)



