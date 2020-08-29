package helper;

import algorithm.Processor;

import java.util.Comparator;

public class ProcessorComparator implements Comparator<Processor> {
	// static variable single_instance of type Singleton
	private static ProcessorComparator _processorComparator = null;

	@Override
	public int compare(Processor p1, Processor p2) {
		if (p1.getEndTime() < p2.getEndTime()) {
			return -1;
		} else if (p1.getEndTime() > p2.getEndTime()){
			return 1;
		}
		return 0;
	}

	// static method to create instance of Singleton class
	public static ProcessorComparator getProcessorComparator()
	{
		if (_processorComparator == null) {
			_processorComparator = new ProcessorComparator();
		}
		return _processorComparator;
	}
}