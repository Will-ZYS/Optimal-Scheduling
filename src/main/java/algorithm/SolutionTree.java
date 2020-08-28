package algorithm;

import java.util.*;

public class SolutionTree {
	private int _bestTime = Integer.MAX_VALUE; // best time
	private SolutionNode _bestSolution;
	private final List<TaskNode> TASKS;
	private final SolutionNode ROOT;
	private final boolean IDENTICAL_TASKS; // if any identical tasks have been detected
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
		IDENTICAL_TASKS = markIdenticalTasks();
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
		// Optimisation: HashMap of Tasks to Processors that are to be allocated this round.
		// If we have two or more TaskNodes that are identical then we only have to schedule them to a processor once.
		// i.e. if A and B are identical tasks, no need to schedule them on Processor 1.
		Map<TaskNode, Integer> taskToProcessor = new HashMap<>();

		// check the lower bound (estimation) of this node
		if (solutionNode.getLowerBound(TOTAL_TASK_WEIGHT) < _bestTime) {

			// get the unvisited nodes of this solutionNode
			List<TaskNode> unvisitedTaskNodes = solutionNode.getUnvisitedTaskNodes();

			// if this solutionNode still has unvisited task node
			if (!unvisitedTaskNodes.isEmpty()) {
				// optimisation: for independent tasks, the order of scheduling doesn't matter
				// e.g. if "a" and "b" are independent tasks (without parents and children),
				// then schedule a first or b first doesn't matter
				boolean hasSeenIndependentTask = false;

				// loop through all the unvisited task nodes
				for (TaskNode taskNode : unvisitedTaskNodes) {

					if (hasSeenIndependentTask) {
						continue;
					} else if (taskNode.getIncomingEdges().isEmpty() && taskNode.getOutgoingEdges().isEmpty()) {
						// this task is independent
						hasSeenIndependentTask = true;
					}

					// only go through this loop if there is at least one pair of identical tasks
					if (IDENTICAL_TASKS) {
						// check in the hashmap if an identical tasknode has been allocated
						boolean isIdenticalToTask = false;
						for (TaskNode other : taskToProcessor.keySet()) {
							if (taskNode.isIdenticalTo(other)) {
								isIdenticalToTask = true;
								break;
							}
						}
						if (isIdenticalToTask) continue; // if identical, skip this task
					}

					// optimisation: if more than one empty processor, only allocate a task to one
					boolean hasSeenEmpty = false;

					// check if this task node can be used to create a partial solution
					if (!solutionNode.canCreateNode(taskNode)) {
						break;
					} else {
						// find the possible start time for this taskNode
						solutionNode.calculateStartTime(taskNode);

						// loop through all processors
						for (int i = 0; i < NUMBER_OF_PROCESSORS; i++) {
							// if the processor is empty
							if (solutionNode.getProcessors().get(i).getEndTime() == 0) {
								if (!hasSeenEmpty) { // first instance of a processor with no tasks
									// now that we have allocated a task to an empty processor, there is no need
									// to allocate to another empty processor - eliminating identical states
									hasSeenEmpty = true;
								} else {
									// we've already allocated the task to an empty processor, no need to do it again
									continue;
								}
							}
							// call create child nodes by giving the id of processor as a parameter
							// get the returned child solutionNodes
							SolutionNode childSolutionNode = solutionNode.createChildNode(taskNode, i);

							// call algorithm based on this child solutionNodes
							DFSBranchAndBoundAlgorithm(childSolutionNode);

							taskToProcessor.put(taskNode, i);
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

	/**
	 * Upon initialisation, will check for identical TaskNodes.
	 * A task is identical to another task if it has the same weight, parent, children and
	 * data transfer times of both ingoing and outgoing edges.
	 * This will set a Hashmap in each task with the key of the identical task, allowing for
	 * O(1) lookups.
	 */
	private boolean markIdenticalTasks() {
		boolean identicalTasksFound = false;
		for (TaskNode taskA : TASKS) {
			for (TaskNode taskB : TASKS) {
				if (taskA == taskB) continue; // don't compare task A with task A

				if (!taskA.isIdenticalTo(taskB)) continue;

				// task A and B are identical
				taskA.setIdenticalNode(taskB);
				taskB.setIdenticalNode(taskA);
				identicalTasksFound = true;
			}
		}
		return identicalTasksFound;
	}
}
