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
public class TabReader extends DataReader {
    @Override

    public Data parseContents(InputStream input){
        BufferedReader bis= new BufferedReader(new InputStreamReader(input) {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });
        try {

            //get variable names
            String line = bis.readLine();
            String[] varnames = splitLine(line);

            //get number of variables
            int numvars = varnames.length;
            List<Double>[] values = new List[numvars];
            for (int i = 0; i < numvars; i++) {
                values[i] = new LinkedList<>();
            }

            //read data for all variables
            while ((line=bis.readLine())!=null){
                //ignores empty lines at the end of a file
                if(line.length()>0){
                    String[] contents = splitLine(line);
                    for(int i=0;i<contents.length;i++){
                        values[i].add(Double.parseDouble(contents[i]));
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
            e.printStackTrace();
        }

        return null;
    }

    private String[] splitLine(String line) {
        return line.split("\t");
    }
}
