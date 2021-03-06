package algorithm;

import java.util.*;

import helper.ProcessorComparator;

public class SolutionNode {
	private final Queue<Processor> PROCESSORS;
	private final List<TaskNode> UNVISITED_TASK_NODES;
	private int _endTime;   // maximum end time for this partial solution

	// these three fields below are used to find the possible start time of a new taskNode
	Processor _latestProcessor = null;
	int _maxEndTime = 0;
	int _secondMaxEndTime = 0;

	public SolutionNode(Queue<Processor> processors, List<TaskNode> unvisitedTaskNodes) {
		PROCESSORS = processors;
		UNVISITED_TASK_NODES = unvisitedTaskNodes;
		_endTime = - 1;
	}

	/**
	 * Calculate the possible start time of a particular task
	 *
	 * @param taskNode the task to be scheduled
	 */
	public void calculateStartTime(TaskNode taskNode) {
		_latestProcessor = null;
		_maxEndTime = 0;
		_secondMaxEndTime = 0;

		// Put all the values of parents to the hashmap
		for (Processor lastProcessor : PROCESSORS) {
			int time = 0;


			// loop through all its parents on a specific processor
			// find out the earliest start time based on its parents on this processor
			// assuming this task is put on a different processor from its parents
			for (DataTransferEdge edge : taskNode.getIncomingEdges()) {
				TaskNode parentTask = edge.getSourceNode();
				Integer parentTaskStartTime = lastProcessor.getTasks().get(parentTask);
				if (parentTaskStartTime != null && parentTaskStartTime +
						parentTask.getWeight() + edge.getDataTransferTime() > time) {
					time = parentTaskStartTime + parentTask.getWeight() + edge.getDataTransferTime();
				}
			}
			if (time > _maxEndTime) {
				_secondMaxEndTime = _maxEndTime;
				_maxEndTime = time;
				_latestProcessor = lastProcessor;
			} else if (time > _secondMaxEndTime) {
				_secondMaxEndTime = time;
			}
		}
	}

	/**
	 * This function creates a SolutionNode for a single given task on a given processor
	 *
	 * @param taskNode       The task to be allocated to processors to create SolutionNodes
	 * @param processorID the ID of the processor that this task is going to be put on
	 * @return the solution node created which represents a partial solution
	 */
	public SolutionNode createChildNode(TaskNode taskNode, int processorID) {

		int time;

		// deep copy of the list of processors
		Queue<Processor> processors = new PriorityQueue<>(ProcessorComparator.getProcessorComparator());
		Processor allocatedProcessor = null;
		for (Processor processor : PROCESSORS) {
			Processor copiedProcessor = new Processor(processor);
			if (copiedProcessor.getID() == processorID) {
				// only add the allocatedProcessor to the priority queue after updating its end time
				allocatedProcessor = copiedProcessor;
			} else {
				processors.add(copiedProcessor);
			}
		}

		if (_latestProcessor == null) {
			time = allocatedProcessor.getEndTime();
		} else if (allocatedProcessor.getID() != _latestProcessor.getID()) {
			time = Math.max(allocatedProcessor.getEndTime(), _maxEndTime);
		} else {
			time = Math.max(allocatedProcessor.getEndTime(), _secondMaxEndTime);
		}

		// add task and start time of task to the processor
		allocatedProcessor.addTask(taskNode, time);

		//set end time of the processor
		allocatedProcessor.setEndTime(time + taskNode.getWeight());

		// add it to the processors queue
		processors.add(allocatedProcessor);

		// update unvisited task node in the child node by removing
		// the current task in the child node that is being created
		List<TaskNode> unvisitedTaskNodes = new ArrayList<>(UNVISITED_TASK_NODES);
		unvisitedTaskNodes.remove(taskNode);

		//Instantiate the child solution node
		SolutionNode childSolutionNode = new SolutionNode(processors, unvisitedTaskNodes);

		// add end time to the child node
		if (_endTime != - 1) {
			childSolutionNode.setEndTime(Math.max(time + taskNode.getWeight(), _endTime));
		} else {
			childSolutionNode.setEndTime(time + taskNode.getWeight());
		}

		return childSolutionNode;
	}

	/**
	 * This function validates is a TaskNode can be used to create a SolutionNod by checking
	 * 1) if it has any parents or
	 * 2) if the parents have been visited
	 *
	 * @return true - has no unvisited parents and can be scheduled
	 * false - has unvisited parents and cannot be scheduled now
	 */
	public boolean canCreateNode(TaskNode taskNode) {
		List<DataTransferEdge> incomingEdges = taskNode.getIncomingEdges();

		boolean hasUnvisitedParents = false;
		for (DataTransferEdge incomingEdge : incomingEdges) {
			TaskNode parentTaskNode = incomingEdge.getSourceNode();
			if (UNVISITED_TASK_NODES.contains(parentTaskNode)) {
				hasUnvisitedParents = true;
				break;
			}
		}
		return ! hasUnvisitedParents;
	}

	/**
	 * This function calculates the LowerBound of the current
	 *
	 * @return the estimated earliest end time
	 */
	public int getLowerBound(int totalTaskWeight) {
		int totalIdleTime = 0;

		//heuristic equation 1: max((start time + max(bottom level, bottom load) of each allocated task)
		int potentialLowerBoundOne = 0;

		for (Processor processor : PROCESSORS) {
			totalIdleTime += processor.getIdleTime();
			if (processor.getWeightOfCriticalPath() > potentialLowerBoundOne) {
				potentialLowerBoundOne = processor.getWeightOfCriticalPath();
			}
		}

		// heuristic equation 2: (total weight of all tasks + total idle time) / number of processors
		int potentialLowerBoundTwo = (int) Math.ceil(((double)totalTaskWeight + totalIdleTime) / PROCESSORS.size());

		return (Math.max(potentialLowerBoundOne, potentialLowerBoundTwo));
	}

	public int getEndTime() {
		return _endTime;
	}

	public void setEndTime(int _endTime) {
		this._endTime = _endTime;
	}

	public Queue<Processor> getProcessors() {
		return PROCESSORS;
	}

	public List<TaskNode> getUnvisitedTaskNodes() {
		return UNVISITED_TASK_NODES;
	}

	/**
	 * This function print out the details of a given SolutionNode in the terminal for testing purpose
	 *
	 * @param solutionNode The SolutionNode to be print out
	 */
	public static void printSolutionNode(SolutionNode solutionNode) {
		System.out.println();
		System.out.println("Printing out details of Solution Node:");
		List<TaskNode> unvisitedTasks = solutionNode.getUnvisitedTaskNodes();
		System.out.println("Unvisited Tasks :");
		for (TaskNode task : unvisitedTasks) {
			System.out.print(task.getName() + " ");
		}
		System.out.println();

		Queue<Processor> processors = solutionNode.getProcessors();
		for (Processor processor : processors) {
			System.out.println("Processor: " + processor.getID());
			System.out.println("Tasks :");
			Map<TaskNode, Integer> tasks = processor.getTasks();
			for (TaskNode task : tasks.keySet()) {
				System.out.print(task.getName() + " start at ");
				System.out.print(tasks.get(task) + "\n");
			}
			System.out.println();
		}
		System.out.println("End Time: " + solutionNode.getEndTime());
	}

	public boolean isDuplicateOf(SolutionNode other) {
		if (_endTime != other.getEndTime()) return false;

		if (UNVISITED_TASK_NODES.size() != other.getUnvisitedTaskNodes().size()) return false;

		for (int i = 0; i < UNVISITED_TASK_NODES.size(); i++) {
			TaskNode taskA = UNVISITED_TASK_NODES.get(i);
			TaskNode taskB = other.getUnvisitedTaskNodes().get(i);
			if (!(taskA.getName().equals(taskB.getName()))) return false;
		}

		for (Processor pA : PROCESSORS) {
			for (Processor pB : other.getProcessors()) {
				if (pA.getID() == pB.getID()) {
					if (!(pA.isDuplicateOf(pB))) return false;
				}
			}
		}
		return true;
	}

}
