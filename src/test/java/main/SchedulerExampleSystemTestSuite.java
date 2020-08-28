package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        SchedulerExampleSystemTest.class,
        SchedulerExampleParallelSystemTest.class
})

public class SchedulerExampleSystemTestSuite {
}
