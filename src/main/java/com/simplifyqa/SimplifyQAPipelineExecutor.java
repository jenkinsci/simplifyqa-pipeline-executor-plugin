package com.simplifyqa;

import com.simplifyqa.model.Execution;
import com.simplifyqa.service.SimplifyQAService;
import com.simplifyqa.utils.SimplifyQAUtils;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.Secret;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SimplifyQAPipelineExecutor extends Builder implements SimpleBuildStep {
    private final String pipelineId;
    private final String apiUrl;
    private final Secret apiKey;
    private final double threshold;

    @DataBoundConstructor
    public SimplifyQAPipelineExecutor(String apiUrl, String apiKey, String pipelineId, double threshold) {
        this.apiUrl = apiUrl;
        this.apiKey = Secret.fromString(apiKey);
        this.pipelineId = pipelineId;
        this.threshold = threshold;
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

    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener)
            throws InterruptedException, IOException {
        listener.getLogger().println("********** FETCHING VALUES **********");
        listener.getLogger().println("********** SIMPLIFYQA PIPELINE EXECUTOR **********");
        listener.getLogger().println("Pipeline Execution Started...");
        listener.getLogger().println("API URL: " + apiUrl);
        listener.getLogger().println("Pipeline ID: " + pipelineId);
        listener.getLogger().println("Threshold percentage: " + threshold);

        Execution response = SimplifyQAService.startPipelineExecution(apiUrl, getApiKey(), pipelineId, listener);
        if (response == null) {
            listener.getLogger().println("Failed to start execution.");
            run.setResult(Result.FAILURE);
            return;
        }

        Execution execObj = new Execution((Execution) response, threshold);

        listener.getLogger().println("Execution started with status: " + execObj.getStatus());
        SimplifyQAUtils.printStatus(execObj);

        try {
            Execution temp = null;
            while (execObj.getStatus().equalsIgnoreCase("INPROGRESS")) {
                double failedPercent = execObj.getMetadata().getFailedPercent();
                if (failedPercent >= threshold) {
                    listener.getLogger().println("Threshold reached (" + threshold + "%). Stopping execution...");
                    SimplifyQAService.stopExecution(apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId());
                    run.setResult(Result.FAILURE);
                    return;
                }
                Execution statusResponse = SimplifyQAService.fetchPipelineStatus(
                        apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId(), listener);

                if (statusResponse == null) {
                    listener.getLogger().println("Failed to fetch execution status after retries. Marking as FAILURE.");
                    SimplifyQAService.stopExecution(apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId());
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

            if ("FAILED".equalsIgnoreCase(execObj.getStatus())) {
                listener.getLogger().println("Execution failed. Stopping pipeline...");
                SimplifyQAService.stopExecution(apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId());
                run.setResult(Result.FAILURE);
            } else {
                listener.getLogger().println("Execution completed successfully.");
                run.setResult(Result.SUCCESS);
            }
        } catch (Exception e) {
            listener.getLogger().println("Error occurred: " + e.getMessage());
            SimplifyQAService.stopExecution(apiUrl, getApiKey(), execObj.getProjectId(), execObj.getId());
            run.setResult(Result.FAILURE);
        }
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
