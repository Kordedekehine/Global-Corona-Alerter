package covid.covidApp.logic;

import covid.covidApp.model.SpreadLocation;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Service
public class CovidDatasService {

    /**
     * Got data from --> https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv
     */

    private static String CORONA_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<SpreadLocation> allStats = new ArrayList<>();

    /**
     * This method particularly get data from the data source we declared above
     * Java I0 EXCEPTION OCCURS When the input or output operation fails.In our case, We're outputting from a data
     * Interrupted exception particularly occurs when a particular thread is interrupted while threading or running
     * @throws IOException
     * @throws InterruptedException
     */

    /**
     * List all in the spreads location to check latest updates
     * HttpClient is basically for sending message and receiving response, same web function
     * HttpRequest is used for sending request basically-its applicable to POST-GET-PATCH-DELETE- then we create the request builder and reference the data we're getting from
     * HttpResponse is used for returning response to the request sent
     * Then we create a StringReader to read our response given by HttpResponse
     * @throws IOException
     * @throws InterruptedException
     */
    public void getVirusData() throws IOException, InterruptedException{
        List<SpreadLocation> latestUpdates = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(CORONA_DATA_URL)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
        StringReader stringReader = new StringReader(httpResponse.body());
        /**
         * Here we are working with CSV file rest Api-(we have to implement the dependency to use it)
         * We set the response inside the string reader inside the csv record(to keep track),setting the first record(output) as the header
         * Note-we're using defaullt csv format so we're not restricted to any database doctrine
         */
        Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        /**
         * Now that we've recorded the responses generated--then we loop through each and every record-for each
         * we initialize (call) the spreadLocation class then we set the objects to get the datas
         * we've gotten the latestUpdatedCases and the lastUpdatedCases
         * then we set the latest total cases to the last updated cases
         * then we go ahead to set the emergency updates(fresh compiled updates) to the difference between the latest updated cases and the last updated cases
         * then we affirm the latest updates
         */
        for (CSVRecord record : csvRecords) {
         SpreadLocation spreadLocation = new SpreadLocation();
         spreadLocation.setState(record.get("Jurisdiction/State"));
         spreadLocation.setCountry(record.get("Region/Country"));
         int latestUpdatedCases = Integer.parseInt(record.get(record.size() - 1));
         int lastUpdatedCases = Integer.parseInt(record.get(record.size() - 2));
         spreadLocation.setLatestTotalCases(latestUpdatedCases);
         spreadLocation.setEmergencyUpdates(latestUpdatedCases - lastUpdatedCases);
         latestUpdates.add(spreadLocation);
        }
    }
}
