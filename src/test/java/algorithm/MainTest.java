package algorithm;

import main.Scheduler;
import org.junit.Test;

public class MainTest {

    @Test
    public void testMain() {
        Scheduler.main(new String[]{ "abc.dot", "3" });
    }
}
