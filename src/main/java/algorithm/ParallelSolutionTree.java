package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelSolutionTree extends SolutionTree {
	private int _bestTime = Integer.MAX_VALUE; // best time
	private SolutionNode _bestSolution;
	private final List<TaskNode> TASKS;
	private final SolutionNode ROOT;
	private final int NUMBER_OF_PROCESSORS;
	private final int TOTAL_TASK_WEIGHT;
	private final int NUM_CORES;
	public static ForkJoinPool _forkJoinPool;

	public ParallelSolutionTree(List<TaskNode> allTasks, List<Processor> processors, int numCores) {
		super(allTasks, processors);
		ROOT = new SolutionNode(processors, allTasks);
		TASKS = allTasks;
		NUMBER_OF_PROCESSORS = processors.size();
		int total_task_weight = 0;
		for (TaskNode taskNode : allTasks) {
			total_task_weight += taskNode.getWeight();
		}
		TOTAL_TASK_WEIGHT = total_task_weight;
		NUM_CORES = numCores;
	}

	/**
	 * DFS branch and bound algorithm
	 *
	 * @param solutionNode the node to perform DFS branch and bound
	 */
	private void DFSBranchAndBoundAlgorithm(SolutionNode solutionNode) {
		Stack<SolutionNode> stack = new Stack<>();
		stack.push(ROOT);

		_forkJoinPool = new ForkJoinPool(NUM_CORES);
		ScheduleRecursiveAction schedule = new ScheduleRecursiveAction(stack, this);
		_forkJoinPool.invoke(schedule);

		// check the lower bound (estimation) of this node
		if (solutionNode.getLowerBound(TOTAL_TASK_WEIGHT) < _bestTime) {

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
						for (int i = 0; i < NUMBER_OF_PROCESSORS; i++) {
							// call create child nodes by giving the id of processor as a parameter
							// get the returned child solutionNodes
							SolutionNode childSolutionNode = solutionNode.createChildNode(taskNode, i);

							// call algorithm based on this child solutionNodes
							DFSBranchAndBoundAlgorithm(childSolutionNode);
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

	public int getTotalTaskWeight() {
		return TOTAL_TASK_WEIGHT;
	}

	public synchronized int getBestTime() {
		return _bestTime;
	}

	public synchronized void setBestTime(int bestTime) {
		_bestTime = bestTime;
	}

	public synchronized void setBestSolution(SolutionNode node) {
		_bestSolution = node;
	}

	public int getNumberOfProcessors() {
		return NUMBER_OF_PROCESSORS;
	}
}
