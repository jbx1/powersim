package uk.ac.kcl.inf.aps.powersim.configuration;

/**
* Date: 09/07/13
* Time: 16:08
*
* @author: josef
*/
public class PolicyHouseholdData
{
  String category;
  int quantity;

  public PolicyHouseholdData()
  {
  }

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public void setQuantity(int quantity)
  {
    this.quantity = quantity;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("PolicyHouseholdData");
    sb.append("{category='").append(category).append('\'');
    sb.append(", quantity=").append(quantity);
    sb.append('}');
    return sb.toString();
  }
}
