package covid.covidApp.controller;

import covid.covidApp.logic.CovidDatasService;
import covid.covidApp.model.SpreadLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CovidController {


   @Autowired
    CovidDatasService covidDatasService;

   @GetMapping("/")
    public String covid(Model model){
       List<SpreadLocation> collectedData = covidDatasService.getAllDatas();
//       int totalCasesAlert = collectedData.stream().mapToInt(SpreadLocation::getLatestTotalCases).sum();
//       int totalNewCases = collectedData.stream().mapToInt(SpreadLocation::getEmergencyUpdates).sum();
       int totalCasesAlert = collectedData.stream().mapToInt(data -> data.getLatestTotalCases()).sum();
       int totalNewCases = collectedData.stream().mapToInt(data -> data.getEmergencyUpdates()).sum();
       model.addAttribute("spreadLocation",collectedData);
       model.addAttribute("totalCasesAlert",totalCasesAlert);
       model.addAttribute("totalNewCases",totalNewCases);
       return "covid";
    }
}
