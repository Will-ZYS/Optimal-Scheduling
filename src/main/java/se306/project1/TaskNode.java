package se306.project1;

public class TaskNode {
    private int weight;
    private String name;

    public TaskNode(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public TaskNode(String name) {
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }
}
