package se306.project1;

public class DataTransferEdge {

    private TaskNode sourceTask;
    private TaskNode destinationTask;
    private int weight;

    public DataTransferEdge(TaskNode sourceTask, TaskNode destinationTask, int weight) {
        this.sourceTask = sourceTask;
        this.destinationTask = destinationTask;
        this.weight = weight;
    }

    public TaskNode getSourceTask() {
        return this.sourceTask;
    }

    public TaskNode getDestinationTask() {
        return this.destinationTask;
    }

    public int getWeight() {
        return this.weight;
    }
}
