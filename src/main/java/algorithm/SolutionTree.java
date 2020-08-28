package algorithm;

import java.util.List;

public abstract class SolutionTree {
    protected int _bestTime = Integer.MAX_VALUE; // best time
    protected SolutionNode _bestSolution;
    protected final List<TaskNode> TASKS;
    protected final SolutionNode ROOT;
    protected final int NUMBER_OF_PROCESSORS;
    protected final int TOTAL_TASK_WEIGHT;

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
     * @param node the node to perform DFS branch and bound
     */
    protected abstract void DFSBranchAndBoundAlgorithm(SolutionNode node);
}
