package helper;

import algorithm.Processor;

import java.util.Comparator;

public class ProcessorComparator implements Comparator<Processor> {
	@Override
	public int compare(Processor p1, Processor p2) {
		if (p1.getEndTime() < p2.getEndTime()) {
			return -1;
		} else if (p1.getEndTime() > p2.getEndTime()){
			return 1;
		}
		return 0;
	}
}