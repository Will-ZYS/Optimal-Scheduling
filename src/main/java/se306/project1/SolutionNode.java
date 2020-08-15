package se306.project1;

import java.util.*;

public class SolutionNode {
    private final List<Processor> _processors;
    private final TaskNode _currentTask;  // the last task that is being put into the SolutionNode
    private final List<TaskNode> _unvisitedTaskNodes;
    private int _endTime;   // maximum end time for this partial solution

    // these three fields below are used to find the possible start time of a new taskNode
    Processor _latestProcessor = null;
    int _maxEndTime = 0;
    int _secondMaxEndTime = 0;

    public SolutionNode(List<Processor> processors, List<TaskNode> unvisitedTaskNodes, TaskNode currentTask) {
        _processors = processors;
        _unvisitedTaskNodes = unvisitedTaskNodes;
        _currentTask = currentTask;
        _endTime = -1;
    }

    public void calculateStartTime(TaskNode taskNode) {

        // Put all the values of parents to the hashmap
        for (Processor lastProcessor : _processors) {
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
                }
                else if (time > _secondMaxEndTime) {
                    _secondMaxEndTime = time;
                }
            }
        }
    }

    /**
     * This function creates a SolutionNode for every processor for a single given task
     * @param taskNode The task to be allocated to processors to create SolutionNodes
     */
    public SolutionNode createChildNode(TaskNode taskNode, int processorIndex) {

        int time;

        // deep copy of the list of processors
        List<Processor> processors = new ArrayList<>();
        for (Processor processor : _processors) {
            processors.add(new Processor(processor));
        }

        Processor processor = processors.get(processorIndex);
        if (_latestProcessor == null) {
            time = processor.getEndTime();
        } else if (processor.getID() != _latestProcessor.getID()) {
            time = Math.max(processor.getEndTime(), _maxEndTime);
        } else {
            time = Math.max(processor.getEndTime(), _secondMaxEndTime);
        }

        // add task and start time of task to the processor
        processor.addTask(taskNode, time);

        //set end time of the processor
        processor.setEndTime(time + taskNode.getWeight());

        // update unvisited task node in the child node by removing
        // the current task in the child node that is being created
        List<TaskNode> unvisitedTaskNodes = new ArrayList<>(_unvisitedTaskNodes);
        unvisitedTaskNodes.remove(taskNode);

        //Instantiate the child solution node
        SolutionNode childSolutionNode = new SolutionNode(processors, unvisitedTaskNodes, taskNode);

        // add end time to the child node
        if (_endTime != - 1) {
            childSolutionNode.setEndTime(Math.max(time + taskNode.getWeight(), _endTime));
        } else {
            childSolutionNode.setEndTime(time + taskNode.getWeight());
        }

        return childSolutionNode;
    }

    /**
     * This function validates is a TaskNode can be used to create a SolutionNod by
     * 1) checking if it has any parents or
     * 2) if the parents have been visited
     * @return the total transferTime if SolutionNode can be created otherwise -1
     */
    public boolean canCreateNode(TaskNode taskNode) {
        List<DataTransferEdge> incomingEdges = taskNode.getIncomingEdges();

        boolean hasUnvisitedParents = false;
        for (DataTransferEdge incomingEdge : incomingEdges) {
            TaskNode parentTaskNode = incomingEdge.getSourceNode();
            if (_unvisitedTaskNodes.contains(parentTaskNode)) {
                hasUnvisitedParents = true;
                break;
            }
        }
        return  !hasUnvisitedParents;
    }

    /**
     * This function calculates the LowerBound of the current
     * @return the estimated earliest end time
     */
    public int getLowerBound() {
        int sumOfUnvisitedNodeWeight = 0;
        for (TaskNode unvisitedNode : _unvisitedTaskNodes) {
            sumOfUnvisitedNodeWeight += unvisitedNode.getWeight();
        }
        int maxEndTime = 0;
        for (Processor processor : _processors) {
            if (processor.getEndTime() > maxEndTime) {
                maxEndTime = processor.getEndTime();
            }
        }
        return (maxEndTime + (sumOfUnvisitedNodeWeight / _processors.size()));
    }

    public int getEndTime() {
        return _endTime;
    }

    public void setEndTime(int _endTime) {
        this._endTime = _endTime;
    }

    public List<Processor> getProcessors() {
        return _processors;
    }

    public List<TaskNode> getUnvisitedTaskNodes() {
        return _unvisitedTaskNodes;
    }

    /**
     * This function print out the details of a given SolutionNode in the terminal
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

        List<Processor> processors = solutionNode.getProcessors();
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
}
