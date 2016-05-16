package ch.fhnw.project.reader;

import ch.fhnw.project.*;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;


public class LineReaderTest {
    @Test(expected = DataReaderException.class) //Should no data being present throw an exception or be treated as no data?
    public void testEmpty() throws DataReaderException{
        String data = "";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testInsufficientVariables() throws DataReaderException{
        String data = "2\nv1";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testNoDelimiter() throws DataReaderException{
        String data = "2\nv1\nv2";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testNoData() throws DataReaderException{
        String data = "2\nv1\nv2\n;";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testNoVariable() throws DataReaderException{
        String data = "0\n,\n";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testInsufficientData() throws DataReaderException{
        String data = "2\nv1\nv2\n,\n1,2";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testDifferentDataLengths() throws DataReaderException{
        String data = "2\nv1\nv2\n,\n1,2\n3";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testInvalidData() throws DataReaderException{
        String data = "2\nv1\nv2\n,\n1,2\n3,DefinitelyNotANumber";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

    @Test(expected = DataReaderException.class)
    public void testTooMuchData() throws DataReaderException{
        String data = "2\nv1\nv2\n\t\n1\t2\n3\t4\n5\t6";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }



    @Test
    public void testValidData() throws DataReaderException{
        String data = "2\nv1\nv2\n\t\n1\t2\n3\t4";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        Data pd = dr.parseContents(stream);
        assertEquals(pd.getVariableNames().size(), 2);
        String[] varNames = {"v1", "v2"};
        Double[][] expectedData = {{1d,2d},{3d,4d}};
        for(int i = 0; i < varNames.length; i++)
            assertArrayEquals(pd.getDataForVariable(varNames[i]), expectedData[i]);
    }

    @Test
    public void testValidDataAppendedNewline() throws DataReaderException{
        String data = "2\nv1\nv2\n\t\n1\t2\n3\t4\n\n";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        Data pd = dr.parseContents(stream);
        assertEquals(pd.getVariableNames().size(), 2);
        String[] varNames = {"v1", "v2"};
        Double[][] expectedData = {{1d,2d},{3d,4d}};
        for(int i = 0; i < varNames.length; i++)
            assertArrayEquals(pd.getDataForVariable(varNames[i]), expectedData[i]);
    }

    @Test
    public void testValidDataOneVar() throws DataReaderException{
        String data = "1\nv1\n\t\n1\t2";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        Data pd = dr.parseContents(stream);
        assertEquals(pd.getVariableNames().size(), 1);
        String[] varNames = {"v1"};
        Double[][] expectedData = {{1d,2d}};
        for(int i = 0; i < varNames.length; i++)
            assertArrayEquals(pd.getDataForVariable(varNames[i]), expectedData[i]);
    }

    @Test(expected = DataReaderException.class)
    public void testDuplicateVarName() throws DataReaderException{
        String data = "2\nv1\nv1\n\t\n1\t2\n3\t4";
        InputStream stream = new ByteArrayInputStream(data.getBytes());
        LineReader dr = new LineReader();
        dr.parseContents(stream);
    }

}
