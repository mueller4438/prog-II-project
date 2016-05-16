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
    public Data parseContents(InputStream input){
        BufferedReader bis= new BufferedReader(new InputStreamReader(input));
        try {
            String line = bis.readLine();

            //get number of variables
            int numvar= Integer.parseInt(line);

            //get variable names
            String[] varnames= new String[numvar];
            for(int i=0;i<numvar;i++){
                line = bis.readLine();
                varnames[i]=line;
            }
            //get Delimiter
            String delimiter= bis.readLine();

            //read Data for all variables
            Map<String,Double[]> dataMap= new HashMap<>(numvar);
            for(int i=0;i<numvar;i++) {
                line = bis.readLine();

                List<Double> data = new LinkedList<>();

                for (String s : linesplit(line, delimiter)) {
                    data.add(Double.parseDouble(s));

                }
                Double[] vals = new Double[data.size()];
                data.toArray(vals);
                dataMap.put(varnames[i], vals);
            }
                return  new DataImplementation(dataMap);












        } catch (IOException e) {
            e.printStackTrace();
        }


    return null;
}

    private  String[] linesplit(String line, String delimiter) {
        return line.split(delimiter);
    }

}
