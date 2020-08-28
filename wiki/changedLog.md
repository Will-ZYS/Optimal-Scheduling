# SOFTENG 306 Project 1 Documentation - Log

**27/07/2020 - 04/08/2020**
- Construct [work breakdown structure](https://drive.google.com/file/d/1t0jTo27w_MbBBrNYL2wVsXB7Nq3jl6lp/view?usp=sharing)
    - Work breakdown structure follows the waterfall development methodology
    - Each task contains a unique story code 
    
- Construct [network diagram](https://drive.google.com/file/d/1OkyNZE6Lb5DUaFBgzlU9tacvQyrIPqZU/view?usp=sharing)

- Construct [Gantt Chart](https://drive.google.com/file/d/1d6GdfwgcR6T0-cyUGwHF8K0bXwbIza9M/view?usp=sharing)

<hr/>

**07/08/2020**
- Team meeting 
- Decide to use branch and bound algorithm instead of A*
- Set up a [trello board](https://trello.com/invite/b/1VJ3Qf6P/26b5c6b0781b4e8fdfe003bdb1f27099/softeng306-milestone1) 
  to manage tasks that need to be done by milestone 1
- Allocate tasks to each team member
- Main tasks before milestone 1:
    - Read inputs 
    - Construct a graph with each node acts as a partial solution
    - Perform a simple DFS search to get a valid solution 
 
<hr/>

**10/08/2020**
- Construct UML diagram for DFS branch and bound algorithm
- Implement pseudocode for this algorithm
- Team meeting to discuss UML diagram and pseudocode 

<hr/>
    
**11/08/2020**
- Meeting among Emily(Ziwei), Martin and Will. 
- Start implementation on SolutionTree class 
- Mainly working on creating child partial solutionNode (please refer to architecture.md to see the detailed design)

_Changes made to the plan:_  
- Graph don't need to be construct first before searching
- Searching and constructing the graph happens at the same time, 
  only construct part of the graph that is worth searching   
- No longer performing a simple DFS, instead, implementing the DFS branch and bound algorithm directly  

_Reason for change:_
- implement graph first and then simple DFS requires a different design which doesn't match with the 
  previous constructed UML
- Refactoring later will take more time

<hr/>

**11/08/2020**
- Pair programming section between Tommy and Kevin (Jiawei) 
- Working on handling program argument and .dot file input

_Changes made to the plan:_  
- Add code to verify that user inputs the correct program argument in the right format
- Get the number of processors and output file name from the argument
- Implement an InputReader to read the input .dot file line by line
- A list of tasks, and an adjacency list representing the graph were created and passed to the SolutionTree   

_Reason for change:_
- To ensure user enters the correct argument on the command line while running the program, and give correct usage instruction
- Convert the plain .dot file to some useful format represented by Object (i.e. TaskNode, DataTransferEdge) to make the
  algorithm implementation easier

<hr/>

**12/08/2020**
- Meeting among Emily(Ziwei), Martin and Will. 
- Finished implementation on the algorithm

_Changes made to the design:_  
- Initially, the design is to have each partial solutionNode contains only the latest added task and the processor that 
  this task is added in.   
  Changes the solutionNode to include a list of processors, each processor includes a list of task as well as the 
  starting time of each task.
  
_Reason for change:_
- When a new task is added into a processor, the program needs to look for all its parent tasks and the corresponsding 
  processors to decide the start time of this new task.
  If the solutionNode doesn't have any information on parents tasks, it is hard to calculate the start time. 
  
<hr/>

**12/08/2020**
- Pair programming section between Tommy and Kevin (Jiawei) 
- Change the InputReader implementation requested by Emily

_Changes made to the design:_  
- Instead of representing the graph with an adjacency list, each TaskNode now has its list of incoming edges and outgoing
  edges.
- InputReader now initialise a list of empty Processor objects according to the input argument and pass the list to the 
  SolutionTree
- Change the readInputFile() function in InputReader so it returns a SolutionTree object so the main function can make 
  the algorithm call
  
_Reason for change:_
- Emily, Will and Martin, who are implementing the algorithm, felt that using lists of incoming and outgoing edges, 
  and initialise the Processor list early can help them implement the algorithm easier
- The main function needs to know the SolutionTree that the InputReader created to invoke the algorithm and write output
  
<hr/>
  
**13/08/2020**
- Team meeting to explain the implementation of input reader and algorithm to the whole group
- Merge everyone's code together 
- Cleaning up wiki and start manual testing on the algorithm

<hr/>

**14/08/2020**
- Bug fix and enhancement of the main method by Tommy

_Changes made to the design:_
- Fix the default output file name to INPUT-output
- Change the usage message to be more intuitive and useful

_Reason for change:_
- The previous default output file name does not match the one in the project description
- To improve user experience when the users make the error

<hr/>

**17/08/2020**
- Bug fix to trim spaces in the output done by Lucas
- Added comments and Java Docs
- Removed printings in the console

_Changes made to the design:_
- Refactored the file structure of the source files into different packages
- Only scheduling all tasks onto one processor for Milestone 1 release

_Reason for change:_
- To have a clear file structure.
- Scheduling all tasks onto one processor to ensure the program will produce an output.

<hr/>

**18/08/2020**
- Update Gradle build script by Tommy

_Changes made to the design:_
- Change the Gradle project build script 
- Write a simple README file for running the project

_Reason for change:_
- The project can be easily cloned, compiled and run
- To provide detailed instruction for running the application

<hr/>

**24/08/2020**
- Bug fix in InputReader class by Tommy

_Changes made to the design:_
- Add regex match when reading lines from the .dot file
- Catch FileNotFoundException when reading .dot file, display intuitive error message

_Reason for change:_
- To make the InputReader more robust
- Improve user experience on error handling

<hr/>

**26/08/2020**
- Bug fix in algorithm by Emily 

_Changes made to the design:_
- Change the heuristic equation to be (weight of all tasks + total idle time) / number of processors.   
This equation calculates the end time when there is perfect balance loading across all processors.
- Add another equation - max(start time of each allocated task + its bottom level).  
  The final lower bound is the maximum between the result of two heuristic equations.

_Reason for change:_
- The original heuristic equation used - (Endtime + weight/number of processors) doesn't necessarily give a lower bound.  
    - For example, consider task "a" in processor 1 which ends at 2, and task "b" in processor 2 which ends at 5.  
  If we still have a third task "c", based on the current equation, it will calculate the lower bound as 
  (5 + weight of c / 2), which is definitely larger than 5.  
  However, it is possible for c to be scheduled directly after "a" in processor 1. The end time doesn't necessarily 
  need to be larger than 5.
  
 <hr/>
 
**28/08/2020**
- Implemented Parallelisation  
 
_Changes made to the design:_
 - InputReader now takes in an extra argument, numCores which is the number of cores the user inputs. It is defaulted to 1.
 - InputReader creates either a SequentialSolutionTree or a ParallelSolutionTree depending on if the user created
 - A ParallelSolution tree creates a SolutionRecursiveAction that a ForkJoinPool executes.
 - A SolutionRecursiveAction contains the tasks the threads operate DFS Branch and Bound on. If the task size is too
 large, then the SolutionRecursiveAction recursively makes new instances, with the tasks being split into smaller chunks. 
 
_Reason for change:_
 - The project brief required the search to support parallelisation.
 - ForkJoinPool is the best choice for our DFS because it allows for work to be easily split up into smaller tasks.   
