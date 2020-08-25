package algorithm;

import java.util.*;

public class SolutionTree {
	private int _bestTime = Integer.MAX_VALUE; // best time
	private SolutionNode _bestSolution;
	private final List<TaskNode> TASKS;
	private final SolutionNode ROOT;
	private final int NUMBER_OF_PROCESSORS;
	private final int TOTAL_TASK_WEIGHT;

	public SolutionTree(List<TaskNode> allTasks, List<Processor> processors) {
		ROOT = new SolutionNode(processors, allTasks);
		TASKS = allTasks;
		NUMBER_OF_PROCESSORS = processors.size();
		int total_task_weight = 0;
		for (TaskNode taskNode : allTasks) {
			total_task_weight += taskNode.getWeight();
		}
		TOTAL_TASK_WEIGHT = total_task_weight;
	}

	/**
	 * Find the optimal solution by using the DFS branch and bound algorithm
	 *
	 * @return the optimal solution
	 */
	public SolutionNode findOptimalSolution() {

		if (! TASKS.isEmpty()) {
			DFSBranchAndBoundAlgorithm(ROOT);
		} else {
			_bestTime = 0;
			_bestSolution = ROOT;
		}
		return _bestSolution;
	}

	/**
	 * DFS branch and bound algorithm
	 *
	 * @param solutionNode the node to perform DFS branch and bound
	 */
	private void DFSBranchAndBoundAlgorithm(SolutionNode solutionNode) {

		// check the lower bound (estimation) of this node
		if (solutionNode.getLowerBound(TOTAL_TASK_WEIGHT) < _bestTime) {

			// get the unvisited nodes of this solutionNode
			List<TaskNode> unvisitedTaskNodes = solutionNode.getUnvisitedTaskNodes();

			// if this solutionNode still has unvisited task node
			if (! unvisitedTaskNodes.isEmpty()) {
				// loop through all the unvisited task nodes
				for (TaskNode taskNode : unvisitedTaskNodes) {
					// optimisation: if more than one empty processor, only allocate a task to one
					boolean hasSeenEmpty = false;

					// if this task node can be used to create a partial solution
					if (solutionNode.canCreateNode(taskNode)) {

						// find the possible start time for this taskNode
						solutionNode.calculateStartTime(taskNode);

						// loop through all processors
						for (int i = 0; i < NUMBER_OF_PROCESSORS; i++) {
							// if the processor is empty
							if (solutionNode.getProcessors().get(i).getEndTime() == 0) {
								if (!hasSeenEmpty) { // first instance of a processor with no tasks
									// call create child nodes by giving the id of processor as a parameter
									// get the returned child solutionNodes
									SolutionNode childSolutionNode = solutionNode.createChildNode(taskNode, i);

									// call algorithm based on this child solutionNodes
									DFSBranchAndBoundAlgorithm(childSolutionNode);

									// now that we have allocated a task to an empty processor, there is no need
									// to allocate to another empty processor - eliminating identical states
									hasSeenEmpty = true;
								}
							} else {
								// call create child nodes by giving the id of processor as a parameter
								// get the returned child solutionNodes
								SolutionNode childSolutionNode = solutionNode.createChildNode(taskNode, i);

								// call algorithm based on this child solutionNodes
								DFSBranchAndBoundAlgorithm(childSolutionNode);
							}
						}
					}
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
