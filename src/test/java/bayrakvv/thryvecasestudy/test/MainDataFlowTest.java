package bayrakvv.thryvecasestudy.test;

import bayrakvv.thryvecasestudy.controller.ThryveApiController;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static bayrakvv.thryvecasestudy.controller.PutsreqController.getLastWebhookData;
import static org.assertj.core.api.Assertions.assertThat;

public class MainDataFlowTest {
    @Test
    public void mainTest() {
        // GIVEN
        // Prepare request data for uploading to warehouse
        String uploadMeasurementValue = "36.0";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String startTimestamp = now.minusMinutes(181).format(formatter) + "+00:00";
        String endTimestamp = now.minusMinutes(180).format(formatter) + "+00:00";

        String jsonBody = """
            {
              "data": [
                {
                  "value": "%s",
                  "startTimestamp": "%s",
                  "endTimestamp": "%s",
                  "dynamicValueType": 5041
                }
              ]
             }""".formatted(uploadMeasurementValue, startTimestamp, endTimestamp);

        // Initialize Thryve API controller
        final ThryveApiController thryveApiController = new ThryveApiController();

        // Get Thryve user token
        String accessToken = thryveApiController.getAccessToken();

        // WHEN
        // Upload data to Thryve warehouse
        thryveApiController.uploadEpochDataFromWebApp(accessToken, jsonBody);

        // THEN
        // Check within 10 seconds for a new data point
        // in the Health Data API response for the specified time period,
        // otherwise interrupt the test
        Response thryveData;
        List<?> thryveDataPoints;
        long newDataPointRequestTimeout = System.currentTimeMillis() + 10000;

        do {
            thryveData = thryveApiController.
                retrieveDataFromHealthDataApi(accessToken, startTimestamp, endTimestamp);
            thryveDataPoints = thryveData.path("[0].dataSources[0].data");

            if (System.currentTimeMillis() > newDataPointRequestTimeout) {
                throw new RuntimeException(
                    "Timeout: there is still no data in the provided timestamp after 10 seconds");
            }
        } while (thryveDataPoints == null);

        // Get values from the Health Data API response
        String thryveMeasurementValue = thryveData.path("[0].dataSources[0].data[0].value");
        int thryveValueType = thryveData.path("[0].dataSources[0].data[0].dynamicValueType");
        String thryveStartDate = thryveData.path("[0].dataSources[0].data[0].startTimestamp");
        String thryveEndDate = thryveData.path("[0].dataSources[0].data[0].endTimestamp");

        // Get last data from the webhook
        Response webhookData = getLastWebhookData();

        // Get values from the webhook response
        int webhookValueType = webhookData.path("sourceUpdate.'/v5/dynamicEpochValues'.dynamicValueTypes[0]");
        String webhookStartDate = webhookData.path("sourceUpdate.'/v5/dynamicEpochValues'.startTimestamp");
        String webhookEndDate = webhookData.path("sourceUpdate.'/v5/dynamicEpochValues'.endTimestamp");

        // Make assertions
        assertThat(thryveMeasurementValue).isEqualTo(uploadMeasurementValue);
        assertThat(thryveValueType).isEqualTo(webhookValueType);
        assertThat(thryveStartDate).isEqualTo(webhookStartDate);
        assertThat(thryveEndDate).isEqualTo(webhookEndDate);
    }
}
