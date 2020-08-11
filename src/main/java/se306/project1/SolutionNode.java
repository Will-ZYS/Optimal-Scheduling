package se306.project1;

import java.util.ArrayList;
import java.util.List;

public class SolutionNode {
    List<Processor> _processors;
    List<SolutionNode> _childNode;

    public boolean hasChild() {
        return false;
    }

    public int getLowerBound() {
        return 0;
    }

    public void createChildNodes() {
    }

    public List<SolutionNode> getChild() {
        return new ArrayList<>();
    }

    public int getTime() {
        return 0;
    }
}
