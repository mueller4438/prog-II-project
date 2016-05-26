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
public class TabReader extends DataReader  {
    @Override

    public Data parseContents(InputStream input)throws DataReaderException{
        BufferedReader bis= new BufferedReader(new InputStreamReader(input));

        try {
            int linenum=0;

            //get variable names
            String line = bis.readLine();linenum++;
            if(line.length()==0 || line==null){
                throw new DataReaderException("Error;no data found");
            }
            String[] varnames = splitLine(line);

            //get number of variables
            int numvars = varnames.length;
            if(numvars==0){
                throw new DataReaderException("Error;no variables found");
            }
            List<Double>[] values = new List[numvars];
            for (int i = 0; i < numvars; i++) {
                values[i] = new LinkedList<>();
            }

            //read data for all variables
            while ((line=bis.readLine())!=null){
                line =bis.readLine();
                linenum++;
                //ignores empty lines at the end of a file
                if(line.length()>0){
                    String[] contents = splitLine(line);
                    if(contents.length!=numvars){
                        throw new DataReaderException("Error on line "+linenum+" :expected "+numvars+"values, but found "+contents.length + "values instead");
                    }
                    for(int i=0;i<contents.length;i++){
                        String valToParse=contents[i];
                        values[i].add(parseDouble(valToParse,linenum));
                    }


                }
                }
            Map<String,Double[]> dataMap= new HashMap<>(numvars);

            for(int i=0;i<numvars;i++){
                Double[] vals =new Double[values[i].size()];
                values[i].toArray(vals);
                dataMap.put(varnames[i],vals);

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
