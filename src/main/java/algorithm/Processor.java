package algorithm;

import java.util.HashMap;
import java.util.Map;

public class Processor {
	// Integer: starting time of a specific task in this processor
	private final Map<TaskNode, Integer> TASKS;
	private int _endTime;
	private final int PROCESSOR_ID;

	public Processor(int id) {
		TASKS = new HashMap<>();
		_endTime = 0;
		PROCESSOR_ID = id;
	}

	/**
	 * Create a copy of a given processor
	 *
	 * @param processor a processor that to copy from
	 */
	public Processor(Processor processor) {
		PROCESSOR_ID = processor.getID();
		TASKS = new HashMap<>(processor.getTasks());
		_endTime = processor.getEndTime();
	}

	public Map<TaskNode, Integer> getTasks() {
		return TASKS;
	}

	public void addTask(TaskNode task, int startTime) {
		TASKS.put(task, startTime);
		_endTime = startTime + task.getWeight();
	}

	public int getEndTime() {
		return _endTime;
	}

	public void setEndTime(int _endTime) {
		this._endTime = _endTime;
	}

	public int getID() {
		return PROCESSOR_ID;
	}
}
