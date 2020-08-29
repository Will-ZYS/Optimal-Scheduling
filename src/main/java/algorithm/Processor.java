package algorithm;

import java.util.HashMap;
import java.util.Map;

public class Processor {
	// Integer: starting time of a specific task in this processor
	private final Map<TaskNode, Integer> TASKS;
	private int _endTime;
	private final int PROCESSOR_ID;
	private int _idleTime;
	private int _weightOfCriticalPath;

	public Processor(int id) {
		TASKS = new HashMap<>();
		_endTime = 0;
		_idleTime = 0;
		_weightOfCriticalPath = 0;
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
		_weightOfCriticalPath = processor.getWeightOfCriticalPath();
	}

	public Map<TaskNode, Integer> getTasks() {
		return TASKS;
	}

	public void addTask(TaskNode task, int startTime) {
		TASKS.put(task, startTime);
		_idleTime += startTime - _endTime;
		_endTime = startTime + task.getWeight();

		int bottomLevel = task.getBottomLevel();
		int bottomLoad = task.getBottomLoad();

		if (Math.max(startTime + bottomLevel, _endTime + bottomLoad) > _weightOfCriticalPath) {
			_weightOfCriticalPath = Math.max(startTime + bottomLevel, _endTime + bottomLoad);
		}
	}

	public int getWeightOfCriticalPath() {
		return _weightOfCriticalPath;
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

	public boolean isDuplicateOf(Processor other) {
		if (PROCESSOR_ID != other.getID()) return false;
//		System.out.println("In Processor: Comparing " + PROCESSOR_ID + " with " + other.getID());
//		System.out.println(_endTime + " " + other.getEndTime());
		if (_endTime != other.getEndTime()) return false;

//		System.out.println(_idleTime + " " + other.getIdleTime());
		if (_idleTime != other.getIdleTime()) return false;

//		System.out.println(_weightOfCriticalPath + " " + other.getWeightOfCriticalPath());
		if (_weightOfCriticalPath != other.getWeightOfCriticalPath()) return false;

		// if the TaskNodes are different
		if (!(TASKS.keySet().equals(other.getTasks().keySet()))) return false;
//		System.out.println("Tasks are the same");

		// if the start time is different
		for (Map.Entry<TaskNode, Integer> entryA : TASKS.entrySet()) {
			for (Map.Entry<TaskNode, Integer> entryB : TASKS.entrySet()) {
				// check if tasks match
//				System.out.println("Task comparison: " + entryA.getKey().getName() + ":" + entryB.getKey().getName());
				if (entryA.getKey().getName().equals(entryB.getKey().getName())) {
//					System.out.println("Start time: " + entryA.getValue() + ":" + entryB.getValue());
					if (entryA.getValue() != entryB.getValue()) return false; // the start time is not the same
				}
			}
		}

//		System.out.println("Processor are the same");

		return true;
	}
}
