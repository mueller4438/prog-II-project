package ch.fhnw.project;

import java.util.Set;

/**
 * Created by Müller on 13.05.2016.
 */
public interface Data {
    Set<String> getVariableNames();

    Double[] getDataForVariable(String name);
}

