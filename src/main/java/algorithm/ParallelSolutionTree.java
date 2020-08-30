package algorithm;

import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ForkJoinPool;

public class ParallelSolutionTree extends SolutionTree {
	private final int NUM_CORES;
	public static ForkJoinPool _forkJoinPool;

	public ParallelSolutionTree(List<TaskNode> allTasks, Queue<Processor> processors, int numCores) {
		super(allTasks, processors);
		NUM_CORES = numCores;
	}

	/**
	 * DFS branch and bound algorithm
	 *
	 * @param solutionNode the node to perform DFS branch and bound
	 */
	@Override
	protected void DFSBranchAndBoundAlgorithm(SolutionNode solutionNode) {
		_checkedSchedule++;
		Stack<SolutionNode> stack = new Stack<>();
		stack.push(ROOT);

		_forkJoinPool = new ForkJoinPool(NUM_CORES);
		SolutionRecursiveAction schedule = new SolutionRecursiveAction(stack, this);
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

	public boolean getIsIdenticalTask() {
		return IDENTICAL_TASKS;
	}
}
