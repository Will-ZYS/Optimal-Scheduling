package se306.project1;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputReader {

    String pathToDotFile;

    public InputReader ( String path ){
        pathToDotFile = path;
    }

    public void readInputFile () throws IOException {

        // read the file with provided path
        FileReader dotFile = new FileReader(pathToDotFile);
        BufferedReader brFile = new BufferedReader(dotFile);

        // read the file line by line and get desired information from each line
        String line;
        while ((line = brFile.readLine()) != null) {

            // ignore the line without the symbol '['
            if(!line.contains("[")){
                continue;
            }

            // get the weight and task node name from lines
            String[] lineArray = line.split("\\[");
            String attributeInfo = lineArray[0].trim();
            String weightInfo = lineArray[1].trim().split("=")[1];
            int weight = Integer.parseInt(weightInfo.substring(0, weightInfo.length()-2));

            if(attributeInfo.contains("->")){
                // process as edge
            }else{
                // process as node

            }

        }

        dotFile.close();
    }


}
