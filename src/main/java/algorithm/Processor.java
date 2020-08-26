package algorithm;

import java.util.HashMap;
import java.util.Map;

public class Processor {
	// Integer: starting time of a specific task in this processor
	private final Map<TaskNode, Integer> TASKS;
	private int _endTime;
	private final int PROCESSOR_ID;
	private int _idleTime;
	private int _critialPath;

	public Processor(int id) {
		TASKS = new HashMap<>();
		_endTime = 0;
		_idleTime = 0;
		_critialPath = 0;
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
		_idleTime = processor.getIdleTime();
		_critialPath = processor.getCriticalPath();
	}

	public Map<TaskNode, Integer> getTasks() {
		return TASKS;
	}

	public void addTask(TaskNode task, int startTime) {
		TASKS.put(task, startTime);
		_idleTime += startTime - _endTime;
		_endTime = startTime + task.getWeight();
		if (startTime + task.getBottomLevel() > _critialPath) {
			_critialPath = startTime + task.getBottomLevel();
		}
	}

	public int getCriticalPath() {
		return _critialPath;
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

	public int getIdleTime() {
		return _idleTime;
	}
}
