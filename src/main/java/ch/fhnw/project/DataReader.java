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

    public abstract Data parseContents(InputStream input) throws DataReaderException;


    public Data parseContents(File input) throws DataReaderException {
        try {
            FileInputStream fileInputStream = new FileInputStream(input);
            return parseContents(fileInputStream);

        } catch (FileNotFoundException e) {
            throw new DataReaderException("Could not read file: " + e.toString());
        }
    }


    //helper function

    static double parseDouble(String s, long lineNum) throws DataReaderException {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new DataReaderException("Error on line " + lineNum + ": Unable to parse " + s + " as a Double");
        }
    }
    static int parseInt(String s, long lineNum) throws DataReaderException{
        try {
            return Integer.parseInt(s);
        }catch(NumberFormatException e){
            throw new DataReaderException("Error on line "+ lineNum +": Unable to parse "+ s +" as a integer");
        }
    }
}
