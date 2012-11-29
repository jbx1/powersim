package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 28/11/12
 *         Time: 11:34
 */
public class ApplianceKeyTuple implements Comparable<ApplianceKeyTuple>
{
  //todo: remove this class, just take type-subtype as one string

  String type;
  String subtype;

  public ApplianceKeyTuple(String type, String subtype)
  {
    this.type = type;
    this.subtype = subtype;
  }

  public String getType()
  {
    return type;
  }

  public String getSubtype()
  {
    return subtype;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append(type).append("-").append(subtype);
    return sb.toString();
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    ApplianceKeyTuple that = (ApplianceKeyTuple) o;

    if (subtype != null ? !subtype.equals(that.subtype) : that.subtype != null)
    {
      return false;
    }
    if (type != null ? !type.equals(that.type) : that.type != null)
    {
      return false;
    }

    return true;
  }

  @Override
  public int compareTo(ApplianceKeyTuple applianceKeyTuple)
  {
    if (!applianceKeyTuple.getType().equals(this.getType()))
    {
      return this.getType().compareTo(applianceKeyTuple.getType());
    }
    else
    {
      return this.getSubtype().compareTo(applianceKeyTuple.getSubtype());
    }
  }
}
