package algorithm;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ForkJoinPool;

public class ParallelSolutionTree implements SolutionTree {
	private int _bestTime = Integer.MAX_VALUE; // best time
	private SolutionNode _bestSolution;
	private final List<TaskNode> TASKS;
	private final SolutionNode ROOT;
	private final int NUMBER_OF_PROCESSORS;
	private final int TOTAL_TASK_WEIGHT;
	private final int NUM_CORES;
	public static ForkJoinPool _forkJoinPool;

	public ParallelSolutionTree(List<TaskNode> allTasks, List<Processor> processors, int numCores) {
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

	@Override
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
		System.out.println("Parallel");
		Stack<SolutionNode> stack = new Stack<>();
		stack.push(ROOT);

		_forkJoinPool = new ForkJoinPool(NUM_CORES);
		ScheduleRecursiveAction schedule = new ScheduleRecursiveAction(stack, this);
		_forkJoinPool.invoke(schedule);
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
