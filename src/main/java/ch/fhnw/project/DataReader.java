package ch.fhnw.project;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by Müller on 13.05.2016.
 */
public abstract class DataReader {

    public abstract Data parseContents(InputStream input);







    public  Data parseContents(File input) throws DataReaderException {
        try {
            FileInputStream fis = new FileInputStream(input);
            return parseContents(fis);

        } catch (FileNotFoundException e) {
            throw new DataReaderException("Could not read file: "+e.toString());
        }
    }
}
