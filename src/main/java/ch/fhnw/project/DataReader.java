package ch.fhnw.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Müller on 13.05.2016.
 */
public abstract class DataReader {

    public parseContents(File input){
        try {
            FileInputStream fis = new FileInputStream(input);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
}
    }
}
