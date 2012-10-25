package uk.ac.kcl.inf.aps.powersim.reporter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.kcl.inf.aps.powersim.persistence.model.SimulationDataDao;

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

  @RequestMapping("/simulations")
  public String getSimulations(Model model)
  {
    model.addAttribute("simulations", simulationDataDao.findAll());
    return "simulations";
  }

}
