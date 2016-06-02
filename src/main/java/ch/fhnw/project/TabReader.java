package ch.fhnw.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mueller on 13.05.2016.
 */
public class TabReader extends DataReader  {
    @Override

    public Data parseContents(InputStream input)throws DataReaderException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

        try {
            //Get Variable Names
            String line = bufferedReader.readLine();
            if(line == null || line.length() == 0){
                throw new DataReaderException("Error;no data found");
            }
            String[] nameOfVariables = splitLine(line);

            //Get Number Of Variables
            int numberOfVariables = nameOfVariables.length;
            if(numberOfVariables == 0){
                throw new DataReaderException("Error; no variables found");
            }
            List[] values = new List[numberOfVariables];
            for (int i = 0; i < numberOfVariables; i++) {
                values[i] = new LinkedList<>();
            }

            //Read Data For All Variables
            long lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null){
                lineNumber++;
                //Ignores Empty Lines At The End Of A File
                if(line.length() > 0){
                    String[] contents = splitLine(line);
                    if(contents.length != numberOfVariables){
                        throw new DataReaderException("Error on line " + lineNumber + " :expected " + numberOfVariables + "values, but found " + contents.length + "values instead");
                    }
                    for(int i = 0; i < contents.length; i++){
                        String variableToParse = contents[i];
                        Double variable = parseDouble(variableToParse,lineNumber);
                        values[i].add(variable);
                    }
                }
            }

            if(lineNumber == 1){
                throw new DataReaderException("Error: only variables, but no data found");
            }
            Map<String,Double[]> dataMap = new HashMap<>(numberOfVariables);

            for(int i = 0; i < numberOfVariables; i++){
                Double[] variable =new Double[values[i].size()];
                values[i].toArray(variable);
                dataMap.put(nameOfVariables[i],variable);
            }
            return new DataImplementation(dataMap);
        }

        catch (IOException e) {
             throw new DataReaderException("Error while reading the data" + e.getMessage());
        }
    }

    private String[] splitLine(String line) {
        return line.split("\t");
    }
}
