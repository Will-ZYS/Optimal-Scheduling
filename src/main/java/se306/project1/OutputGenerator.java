package se306.project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputGenerator {
    SolutionNode _bestSolution;

    public OutputGenerator (SolutionNode bestSolution){
        _bestSolution = bestSolution;
        if (!preCheckSolution()){
            System.err.println("Something went wrong, the solution wasn't valid");
        }
    }

    public Boolean preCheckSolution(){
        if (_bestSolution.get_unvisitedTaskNodes().size() != 0 ) {
            return false;
        } else if (false){
            return false;
        }
        return true;
    }
}
