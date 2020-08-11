package se306.project1;

public class TaskNode {
    private int weight;
    private String name;

    public TaskNode(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public String getName() {
        return this.name;
    }
}
