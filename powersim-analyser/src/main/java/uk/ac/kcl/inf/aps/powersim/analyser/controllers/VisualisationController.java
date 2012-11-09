package uk.ac.kcl.inf.aps.powersim.analyser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.kcl.inf.aps.powersim.persistence.model.*;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 25/10/12
 *         Time: 14:32
 */
@Controller
public class VisualisationController
{
  @Autowired
  private SimulationDataDao simulationDataDao;

  @Autowired
  private ApplianceDataDao applianceDataDao;

  @Autowired
  private HouseholdDataDao householdDataDao;

  @RequestMapping("/")
  public String getSimulations(Model model)
  {
    model.addAttribute("simulations", simulationDataDao.findAll());
    return "simulations";
  }

  @RequestMapping("/{simulationId}")
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




}
