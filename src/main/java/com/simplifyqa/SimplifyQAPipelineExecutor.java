package com.simplifyqa;

import com.simplifyqa.model.Execution;
import com.simplifyqa.service.SimplifyQAService;
import com.simplifyqa.utils.SimplifyQAUtils;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.Secret;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SimplifyQAPipelineExecutor extends Builder implements SimpleBuildStep {
    private final String pipelineId;
    private final String apiUrl;
    private final Secret apiKey;
    private final double threshold;
    private final int maxRetries;
    private final int retryInterval;
    private final int apiTimeout;

    @DataBoundConstructor
    public SimplifyQAPipelineExecutor(
            String apiUrl,
            String apiKey,
            String pipelineId,
            double threshold,
            int maxRetries,
            int retryInterval,
            int apiTimeout) {
        this.apiUrl = apiUrl;
        this.apiKey = Secret.fromString(apiKey);
        this.pipelineId = pipelineId;
        this.threshold = threshold;
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
        this.apiTimeout = apiTimeout;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey.getPlainText();
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public double getThreshold() {
        return threshold;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public int getApiTimeout() {
        return apiTimeout;
    }

    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener)
            throws InterruptedException, IOException {
        listener.getLogger().println("********** FETCHING VALUES **********");
        listener.getLogger().println("********** SIMPLIFYQA PIPELINE EXECUTOR **********");
        listener.getLogger().println("Pipeline Execution Started...");
        listener.getLogger().println("API URL: " + apiUrl);
        listener.getLogger().println("Pipeline ID: " + pipelineId);
        listener.getLogger().println("Threshold percentage: " + threshold);
        listener.getLogger().println("MaxRetries count:" + maxRetries);
        listener.getLogger().println("Retry Interval:" + retryInterval);

        Execution response = SimplifyQAService.startPipelineExecution(apiUrl, getApiKey(), pipelineId, listener);
        if (response == null) {
            listener.getLogger().println("Failed to start execution.");
            run.setResult(Result.FAILURE);
            return;
        }

        Execution execObj = new Execution(response, threshold);

        listener.getLogger().println("Execution started with status: " + execObj.getStatus());
        SimplifyQAUtils.printStatus(execObj);

        try {
            Execution temp = null;
            int retryCount = 0;
            while ("INPROGRESS".equalsIgnoreCase(execObj.getStatus())
                    || "NOTEXECUTED".equalsIgnoreCase(execObj.getStatus())) {
                double failedPercent = execObj.getMetadata().getFailedPercent();
                if (failedPercent >= threshold) {
                    listener.getLogger().println("Threshold reached (" + threshold + "%). Stopping execution...");
                    BigDecimal failPercent = BigDecimal.valueOf(
                                    execObj.getMetadata().getFailedPercent())
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal passedPercent = BigDecimal.valueOf(
                                    execObj.getMetadata().getPassedPercent())
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal executedPercent = BigDecimal.valueOf(
                                    execObj.getMetadata().getExecutedPercent())
                            .setScale(2, RoundingMode.HALF_UP);
                    int passedCount = execObj.getMetadata().getPassedCount();
                    int failedCount = execObj.getMetadata().getFailedCount();
                    int totalCount = execObj.getMetadata().getTotalCount();
                    listener.getLogger().println("Executed Percent: " + executedPercent);
                    listener.getLogger().println("Passed Percent: " + passedPercent);
                    listener.getLogger().println("Failed Percent: " + failPercent);
                    listener.getLogger().println("Passed Count: " + passedCount);
                    listener.getLogger().println("Failed Count: " + failedCount);
                    listener.getLogger().println("Total Count: " + totalCount);
                    String killType = computeKillType(execObj);
                    SimplifyQAService.stopExecution(
                            apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId(), killType);
                    run.setResult(Result.FAILURE);
                    return;
                }

                Execution statusResponse = null;
                retryCount = 0;
                int effectiveMaxRetries = maxRetries > 0 ? maxRetries : 5;

                //                Execution statusResponse = SimplifyQAService.fetchPipelineStatus(
                //                        apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId(), listener);
                while (retryCount < effectiveMaxRetries) {
                    try {
                        statusResponse = SimplifyQAService.fetchPipelineStatus(
                                apiUrl,
                                getApiKey(),
                                execObj.getProjectId(),
                                execObj.getId(),
                                effectiveMaxRetries,
                                retryInterval,
                                apiTimeout,
                                listener);
                        listener.getLogger().println("Status API raw response: " + statusResponse);
                        if (statusResponse != null) break;
                    } catch (Exception e) {
                        listener.getLogger().println("Attempt " + (retryCount + 1) + " failed: " + e.getMessage());
                    }

                    retryCount++;
                    Thread.sleep(retryInterval * 1000L);
                }
                if (statusResponse == null) {
                    listener.getLogger().println("Failed to fetch execution status after retries. Marking as FAILURE.");
                    String killType = computeKillType(execObj);
                    SimplifyQAService.stopExecution(
                            apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId(), killType);
                    run.setResult(Result.FAILURE);
                    return;
                }

                execObj = new Execution(statusResponse, threshold);

                if (temp == null
                        || temp.getMetadata().getExecutedPercent()
                                < execObj.getMetadata().getExecutedPercent()) {
                    SimplifyQAUtils.printStatus(execObj);
                }

                temp = execObj;
                Thread.sleep(5000); // Delay for status polling
            }
            BigDecimal failedPercent =
                    BigDecimal.valueOf(execObj.getMetadata().getFailedPercent()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal passedPercent =
                    BigDecimal.valueOf(execObj.getMetadata().getPassedPercent()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal executedPercent = BigDecimal.valueOf(
                            execObj.getMetadata().getExecutedPercent())
                    .setScale(2, RoundingMode.HALF_UP);

            int passedCount = execObj.getMetadata().getPassedCount();
            int failedCount = execObj.getMetadata().getFailedCount();
            int totalCount = execObj.getMetadata().getTotalCount();
            listener.getLogger().println("Executed Percent: " + executedPercent);
            listener.getLogger().println("Passed Percent: " + passedPercent);
            listener.getLogger().println("Failed Percent: " + failedPercent);
            listener.getLogger().println("Passed Count: " + passedCount);
            listener.getLogger().println("Failed Count: " + failedCount);
            listener.getLogger().println("Total Count: " + totalCount);
            if ("FAILED".equalsIgnoreCase(execObj.getStatus())) {
                listener.getLogger().println("Execution failed. Stopping pipeline...");
                String killType = computeKillType(execObj);
                SimplifyQAService.stopExecution(apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId(), killType);
                run.setResult(Result.FAILURE);
            } else {
                listener.getLogger().println("Execution completed successfully.");
                run.setResult(Result.SUCCESS);
            }
        } catch (Exception e) {
            listener.getLogger().println("Error occurred: " + e.getMessage());
            String killType = computeKillType(execObj);
            SimplifyQAService.stopExecution(apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId(), killType);
            run.setResult(Result.FAILURE);
        }
    }

    // helper to compute killType similar to JS:
    // const killType = (['PARALLEL'].includes(execObj.executionStyle?.toUpperCase()) &&
    // ['CLOUD'].includes(execObj.mode?.toUpperCase())) ? 'STOP_EXECUTION_PARALLEL_CLOUD' : 'STOP_EXECUTION';
    private String computeKillType(Execution exec) {
        if (exec == null) return "STOP_EXECUTION";
        try {
            String execStyle = null;
            String mode = null;
            // Attempt to read properties; adjust method names if your Execution class uses different getters
            try {
                execStyle = exec.getExecutionStyle();
            } catch (NoSuchMethodError ignored) {
                /* ignore */
            }
            try {
                mode = exec.getMode();
            } catch (NoSuchMethodError ignored) {
                /* ignore */
            }

            execStyle = (execStyle != null) ? execStyle.toUpperCase() : "";
            mode = (mode != null) ? mode.toUpperCase() : "";

            if ("PARALLEL".equals(execStyle) && "CLOUD".equals(mode)) {
                return "STOP_EXECUTION_PARALLEL_CLOUD";
            }
        } catch (Exception e) {
            // ignore and fallback
        }
        return "STOP_EXECUTION";
    }

    @Symbol("simplifyQA")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "SimplifyQA Pipeline Executor";
        }
    }
}
