package se306.project1;

import java.util.HashMap;
import java.util.Map;

public class Processor {
    private Map<TaskNode, Integer> _tasks;
    private int _endTime;
    private final int PROCESSOR_ID;

    public Processor (int id) {
        _tasks = new HashMap<>();
        _endTime = 0;
        PROCESSOR_ID = id;
    }

    // Create a copy of a given processor
    public Processor(Processor processor) {
        PROCESSOR_ID = processor.getID();
        _tasks = new HashMap<>(processor.getTasks());
        _endTime = processor.getEndTime();
    }

    public Map<TaskNode, Integer> getTasks() {
        return _tasks;
    }

    public void addTask(TaskNode task, int startTime) {
        _tasks.put(task, startTime);
        _endTime = startTime + task.getWeight();
    }

    public int getEndTime() {
        return _endTime;
    }

    public void setEndTime(int _endTime) {
        this._endTime = _endTime;
    }

    public int getID(){
        return PROCESSOR_ID;
    }
}
