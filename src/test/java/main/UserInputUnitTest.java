package main;

import org.junit.*;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class UserInputUnitTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testEmptyArgument() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {});
    }

    @Test
    public void testInvalidFileName() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"invalid.dot", "1"});
    }

    @Test
    public void testNoProcessorInput() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"invalid.dot"});
    }

    @Test
    public void testNegativeProcessor() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "-1"});
    }

    @Test
    public void testInvalidProcessor() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "a"});
    }

    @Test
    public void testNoOutputSpecified() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-o"});
    }

    @Test
    public void testNoOutputSpecifiedFollowedByMore() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-o", "-p", "2"});
    }

    @Test
    public void testNegativeCore() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-p", "-2"});
    }

    @Test
    public void testInvalidCore() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-p", "q"});
    }

    @Test
    public void testNoCoreSpecified() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-p"});
    }

    @Test
    public void testNoCoreSpecifiedFollowedByMore() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-p", "-o", "out"});
    }

    @Test
    public void testInvalidOptionalFlag() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-p", "2", "-q", "2w"});
    }

    @Test
    public void testInvalidOptionalFlagInMiddle() {
        exit.expectSystemExitWithStatus(1);
        Scheduler.main(new String[] {"src/test/resources/Nodes_7_OutTree.dot", "1", "-q", "2w", "-p", "2"});
    }
}
