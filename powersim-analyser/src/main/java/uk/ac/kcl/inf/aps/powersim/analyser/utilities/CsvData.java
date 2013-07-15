package uk.ac.kcl.inf.aps.powersim.analyser.utilities;

/**
 * Date: 15/07/13
 * Time: 15:43
 *
 * @author: josef
 */
public class CsvData
{
  private String filename;
  private String[] headings;
  private Object[][] data;

  public CsvData(String filename, String[] headings, Object[][] data)
  {
    this.filename = filename;
    this.headings = headings;
    this.data = data;
  }

  public String getFilename()
  {
    return filename;
  }

  public void setFilename(String filename)
  {
    this.filename = filename;
  }

  public Object[][] getData()
  {
    return data;
  }

  public void setData(Object[][] data)
  {
    this.data = data;
  }

  public String[] getHeadings()
  {
    return headings;
  }

  public void setHeadings(String[] headings)
  {
    this.headings = headings;
  }
}
