package uk.ac.kcl.inf.aps.powersim.policies.simpleonoff;

/**
 * The Simple Appliance Type simulates the simple on/off model with each appliance having a
 * probability P of going ON, and 1-P of going OFF.
 *
 * Each appliance can have hourly boundaries within which the appliance can go ON.
 *
 * An appliance can also be always ON, which will always treat the appliance as ON
 * without going through the random switching process. This is ideal for appliances such as Refrigerators that
 * run continuously.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 19/10/12
 *         Time: 16:03
 */
public enum SimpleApplianceType
{
  LAPTOP (50, false, 17, 1, 70),
  FRIDGE (800, true, 0, 0, 100),
  AQUARIUM (500, true, 0, 0, 100),
  KETTLE (1200, false, 0, 0, 5),
  TV (110, false, 17, 0, 90),
  TOASTER (1000, false, 6, 9, 40),
  OVEN (1200, false, 17, 20, 30),
  WASHING_MACHINE (600, false, 19, 22, 15),
  DISHWASHER (1500, false, 21, 23, 20),
  LIGHT (10, false, 0, 0, 60),
  PV (-1000, false, 8, 17, 70),
  WIND_GENERATOR (-800, false, 0, 0, 40);


  /**
   * The typical wattage of the appliance.
   */
  private long wattage;

  /**
   * Whether the appliance is always on (like refrigerators)
   * Only applicable if alwaysOn is false.
   */
  private boolean alwaysOn = false;

  /**
   * The start of the time period within which the appliance may go on.
   */
  private int startHour = 0;

  /**
   * The end of the time period within which the appliance may go on.
   * Only applicable if alwaysOn is false.
   */
  private int endHour = 0;

  /**
   * The probability percentage the device goes On during a specific time slot, during the time between startHour and endHour.
   * If the device is OFF, it will go ON based on this probability.
   * If the device is ON, it will go OFF based on 1-probability.
   */
  private int probabilityPercOn = 50;

  private SimpleApplianceType(long wattage, boolean alwaysOn, int startHour, int endHour, int probabilityPercOn)
  {
    this.wattage = wattage;
    this.alwaysOn = alwaysOn;
    this.startHour = startHour;
    this.endHour = endHour;
    this.probabilityPercOn = probabilityPercOn;
  }

  public long getWattage()
  {
    return wattage;
  }

  public boolean isAlwaysOn()
  {
    return alwaysOn;
  }

  public int getStartHour()
  {
    return startHour;
  }

  public int getEndHour()
  {
    return endHour;
  }

  public int getProbabilityPercOn()
  {
    return probabilityPercOn;
  }

  public String getDescription()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("SimpleApplianceType");
    sb.append("{").append(this.name());
    sb.append(": wattage=").append(wattage);
    sb.append(", alwaysOn=").append(alwaysOn);
    sb.append(", startHour=").append(startHour);
    sb.append(", endHour=").append(endHour);
    sb.append(", probabilityPercOn=").append(probabilityPercOn);
    sb.append('}');
    return sb.toString();
  }
}
