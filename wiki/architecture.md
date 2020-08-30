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
    - Facebook Messenger group chat
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

<hr/>

## **Pruning Techniques:**

#### **1. Detecting Equivalent shedules**

If there are identical tasks, there is no need to create permutations of identical tasks.    
A task is identical to another if it has the same weight, children, parent and same data transfer times 
for both incoming and outgoing edges.   
e.g. Given two identical tasks A and B, these states are the same.   
State 1:   
Processor 1: A, B   

State 2:   
Processor 1: B, A

#### **2. Reduce duplicate schedules by placing task on only one of the empty processor**

If there are multiple empty processors and we are allocating a task, 
we unnecessarily create more states by placing the task in each empty processor. 
There is no need to create new states, as they are logistically the same.   
e.g. We are allocating for Task A for 3 processors.    

We create three states:   
State 1:   
Processor 1: A   
Processor 2: -   
Processor 3: -   

State 2:   
Processor 1: -
Processor 2: A  
Processor 3: -  

State 3:   
Processor 1: -  
Processor 2: -  
Processor 3: A  

#### **3. Normalisation of processors**

Processors in each partial solution are ordered based on the end time.    
When scheduling a child task, the program will try to schedule it first on the processor with the least end time.   
This helps the program to achieve a relative balanced loading when creating our first upper bound, which helps decreases
the number of runtime run significantly as the size of the graph increases.   

#### **4. Reduce duplicated scheduling for independent tasks**

For independent tasks, the order doesn't make a difference in the overall end time.   
For example, if task "a" and "b" are both independent tasks, scheduling "ab" and "ba" are the same. 
Therefore, once we explore all possibilities of scheduling "a" first, we don't need to look into the possibilities of
scheduling "b" first.

#### **5. Detect duplicated scheduling in general**

Creating a hashmap to store 6 levels of partial solution nodes. 
The hashmap contains the level in the solution tree as the key and a list of partial solutions on that level.   
A partial solution is added to the hashmap after all its children have been explored. 
When the algorithm runs from the last level to the (last level - 7) level, all partial solutions in the last level stored 
in the hashmap is deleted to avoid memory leak. 

#### **6. Topological sort**

Sorting all tasks in topological order before scheduling. 

#### **7. Upper bound**

To create an upper bound for all other partial solutions to compare with, the program uses a recursive DFS branch and 
bound. A valid solution is first created before starting to explore other opportunities. 

#### **8. Heuristic equation**

Heuristic equations are used to estimate the lower bound for each partial solution. If the lower bound is larger than the
current upper bound, this partial solution is discarded. 

There are two heuristic equations used in this project:
1. Balance load: ( weight of all tasks + current idle time ) / number of processors
2. Critical path: max( start time + bottom level, end time plus bottom load )