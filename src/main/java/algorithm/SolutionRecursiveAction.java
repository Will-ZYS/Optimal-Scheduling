package algorithm;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class SolutionRecursiveAction extends RecursiveAction {
	private Stack<SolutionNode> _workload;
	private static final int THRESHOLD = 4;
	private final ParallelSolutionTree PARALLEL_SOLUTION_TREE;
	private final int TOTAL_TASK_WEIGHT;

	public SolutionRecursiveAction(Stack<SolutionNode> workload, ParallelSolutionTree parallelSolutionTree) {
		_workload = workload;
		PARALLEL_SOLUTION_TREE = parallelSolutionTree;
		TOTAL_TASK_WEIGHT = PARALLEL_SOLUTION_TREE.getTotalTaskWeight();
	}

	@Override
	protected void compute() {
		if (_workload.size() > THRESHOLD) {
			ForkJoinTask.invokeAll(createSubtasks()); // create sub tasks if the stack size is beyond the threshold
		} else {
			doParallelDFS();
		}
	}

	/**
	 * This method splits up the tasks into two subtasks.
	 * The first subtask contains only the head of the stack, while the second subtask contains the rest of the elements.
	 * @return list of subtasks to perform parallel processing on.
	 */
	private List<SolutionRecursiveAction> createSubtasks() {
		List<SolutionRecursiveAction> subtasks = new ArrayList<>();

		// remove one SolutionNode from the stack and split it from the rest
		SolutionNode node = _workload.pop();
		Stack<SolutionNode> subWorkload = new Stack<>();
		subWorkload.push(node);

		subtasks.add(new SolutionRecursiveAction(subWorkload, PARALLEL_SOLUTION_TREE));
		subtasks.add(new SolutionRecursiveAction(_workload, PARALLEL_SOLUTION_TREE));

		return subtasks;
	}

	/**
	 * DFS branch and bound algorithm
	 */
	private void doParallelDFS() {
		while (!_workload.isEmpty()) {
			SolutionNode solutionNode = _workload.pop();
			Map<TaskNode, Integer> taskToProcessor = new HashMap<>();


			// get the unvisited nodes of this solutionNode
			List<TaskNode> unvisitedTaskNodes = solutionNode.getUnvisitedTaskNodes();

			// if this solutionNode still has unvisited task node
			if (!unvisitedTaskNodes.isEmpty()) {
				// loop through all the unvisited task nodes
				for (TaskNode taskNode : unvisitedTaskNodes) {

					// only go through this loop if there is at least one pair of identical tasks
					if (PARALLEL_SOLUTION_TREE.getIsIdenticalTask()) {
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

					// if this task node can be used to create a partial solution
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

							// put new child into the stack
							if (childSolutionNode.getLowerBound(TOTAL_TASK_WEIGHT) < PARALLEL_SOLUTION_TREE.getBestTime()) {
								_workload.push(childSolutionNode);
							}

							taskToProcessor.put(taskNode, processor.getID());
						}
					}
				}
			} else {

				// compare the actual time of the leaf to the best time
				if (solutionNode.getEndTime() < PARALLEL_SOLUTION_TREE.getBestTime()) {
					PARALLEL_SOLUTION_TREE.setBestTime(solutionNode.getEndTime());
					PARALLEL_SOLUTION_TREE.setBestSolution(solutionNode);
				}
			}

			// if the stack size becomes larger, then we split the tasks
			if (_workload.size() > THRESHOLD) {
				ForkJoinTask.invokeAll(createSubtasks());
			}
		}
	}
}
