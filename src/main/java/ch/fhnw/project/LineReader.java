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
 * Created by Müller on 13.05.2016.
 */
public class LineReader extends DataReader {
@Override
    public Data parseContents(InputStream input) throws DataReaderException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        try {
            int lineNum = 0;
            String line = bufferedReader.readLine(); lineNum++;
            if(line == null || line.length() == 0){
                throw new DataReaderException("Error no data found");
            }

            //Get Number Of Variables
            int numberOfVariables = parseInt(line,lineNum);
            if(numberOfVariables == 0){
                throw new DataReaderException("Error no variables found");
            }

            //Get Variable Names
            String[] variableNames = new String[numberOfVariables];
            for(int i = 0; i < numberOfVariables; i++){
                line = bufferedReader.readLine(); lineNum++;
                if(line == null || line.length() == 0){
                    throw new DataReaderException("Error: tried to extract a variable name from line "+ lineNum +" but found no data there");
                }
                variableNames[i] = line;
            }
            //Get Delimiter
            String delimiter= bufferedReader.readLine(); lineNum++;
            if(line.length() == 0){
                throw new DataReaderException("Error: tried to extract delimiter "+ lineNum +" but found no delimiter there");
            }

            //Read Data For All Variables
            Map<String,Double[]> dataMap = new HashMap<>(numberOfVariables);
            int dataLength = -1;
            for(int i = 0; i < numberOfVariables; i++) {
                line = bufferedReader.readLine(); lineNum++;
                if(line == null || line.length() == 0){
                    throw new DataReaderException("Error: tried to extract data from line "+ lineNum +" but found no data there");
                }
                List<Double> data = new LinkedList<>();
                for (String s : linesplit(line, delimiter)) {
                    data.add(parseDouble(s,lineNum));
                }
                if(i == 0) {
                    //On First Iteration Remember Data Length So That We Can Compare With The Others
                    dataLength = data.size();
                }
                else{
                    if(dataLength != data.size()){
                    //On All Other Iteration We Make Sure That The Read Data Lines Have The Same Length
                    throw new DataReaderException("Error: datalength between variables are different, expected number of datapoints = "+ dataLength +"found number of datapoint = " + data.size() +",Error on line "+ lineNum);
                    }
                }
                Double[] variable = new Double[data.size()];
                data.toArray(variable);
                dataMap.put(variableNames[i], variable);
            }
                return  new DataImplementation(dataMap);
        }
        catch (IOException e) {
            throw new DataReaderException("Error while reading data "+e.getMessage());

        }
    }

    private  String[] linesplit(String line, String delimiter) {
        return line.split(delimiter);
    }
}
