package com.simplifyqa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplifyqa.model.Execution;
import com.simplifyqa.model.IExecution;
import com.simplifyqa.model.Testcase;
import java.util.List;
import java.util.logging.Logger;

public class SimplifyQAUtils {
    public IExecution createExecutionFromApiResponse(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Deserialize JSON into Execution object (which implements IExecution)
            Execution execution = mapper.readValue(responseBody, Execution.class); // No need for casting

            return execution; // Return Execution object directly, as it implements IExecution
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printStatus(Execution execObj) {
        Logger logger = Logger.getLogger(Execution.class.getName());
        try {
            logger.info(
                    "**************************************  EXECUTION STATUS  **************************************%n");

            logger.info(String.format(
                    "EXECUTION PERCENTAGE: %.2f %%\t| EXECUTED %d of %d items.",
                    execObj.getMetadata().getExecutedPercent(),
                    execObj.getMetadata().getExecutedCount(),
                    execObj.getMetadata().getTotalCount()));

            logger.info(String.format(
                    "THRESHOLD PERCENTAGE: %.2f %%\t| REACHED %.2f %% of %.2f %% %n",
                    execObj.getMetadata().getThreshold(),
                    execObj.getMetadata().getFailedPercent(),
                    execObj.getMetadata().getThreshold()));

            logger.info(String.format(
                    "PASSED PERCENTAGE: %.2f %%\t| PASSED %d of %d items.",
                    execObj.getMetadata().getPassedPercent(),
                    execObj.getMetadata().getPassedCount(),
                    execObj.getMetadata().getTotalCount()));

            logger.info(String.format(
                    "FAILED PERCENTAGE: %.2f %%\t| FAILED %d of %d items.",
                    execObj.getMetadata().getFailedPercent(),
                    execObj.getMetadata().getFailedCount(),
                    execObj.getMetadata().getTotalCount()));

            logger.info(String.format(
                    "SKIPPED PERCENTAGE: %.2f %%\t| SKIPPED %d of %d items.%n",
                    execObj.getMetadata().getSkippedPercent(),
                    execObj.getMetadata().getSkippedCount(),
                    execObj.getMetadata().getTotalCount()));

            logger.info(String.format(
                    "EXEC ID: %s \t| EXECUTION STATUS: %s \t| EXECUTION RESULT: %s",
                    execObj.getCode(),
                    execObj.getStatus() != null ? execObj.getStatus() : "N/A",
                    execObj.getResult() != null ? execObj.getResult() : "N/A"));

            logger.info(String.format(
                    "PARENT ID: %s \t| PARENT NAME: %s \t| PARENT TYPE: %s%n",
                    execObj.getExecutionTypeCode() != null ? execObj.getExecutionTypeCode() : "N/A",
                    execObj.getExecutionTypeName() != null ? execObj.getExecutionTypeName() : "N/A",
                    execObj.getType() != null ? execObj.getType() : "N/A"));

            List<Testcase> testcases = execObj.getTestcases();
            if (testcases != null && !testcases.isEmpty()) {
                for (Testcase testcase : testcases) {
                    logger.info(String.format(
                            "TESTCASE ID: %d | TESTCASE STATUS: %s | TESTCASE NAME: %s",
                            testcase.getTestcaseId(),
                            testcase.getStatus() != null ? testcase.getStatus() : "N/A",
                            testcase.getTestcaseName() != null ? testcase.getTestcaseName() : "N/A"));
                }
            }

            logger.info(
                    "************************************************************************************************%n");
        } catch (Exception e) {
            logger.severe("Error caused while printing execution status!!!");
            e.printStackTrace();
        }
    }
}
