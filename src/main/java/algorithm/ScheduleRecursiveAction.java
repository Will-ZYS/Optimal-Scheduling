package algorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ScheduleRecursiveAction extends RecursiveAction {
	private Stack<SolutionNode> _workload;
	private static final int THRESHOLD = 4;
	private final ParallelSolutionTree PARALLEL_SOLUTION_TREE;
	private final int TOTAL_TASK_WEIGHT;

	public ScheduleRecursiveAction(Stack<SolutionNode> workload, ParallelSolutionTree parallelSolutionTree) {
		_workload = workload;
		PARALLEL_SOLUTION_TREE = parallelSolutionTree;
		TOTAL_TASK_WEIGHT = PARALLEL_SOLUTION_TREE.getTotalTaskWeight();
	}

	@Override
	protected void compute() {
		if (_workload.size() > THRESHOLD) {
			ForkJoinTask.invokeAll(createSubtasks());
		} else {
			doParallelDFS();
		}
	}

	private List<ScheduleRecursiveAction> createSubtasks() {
		List<ScheduleRecursiveAction> subtasks = new ArrayList<>();

		// remove one SolutionNode from the stack and split it from the rest
		SolutionNode node = _workload.pop();
		Stack<SolutionNode> subWorkload = new Stack<>();
		subWorkload.push(node);

		subtasks.add(new ScheduleRecursiveAction(subWorkload, PARALLEL_SOLUTION_TREE));
		subtasks.add(new ScheduleRecursiveAction(_workload, PARALLEL_SOLUTION_TREE));

		return subtasks;
	}

	/**
	 * DFS branch and bound algorithm
	 */
	private void doParallelDFS() {
		while (!_workload.isEmpty()) {
			SolutionNode solutionNode = _workload.pop();

			// check the lower bound (estimation) of this node
			int bestTime = PARALLEL_SOLUTION_TREE.getBestTime();
			if (solutionNode.getLowerBound(TOTAL_TASK_WEIGHT) < bestTime) {
				// get the unvisited nodes of this solutionNode
				List<TaskNode> unvisitedTaskNodes = solutionNode.getUnvisitedTaskNodes();

				// if this solutionNode still has unvisited task node
				if (! unvisitedTaskNodes.isEmpty()) {
					// loop through all the unvisited task nodes
					for (TaskNode taskNode : unvisitedTaskNodes) {
						// if this task node can be used to create a partial solution
						if (solutionNode.canCreateNode(taskNode)) {

							// find the possible start time for this taskNode
							solutionNode.calculateStartTime(taskNode);

							// loop through all processors
							int numProcessors = PARALLEL_SOLUTION_TREE.getNumberOfProcessors();
							for (int i = 0; i < numProcessors; i++) {
								// call create child nodes by giving the id of processor as a parameter
								// get the returned child solutionNodes
								SolutionNode childSolutionNode = solutionNode.createChildNode(taskNode, i);

								// put new child into the stack
								_workload.push(childSolutionNode);
							}
						}
					}
				} else {
					// compare the actual time of the leaf to the best time
					if (solutionNode.getEndTime() < bestTime) {
						PARALLEL_SOLUTION_TREE.setBestTime(solutionNode.getEndTime());
						PARALLEL_SOLUTION_TREE.setBestSolution(solutionNode);
					}
				}
			}

			// if the stack size becomes larger, then we split the tasks
			if (_workload.size() > THRESHOLD) {
				ForkJoinTask.invokeAll(createSubtasks());
			}
		}
	}
}
