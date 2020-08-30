package input;

import algorithm.ParallelSolutionTree;
import algorithm.SequentialSolutionTree;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * A unit test to test if input reader returns the correct instance of SolutionTree
 */
public class InputReaderUnitTest {

    @Test
    public void testInputReaderSequential() throws IOException {
        InputReader reader = new InputReader("src/test/resources/Nodes_7_OutTree.dot", 2, 1);
        assertEquals(SequentialSolutionTree.class, reader.readInputFile().getClass());
    }

    @Test
    public void testInputReaderParallel() throws IOException {
        InputReader reader = new InputReader("src/test/resources/Nodes_7_OutTree.dot", 2, 2);
        assertEquals(ParallelSolutionTree.class, reader.readInputFile().getClass());
    }
}
