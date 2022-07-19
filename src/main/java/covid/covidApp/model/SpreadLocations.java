package covid.covidApp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class SpreadLocations {

    private String state;
    private String country;
    private int latestTotalCases;
    private int emergencyUpdates;
}
