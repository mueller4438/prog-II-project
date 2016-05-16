package ch.fhnw.project;

import java.io.File;

/**
 * Created by Müller on 16.05.2016.
 */
public class Start {
    public static void main(String args[])throws DataReaderException{

            File f;
            f = new File("src/main/resources/helvetia.txt");
            Data mydata = new TabReader().parseContents(f);



    }

    }

