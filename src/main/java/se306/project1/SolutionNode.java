package se306.project1;

import java.util.*;

public class SolutionNode {
    private List<Processor> _processors;
    private TaskNode _currentTask;
    private List<TaskNode> _unvisitedTaskNodes;
    private List<SolutionNode> _childNodes = new ArrayList<>();
    private List<SolutionNode> _parentNodes = new ArrayList<>();
    private int _endTime;

    public SolutionNode(List<Processor> processors, List<TaskNode> unvisitedTaskNodes, TaskNode currentTask) {
        _processors = processors;
        _unvisitedTaskNodes = unvisitedTaskNodes;
        _currentTask = currentTask;
    }



    public void createChildNodes() {

        // if the current task is empty, the solution node is the root
        if (_currentTask == null) {
            // Loop through all task nodes to create child solution nodes that
            // can be created (No incoming edges)
            for (TaskNode taskNode: _unvisitedTaskNodes) {
                createChildNode(taskNode);
            }
        } else {

            // loop through edges of current task to find the child tasks
            List<DataTransferEdge> outgoingEdges = _currentTask.getOutgoingEdges();
            for (DataTransferEdge outgoingEdge : outgoingEdges) {
                TaskNode desTaskNode = outgoingEdge.getDestinationNode();
                createChildNode(desTaskNode);
            }
        }

    }

    private void createChildNode(TaskNode taskNode) {
        // create the childNode
        if (canCreateNode(taskNode)) {


            //  NEW OPTION
            // HashMap<Processor,StartTime>
            // We get the parent's StartTime + Task.getWiehgt) + getTranseferTime()
            // Put in hashmap
            // loop through each processor
            // modify value of Hashmap[currentProcessor]
            // we find


            // copy the list of processors
            List<Processor> processors = new ArrayList<>();
            for (Processor processor : _processors) {
                processors.add(new Processor(processor));
            }

            // for each processor create child solution node based on this desTaskNode
            for (Processor processor : processors) {
                // get processor endTime
                int time = processor.getEndTime();

                for (Processor lastProcessor : _processors) {
                    if (lastProcessor.getID() != processor.getID()) {
                        for (DataTransferEdge edge : taskNode.getIncomingEdges()) {
                            TaskNode parentTask = edge.getSourceNode();
                            Integer parentTaskStartTime = lastProcessor.getTasks().get(parentTask);
                            if (parentTaskStartTime != null && parentTaskStartTime +
                                    parentTask.getWeight() + edge.getDataTransferTime() > time) {
                                time = parentTaskStartTime + parentTask.getWeight() + edge.getDataTransferTime();
                            }
                        }
                    }
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
                SolutionNode childNode = new SolutionNode(processors, unvisitedTaskNodes, taskNode);

                // add end time and parent to the child node
                childNode.setEndTime(time + taskNode.getWeight());
                childNode.addParentNodes(this);

                // add the child node to the list of child node in the current node
                _childNodes.add(childNode);

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
     * @return
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
        return ((maxEndTime + sumOfUnvisitedNodeWeight) / _processors.size());
    }

    public boolean hasChild() {
        return !_childNodes.isEmpty();
    }

    public TaskNode getTask() {
        return _currentTask;
    }

    public List<SolutionNode> getChildNodes() {
        return _childNodes;
    }

    public List<SolutionNode> getParentNodes() {
        return _parentNodes;
    }

    public void addParentNodes(SolutionNode parentNodes) {
        this._parentNodes.add(parentNodes);
    }

    public int getEndTime() {
        return _endTime;
    }

    public void setEndTime(int _endTime) {
        this._endTime = _endTime;
    }
}
