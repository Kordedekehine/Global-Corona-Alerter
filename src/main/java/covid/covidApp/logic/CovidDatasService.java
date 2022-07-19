package covid.covidApp.logic;

import covid.covidApp.model.SpreadLocations;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class CovidDatasService {

    /**
     * Got data from --> https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv
     */

    private static String CORONA_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<SpreadLocations> allDatas = new ArrayList<>();

    public List<SpreadLocations> getAllDatas() {
        return allDatas;
    }

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

    /**
     * The PostConstruct annotation is used on a method that needs to be executed after dependency injection is done to
     * perform any initialization. This method MUST be invoked before the class is put into service.This annotation MUST
     * be supported on all classes that support dependency injection.
     *
     * When we annotate a method in Spring Bean with @PostConstruct annotation, it gets executed after the spring
     * bean is initialized. We can have only one method annotated with @PostConstruct annotation.
     * This annotation is part of Common Annotations API and it's part of JDK module javax.
     *
     * SCHEDULE ANNOTATION
     * It is use for enabling task scheduling for our applications, it takes effect when placed on method and inside the
     * main class
     * it is set with cron generator to be able to restart every one 1 seconds
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")//every one seconds
    public void getVirusData() throws IOException, InterruptedException{
        List<SpreadLocations> latestUpdates = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(CORONA_DATA_URL)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
       StringReader stringReader = new StringReader(httpResponse.body());
        System.out.println(httpResponse.body());
        /**
         * Here we are working with CSV file library rest Api-(we have to implement the dependency to use it)
         * We set the response inside the string reader inside the csv record(to keep track),setting the first record(output) as the header
         * WHY CSV? Getting the raw data does not look so good-We need to convert it to a proper csv file-we need to make the data structured
         * Note-we're using defaullt csv format so we're not restricted to any database doctrine
         * You can copy the flow from the csv library
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
         SpreadLocations spreadLocations = new SpreadLocations();
         spreadLocations.setState(record.get("Province/State"));
         spreadLocations.setCountry(record.get("Country/Region"));
         int latestUpdatedCases = Integer.parseInt(record.get(record.size() - 1));
         int lastUpdatedCases = Integer.parseInt(record.get(record.size() - 2));
         spreadLocations.setLatestTotalCases(latestUpdatedCases);
         spreadLocations.setEmergencyUpdates(latestUpdatedCases - lastUpdatedCases);
         latestUpdates.add(spreadLocations);
        }
        this.allDatas = latestUpdates;
    }
}
