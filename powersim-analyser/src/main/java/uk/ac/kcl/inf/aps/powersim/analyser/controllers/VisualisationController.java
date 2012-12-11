package uk.ac.kcl.inf.aps.powersim.analyser.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.kcl.inf.aps.powersim.persistence.model.*;

import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 25/10/12
 *         Time: 14:32
 */
@Controller
public class VisualisationController
{
  protected static final Logger log = LoggerFactory.getLogger(VisualisationController.class);

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


  @RequestMapping("/")
  public String getSimulations(@RequestParam(value="message", required = false) String message, @RequestParam(value="error_message", required = false) String error_message, Model model)
  {
    if (message != null)
    {
      model.addAttribute("message", message);
    }
    if (error_message != null)
    {
      model.addAttribute("error_message", error_message);
    }

    model.addAttribute("simulations", simulationDataDao.findAll());
    return "simulations";
  }

  @RequestMapping(value = "/{simulationId}", method = RequestMethod.GET)
  public String getSimulations(@PathVariable Long simulationId, Model model)
  {
    SimulationData simulationData = simulationDataDao.find(simulationId);
    model.addAttribute("simulation", simulationData);

    int households = householdDataDao.getHouseholdCountForSimulation(simulationId);
    model.addAttribute("households", households);

    int appliances = applianceDataDao.getApplianceCountForSimulation(simulationId);
    model.addAttribute("appliances", appliances);

    return "simulation";
  }

  @RequestMapping(value = "/{simulationId}/households", method = RequestMethod.GET)
  public String getHouseholds(@PathVariable Long simulationId,
                              @RequestParam(value = "actualPage", required = false, defaultValue = "0") int offsetPage,
                              @RequestParam(value = "elementsPerPage", required = false, defaultValue = "0") int limit,
                              @RequestParam(value = "policy", required = false) String policy,
                              @RequestParam(value = "category", required = false) String category,
                              Model model)
  {
    log.debug("Getting list of households for simulation {} offsetPage {} limit {}", new Object[]{simulationId, offsetPage, limit});

    model.addAttribute("simulationId", simulationId);

    List<HouseholdData> households;

    int offset = offsetPage * limit;
    if (policy == null && category == null)
    {
      households = householdDataDao.getHouseholdsForSimulation(simulationId, offset, limit);
    }
    else if (category == null)
    {
      log.debug("Filtering by policy {}", policy);
      households = householdDataDao.getHouseholdsForPolicy(simulationId, policy, offset, limit);
    }
    else if (policy == null)
    {
      log.debug("Filtering by category {}", category);
      households = householdDataDao.getHouseholdsForCategory(simulationId, category, offset, limit);
    }
    else
    {
      log.debug("Filtering by policy {} and category {}", policy, category);
      households = householdDataDao.getHouseholdsForPolicyAndCategory(simulationId, policy, category, offset, limit);
    }

    model.addAttribute("households", households);

    return "householdList";
  }
}
