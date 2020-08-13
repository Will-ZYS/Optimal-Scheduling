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
  
**13/08/2020**
- Team meeting to explain the implementation of input reader and algorithm to the whole group
- Merge everyone's code together 
- Cleaning up wiki and start manual testing on the algorithm

<hr/>

  