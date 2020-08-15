package se306.project1;

import java.util.*;

public class SolutionNode {
    private List<Processor> _processors;
    private List<TaskNode> _unvisitedTaskNodes;
    private List<SolutionNode> _childNodes = new ArrayList<>();
    private SolutionNode _parentNode;
    private int _endTime;   // maximum end time for this partial solution

    public SolutionNode(List<Processor> processors, List<TaskNode> unvisitedTaskNodes) {
        _processors = processors;
        _unvisitedTaskNodes = unvisitedTaskNodes;
    }

    /**
     * This function try to create child solution nodes for every unvisited Task nodes.
     */
    public void createChildNodes() {

//        System.out.println("Unvisited Tasks:");
        for (TaskNode taskNode: _unvisitedTaskNodes) {
//            System.out.print(taskNode.getName() + " ");
            createChildNode(taskNode);
        }

//        System.out.println();
    }

    /**
     * This function creates a SolutionNode for every processor for a single given task
     * @param taskNode The task to be allocated to processors to create SolutionNodes
     */
    private void createChildNode(TaskNode taskNode) {
        // check this taskNode has no incoming edges
        if (canCreateNode(taskNode)) {

            Map<Processor, Integer> candidateStartTimes = new HashMap<>();

            // Instantiate all the processors
            for (Processor p : _processors) {
                candidateStartTimes.put(p, null);
            }

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
                candidateStartTimes.put(lastProcessor, time);
            }

            // find the processor that has the maximum candidateStartTime
            Processor latestProcessor = null;
            int max = 0;
            int secondMax = 0;

            for (Map.Entry<Processor, Integer> entry : candidateStartTimes.entrySet()) {
                if (entry.getValue() > max) {
                    secondMax = max;
                    max = entry.getValue();
                    latestProcessor = entry.getKey();
                }
                else if (entry.getValue() > secondMax) {
                    secondMax = entry.getValue();
                }
            }

            int time;
            // loop through all processors as the taskNode can be put on any processor
            for (int i = 0; i < _processors.size(); i++) {

                // deep copy of the list of processors
                List<Processor> processors = new ArrayList<>();
                for (Processor processor : _processors) {
                    processors.add(new Processor(processor));
                }

                Processor processor = processors.get(i);
                if (latestProcessor == null) {
                    time = processor.getEndTime();
                }
                else if (processor.getID() != latestProcessor.getID()) {
                    time = Math.max(processor.getEndTime(), max);
                } else {
                    time = Math.max(processor.getEndTime(), secondMax);
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
                SolutionNode childSolutionNode = new SolutionNode(processors, unvisitedTaskNodes);

                // add end time and parent to the child node
                if (_parentNode != null) {
                    childSolutionNode.setEndTime(Math.max(time + taskNode.getWeight(), _parentNode.getEndTime()));
                }
                else {
                    childSolutionNode.setEndTime(time + taskNode.getWeight());
                }
                childSolutionNode.setParentNode(this);

                // add the child node to the list of child node in the current node
                _childNodes.add(childSolutionNode);

            }
        }
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

    public boolean hasChild() {
        return !_childNodes.isEmpty();
    }

    public List<SolutionNode> getChildNodes() {
        return _childNodes;
    }

    public void setParentNode(SolutionNode parentNode) {
        this._parentNode = parentNode;
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
