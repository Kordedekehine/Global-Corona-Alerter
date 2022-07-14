package covid.covidApp.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpreadLocation {

    private String state;
    private String country;
    private int latestTotalCases;
    private int emergencyUpdates;
}
