package uk.ac.kcl.inf.aps.powersim.analyser.utilities;

import au.com.bytecode.opencsv.CSVWriter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Date: 12/07/13
 * Time: 18:34
 *
 * @author: josef
 */
public class CsvMessageConverter extends AbstractHttpMessageConverter<Object>
{
  public static final MediaType MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));

  public CsvMessageConverter()
  {
    super(MEDIA_TYPE);
  }

  protected boolean supports(Class<?> clazz)
  {
    return true;
  }

  @Override
  protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage)
          throws IOException, HttpMessageNotReadableException
  {
    //todo: write a csv reader
    return null;
  }

  protected void writeInternal(Object response, HttpOutputMessage output) throws IOException, HttpMessageNotWritableException
  {
    output.getHeaders().setContentType(MEDIA_TYPE);
    Object data = response;
    String[] headings = null;
    if (response instanceof CsvData)
    {
      CsvData csvdata = (CsvData) response;
      output.getHeaders().set("Content-Disposition", "attachment; filename="+csvdata.getFilename());
      data = csvdata.getData();
      headings = csvdata.getHeadings();
    }
    else
    {
      output.getHeaders().set("Content-Disposition", "attachment; filename=data.csv");
    }
    OutputStream out = output.getBody();
    CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',');
    if (headings != null)
    {
      writer.writeNext(headings);
    }

    if (data instanceof String[][])
    {
      String[][] objects = (String[][]) data;
      for (String[] row : objects)
      {
        writer.writeNext(row);
      }
    }
    else if (data instanceof String[])
    {
      String[] row = (String[]) data;
      writer.writeNext(row);
    }
    else if (data instanceof Object[][])
    {
      Object[][] objects = (Object[][]) data;
      for (Object[] row : objects)
      {
        String[] rowStrings = new String[row.length];
        for (int i = 0; i < row.length; i++)
        {
          rowStrings[i] = row[i].toString();
        }

        writer.writeNext(rowStrings);
      }
    }
    else if (data instanceof Object[])
    {
      Object[] row = (Object[]) data;
      String[] rowStrings = new String[row.length];
      for (int i = 0; i < row.length; i++)
      {
        rowStrings[i] = row[i].toString();
      }

      writer.writeNext(rowStrings);
    }

    writer.close();
  }

}