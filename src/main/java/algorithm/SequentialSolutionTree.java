package algorithm;

import java.util.*;

public class SequentialSolutionTree extends SolutionTree {

	public SequentialSolutionTree(List<TaskNode> allTasks, Queue<Processor> processors) {
		super(allTasks, processors);
	}

	/**
	 * DFS branch and bound algorithm
	 *
	 * @param solutionNode the node to perform DFS branch and bound
	 */
	@Override
	protected void DFSBranchAndBoundAlgorithm(SolutionNode solutionNode) {

		_checkedSchedule++;
		//System.out.println(_checkedSchedule);

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

					if (hasSeenIndependentTask && taskNode.getIncomingEdges().isEmpty()
											   && taskNode.getOutgoingEdges().isEmpty()) {
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
						for (Processor processor : solutionNode.getProcessors()) {
							// if the processor is empty
							if (processor.getEndTime() == 0) {
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
							SolutionNode childSolutionNode = solutionNode.createChildNode(taskNode, processor.getID());

							// call algorithm based on this child solutionNodes
							DFSBranchAndBoundAlgorithm(childSolutionNode);

							taskToProcessor.put(taskNode, processor.getID());
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
