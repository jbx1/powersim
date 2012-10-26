package uk.ac.kcl.inf.aps.powersim.reporter.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  private TimeslotDataDao timeslotDataDao;

  @Autowired
  private AggregateLoadDataDao aggregateLoadDataDao;

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

  private String formatDate(Date date)
  {
    return sdf.get().format(date);
  }

}

