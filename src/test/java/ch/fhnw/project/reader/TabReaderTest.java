package ch.fhnw.project.reader;


import ch.fhnw.project.*;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;

public class TabReaderTest {
    @Test(expected = DataReaderException.class) //Should no data being present throw an exception or be treated as no data?
    public void testEmpty() throws DataReaderException{
        String data = "";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class) //Should no data being present throw an exception or be treated as no data?
    public void testNoData() throws DataReaderException{
        String data = "v1\tv2";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class) //Should no data being present throw an exception or be treated as no data?
    public void testNoVarName() throws DataReaderException{
        String data = "\n";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class) //Should no data being present throw an exception or be treated as no data?
    public void testNoVarName2() throws DataReaderException{
        String data = "\t\n";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testDuplicateVarName() throws DataReaderException{
        String data = "v1\tv1";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    //our data is invalid, an exception should be thrown
    @Test(expected = DataReaderException.class)
    public void testNotEnoughData() throws DataReaderException{
        String data = "v1\tv2\n1.0";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testTooMuchData() throws DataReaderException{
        String data = "v1\tv2\n1.0\t2\t3";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testInvalidData() throws DataReaderException{
        String data = "v1\tv2\n1\tThisIsNotANumber";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        dr.parseContents(stream);
    }

    @Test
    public void testValidData() throws DataReaderException{
        String data = "v1\tv2\n1\t3\n2\t4";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        Data pd = dr.parseContents(stream);
        assertEquals(pd.getVariableNames().size(), 2);
        String[] varNames = {"v1", "v2"};
        Double[][] expectedData = {{1d,2d},{3d,4d}};
        for(int i = 0; i < varNames.length; i++)
            assertArrayEquals(pd.getDataForVariable(varNames[i]), expectedData[i]);
    }

    @Test
    public void testValidDataEmptyLine() throws DataReaderException{
        String data = "v1\tv2\n1\t3\n2\t4\n\n\n";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        Data pd = dr.parseContents(stream);
        assertEquals(pd.getVariableNames().size(), 2);
        String[] varNames = {"v1", "v2"};
        Double[][] expectedData = {{1d,2d},{3d,4d}};
        for(int i = 0; i < varNames.length; i++)
            assertArrayEquals(pd.getDataForVariable(varNames[i]), expectedData[i]);
    }

    @Test
    public void testValidDataSinge() throws DataReaderException{
        String data = "v1\tv2\n1\t3";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        Data pd = dr.parseContents(stream);
        assertEquals(pd.getVariableNames().size(), 2);
        String[] varNames = {"v1", "v2"};
        Double[][] expectedData = {{1d},{3d}};
        for(int i = 0; i < varNames.length; i++)
            assertArrayEquals(pd.getDataForVariable(varNames[i]), expectedData[i]);
    }

    @Test
    public void testValidDataOneVar() throws DataReaderException{
        String data = "v1\n1\n2";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        TabReader dr = new TabReader();
        Data pd = dr.parseContents(stream);
        assertEquals(pd.getVariableNames().size(), 1);
        String[] varNames = {"v1"};
        Double[][] expectedData = {{1d,2d}};
        for(int i = 0; i < varNames.length; i++)
            assertArrayEquals(pd.getDataForVariable(varNames[i]), expectedData[i]);
    }




}
