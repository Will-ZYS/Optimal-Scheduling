package algorithm;

import java.util.*;

public class SequentialSolutionTree extends SolutionTree {

	public SequentialSolutionTree(List<TaskNode> allTasks, List<Processor> processors) {
		super(allTasks, processors);
	}

	@Override
	protected void DFSBranchAndBoundAlgorithm(SolutionNode solutionNode) {

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
}
