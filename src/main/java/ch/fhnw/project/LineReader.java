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
        BufferedReader bis= new BufferedReader(new InputStreamReader(input));
        try {
            int lineNum=0;
            String line = bis.readLine();lineNum++;
            if(line.length()==0||line==null){
                throw new DataReaderException("Error no data found");
            }

            //get number of variables
            int numvar= parseInt(line,lineNum);
            if(numvar==0){
                throw new DataReaderException("Error no variables found");
            }

            //get variable names
            String[] varnames= new String[numvar];
            for(int i=0;i<numvar;i++){
                line = bis.readLine();lineNum++;
                if(line==null || line.length()==0){
                    throw new DataReaderException("Error: tried to extract a variable name from line "+lineNum+" but found no data there");
                }
                varnames[i]=line;
            }
            //get Delimiter
            String delimiter= bis.readLine();lineNum++;
            if(line==null || line.length()==0){
                throw new DataReaderException("Error: tried to extract delimiter "+lineNum+" but found no delimiter there");
            }

            //read Data for all variables
            Map<String,Double[]> dataMap= new HashMap<>(numvar);
            int dataLength=-1;
            for(int i=0;i<numvar;i++) {
                line = bis.readLine();lineNum++;
                if(line==null || line.length()==0){
                    throw new DataReaderException("Error: tried to extract data from line "+lineNum+" but found no data there");
                }

                List<Double> data = new LinkedList<>();

                for (String s : linesplit(line, delimiter)) {
                    data.add(parseDouble(s,lineNum));

                }
                if(i == 0) {
                    //on first iteration remember data length so that we can compare with the others
                    dataLength = data.size();
                }
                else{
                    if(dataLength!=data.size()){                    //on all other iteration we make sure that the read data lines have the same length
                    throw new DataReaderException("Error: datalength between variables are different, expected number of datapoints = "+ dataLength +"found number of datapoint= " +data.size()+",Error on line "+ lineNum);
                }
                }
                Double[] vals = new Double[data.size()];
                data.toArray(vals);
                dataMap.put(varnames[i], vals);
            }
                return  new DataImplementation(dataMap);












        } catch (IOException e) {
            throw new DataReaderException("Error while reading data "+e.getMessage());

        }



}

    private  String[] linesplit(String line, String delimiter) {
        return line.split(delimiter);
    }

}
