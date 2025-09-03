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
    private boolean isSuccess = false;

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public Metadata() {}

    public Metadata(double threshold, boolean verbose, boolean isKilled, Execution execution) {
        this.threshold = threshold > 0 ? threshold : 100.0;
        this.verbose = verbose;
        this.isKilled = isKilled;

        if (execution.getExecutionItems() != null
                && !execution.getExecutionItems().isEmpty()) {
            this.totalCount = execution.getExecutionItems().size();

            for (ExecutionItems testCase : execution.getExecutionItems()) {
                if (testCase == null || testCase.getStatus() == null) {
                    continue;
                }

                switch (testCase.getStatus().toUpperCase()) {
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
                        System.out.println("Unexpected status: " + testCase.getStatus());
                        break;
                }
            }

            // Calculate pass percentage
            this.passedPercent = totalCount > 0 ? ((double) passedCount / totalCount) * 100.0 : 0.0;
            this.failedPercent = totalCount > 0 ? ((double) failedCount / totalCount) * 100.0 : 0.0;
            this.executedPercent = totalCount > 0 ? ((double) (passedCount + failedCount) / totalCount) * 100.0 : 0.0;

            // Check against threshold
            this.isSuccess = this.passedPercent >= this.threshold;

            // Log values if verbose mode is enabled
            if (verbose) {
                System.out.println("********** METADATA DETAILS **********");
                System.out.println("Total Test Cases  : " + totalCount);
                System.out.println("Passed            : " + passedCount);
                System.out.println("Failed            : " + failedCount);
                System.out.println("Skipped           : " + skippedCount);
                System.out.println("Pass %            : " + this.passedPercent);
                System.out.println("Threshold         : " + this.threshold);
                System.out.println("Threshold Met     : " + this.isSuccess);
                System.out.println("Is Killed         : " + this.isKilled);
                System.out.println("**************************************");
            }

        } else {
            this.totalCount = 0;
            this.isSuccess = false;
            if (verbose) {
                System.out.println("Testcases list is null or empty.");
            }
        }
    }

    //    public Metadata(double threshold, boolean verbose, boolean isKilled, Execution execution) {
    //        this.threshold = threshold > 0 ? threshold : 100.0;
    //        this.verbose = verbose;
    //        this.isKilled = isKilled;
    //
    //        if (execution.getTestcases() != null && !execution.getTestcases().isEmpty()) {
    //            this.totalCount = execution.getTestcases().size();
    //
    //            for (ExecutionItem testCase : execution.getTestcases()) {
    //                if (testCase == null || testCase.getStatus() == null) {
    //                    continue;
    //                }
    //
    //                switch (testCase.getStatus().toUpperCase()) {
    //                    case "PASSED":
    //                        this.passedCount++;
    //                        break;
    //                    case "FAILED":
    //                        this.failedCount++;
    //                        break;
    //                    case "SKIPPED":
    //                        this.skippedCount++;
    //                        break;
    //                    default:
    //                        System.out.println("Unexpected status: " + testCase.getStatus());
    //                }
    //            }
    //
    //            // Calculate the pass percentage
    //            double passPercentage = ((double) passedCount / totalCount) * 100;
    //
    //            // Log it if verbose is on
    //            if (verbose) {
    //                System.out.println("Total: " + totalCount);
    //                System.out.println("Passed: " + passedCount);
    //                System.out.println("Pass %: " + passPercentage);
    //                System.out.println("Threshold: " + threshold);
    //            }
    //
    //            // Check if threshold is met
    //            this.isSuccess = passPercentage >= this.threshold;
    //
    //        } else {
    //            this.totalCount = 0;
    //            System.out.println("Testcases list is null or empty.");
    //            this.isSuccess = false;
    //        }
    //    }

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

    public void setState(String state) {
        this.state = state;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
