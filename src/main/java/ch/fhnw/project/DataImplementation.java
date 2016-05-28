package ch.fhnw.project;

import java.util.Map;
import java.util.Set;

/**
 * Created by Müller on 13.05.2016.
 */
public class DataImplementation implements Data {

        private final Map<String, Double[]> dataMap;
        public DataImplementation(Map<String, Double[]> data){
            dataMap  = data;
        }

    @Override
    public Set<String>getVariableNames() {
        return dataMap.keySet();
    }

    @Override
    public Double[] getDataForVariable(String variableName) {
        return dataMap.get(variableName);
    }
}


