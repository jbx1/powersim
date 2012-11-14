package uk.ac.kcl.inf.aps.powersim.analyser.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.kcl.inf.aps.powersim.persistence.model.*;
import uk.ac.kcl.inf.aps.powersim.persistence.reporting.SimulationTimeslotAggregateData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller that provides REST style data about the simuations
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 24/10/12
 *         Time: 11:54
 */
@Controller
@RequestMapping ("data")
final class DataController
{
  protected static final Logger log = LoggerFactory.getLogger(DataController.class);

  @Autowired
  private SimulationDataDao simulationDataDao;

  @Autowired
  private ApplianceDataDao applianceDataDao;

  @Autowired
  private HouseholdDataDao householdDataDao;

  @Autowired
  private AggregateLoadDataDao aggregateLoadDataDao;

  @Autowired
  private ConsumptionDataDao consumptionDataDao;

  @Autowired
  private TimeslotDataDao timeslotDataDao;


  // SimpleDateFormat is not thread-safe, so give one to each thread
  private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
  };

  @RequestMapping( method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public final List<SimulationData> getAll()
  {
    return simulationDataDao.findAll();
  }

/*  @RequestMapping(value = "/{simulationId}/timeslots", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public final List<TimeslotData> getTimeslotsForSimulation(@PathVariable("simulationId") Long simulationId)
  {
    log.info("Retrieving timeslots for simulation {}", simulationId);
    List<TimeslotData> timeslotDataList = timeslotDataDao.findAll(simulationId);
    log.info("Retrieved {} rows", timeslotDataList.size());
    return timeslotDataList;
  }*/

  @RequestMapping(value = "/{simulationId}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public final Object[][] getSimulationData(@PathVariable("simulationId") Long simulationId,
                                            @RequestParam(value="startTime", required = false) String startTime,
                                            @RequestParam(value="endTime", required = false) String endTime)
  {
    log.info("Retrieving aggregate load data for simulation {}", simulationId);

    //todo: date filtering by startTime and endTime
    List<SimulationTimeslotAggregateData> aggregateLoadDataList = aggregateLoadDataDao.getAggregateLoadDataForSimulation(simulationId, new Date(), new Date());

    Object[][] data = new Object[aggregateLoadDataList.size()][4];
    int i = 0;
    for (SimulationTimeslotAggregateData aggregateLoadData : aggregateLoadDataList)
    {
      data[i][0] = formatDate(aggregateLoadData.getTimeSlotStartTime());
      data[i][1] = aggregateLoadData.getGenerated();
      data[i][2] = aggregateLoadData.getConsumed();
      //calculate the net
      data[i][3] = aggregateLoadData.getConsumed() - aggregateLoadData.getGenerated();
      i++;
    }

    return data;
  }

  @RequestMapping(value = "/{simulationId}", method = RequestMethod.DELETE, produces = "application/json")
  @ResponseBody
  public final String deleteSimulation(@PathVariable Long simulationId)
          throws Exception
  {
    try
    {
      SimulationData simulationData = simulationDataDao.find(simulationId);
      if (simulationData == null)
      {
        log.warn("Unable to delete simulation, it does not exist.");
        throw new Exception( "Error: Unable to delete simulation, it does not exist");
      }

      if (simulationData.getActualEndTime() == null)
      {
        log.warn("Unable to delete simulation, it is still in progress.");
        throw new Exception("Error: Unable to delete simulation, it is still in progress");
      }

      log.info("Deleting data for simuation {}",simulationId);
      simulationDataDao.delete(simulationId);
      log.info("Simulation {} data deleted.", simulationId);
    }
    catch (Exception ex)
    {
      log.error("Error deleting simulation data", ex);
      throw ex;
    }

    return "Simulation " + simulationId + " Data Deleted!";
  }

  @ExceptionHandler (Exception.class)
  @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public final String handleAllExceptions(Exception ex)
  {
    log.debug("Handling Exception Return the error message.");
    return ex.getMessage();
  }

  private String formatDate(Date date)
  {
    return sdf.get().format(date);
  }
}

