package main;

import algorithm.ParallelSolutionTree;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * This tests the whole scheduler program using the example input graphs from Oliver
 * using 2 cores running in parallel
 */
public class SchedulerExampleParallelSystemTest {
    private static Scheduler _scheduler;

    @Rule
    public Timeout timeout = new Timeout(30, TimeUnit.MINUTES);

    @BeforeClass
    public static void setUp() throws Exception {
        _scheduler = new Scheduler();
    }

    /**
     * Test the whole program with the example Nodes_7_OutTree graph
     */
    @Test
    public void testNodes7OutTreeParallel() {
        _scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "2", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(28, _scheduler.getBestSolution().getEndTime());

        _scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "4", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(22, _scheduler.getBestSolution().getEndTime());
    }

    /**
     * Test the whole program with the example Nodes_8_Random graph
     */
    @Test
    public void testNodes8RandomParallel() {
        _scheduler.main(new String[] {"src/test/resources/Nodes_8_Random.dot", "2", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(581, _scheduler.getBestSolution().getEndTime());

        _scheduler.main(new String[] {"src/test/resources/Nodes_8_Random.dot", "4", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(581, _scheduler.getBestSolution().getEndTime());
    }

    /**
     * Test the whole program with the example Nodes_9_SeriesParallel graph
     */
    @Test
    public void testNodes9SeriesParallelInParallel() {
        _scheduler.main(new String[] {"src/test/resources/Nodes_9_SeriesParallel.dot", "2", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(55, _scheduler.getBestSolution().getEndTime());

        _scheduler.main(new String[] {"src/test/resources/Nodes_9_SeriesParallel.dot", "4", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(55, _scheduler.getBestSolution().getEndTime());
    }

    /**
     * Test the whole program with the example Nodes_10_Random graph
     */
    @Test
    public void testNodes10RandomParallel() {
        _scheduler.main(new String[] {"src/test/resources/Nodes_10_Random.dot", "2", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(50, _scheduler.getBestSolution().getEndTime());

        _scheduler.main(new String[] {"src/test/resources/Nodes_10_Random.dot", "4", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(50, _scheduler.getBestSolution().getEndTime());
    }

    /**
     * Test the whole program with the example Nodes_11_OutTree graph
     */
    @Test
    public void testNodes11OutTreeParallel() {
        _scheduler.main(new String[] {"src/test/resources/Nodes_11_OutTree.dot", "2", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(350, _scheduler.getBestSolution().getEndTime());

        _scheduler.main(new String[] {"src/test/resources/Nodes_11_OutTree.dot", "4", "-p", "2"});
        assertEquals(ParallelSolutionTree.class, _scheduler.getSolutionTree().getClass());
        assertEquals(227, _scheduler.getBestSolution().getEndTime());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        File file = new File("src/test/resources/Nodes_7_OutTree-output.dot");
        file.delete();

        file = new File("src/test/resources/Nodes_8_Random-output.dot");
        file.delete();

        file = new File("src/test/resources/Nodes_9_SeriesParallel-output.dot");
        file.delete();

        file = new File("src/test/resources/Nodes_10_Random-output.dot");
        file.delete();

        file = new File("src/test/resources/Nodes_11_OutTree-output.dot");
        file.delete();
    }
}