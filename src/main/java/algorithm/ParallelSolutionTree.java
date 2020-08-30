package algorithm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

		Map<Integer, List<SolutionNode>> visitedPartialSolutions = Collections.synchronizedMap(new HashMap<>());
		for (int i = 1; i <= getTasks().size(); i++) {
			visitedPartialSolutions.put(i, Collections.synchronizedList(new ArrayList<>()));
		}

		_forkJoinPool = new ForkJoinPool(NUM_CORES);
		SolutionRecursiveAction schedule = new SolutionRecursiveAction(visitedPartialSolutions, this, ROOT, 0);
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
