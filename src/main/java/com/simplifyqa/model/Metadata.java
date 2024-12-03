package com.simplifyqa.model;

public class Metadata implements IMetadata {
    private double threshold;
    private boolean isKilled;
    private boolean verbose;
    private int totalCount;
    private int passedCount;
    private double passedPercent;
    private int failedCount;
    private double failedPercent;
    private int skippedCount;
    private double skippedPercent;
    private int executedCount;
    private double executedPercent;
    private String state;
    private String result;

    public Metadata() {}

    public Metadata(double threshold, boolean verbose, boolean isKilled, Execution execution) {
        this.threshold = threshold > 0 ? threshold : 100.0;
        this.verbose = verbose;
        this.isKilled = isKilled;

        // Initialize counts
        this.totalCount = execution.getTestcases().size();
        this.passedCount = 0;
        this.failedCount = 0;
        this.skippedCount = 0;

        // Calculate counts based on test case statuses
        for (Testcase testCase : execution.getTestcases()) {
            String status = testCase.getStatus();
            if (status != null) {
                switch (status.toUpperCase()) {
                    case "PASSED":
                        this.passedCount++;
                        break;
                    case "FAILED":
                        this.failedCount++;
                        break;
                    case "SKIPPED":
                        this.skippedCount++;
                        break;
                    default:
                        System.out.println("Unexpected status: " + status); // Example: Logging the unexpected status
                        break;
                }
            }
        }

        // Compute percentages
        this.executedCount = this.passedCount + this.failedCount + this.skippedCount;
        this.passedPercent = calculatePercentage(this.passedCount, this.totalCount);
        this.failedPercent = calculatePercentage(this.failedCount, this.totalCount);
        this.skippedPercent = calculatePercentage(this.skippedCount, this.totalCount);
        this.executedPercent = calculatePercentage(this.executedCount, this.totalCount);

        // Set state and result from execution
        this.state = execution.getStatus();
        this.result = execution.getResult();
    }

    private double calculatePercentage(int count, int total) {
        return total > 0 ? (count * 100.0) / total : 0.0;
    }

    // Getters
    public double getThreshold() {
        return threshold;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPassedCount() {
        return passedCount;
    }

    public double getPassedPercent() {
        return passedPercent;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public double getFailedPercent() {
        return failedPercent;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public double getSkippedPercent() {
        return skippedPercent;
    }

    public int getExecutedCount() {
        return executedCount;
    }

    public double getExecutedPercent() {
        return executedPercent;
    }

    public String getState() {
        return state;
    }

    public String getResult() {
        return result;
    }
}
