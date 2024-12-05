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
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SimplifyQAPipelineExecutor extends Builder implements SimpleBuildStep {

    private final String name;
    private final SimplifyQAService pipelineService = new SimplifyQAService();
    private final SimplifyQAUtils simplifyQAUtils = new SimplifyQAUtils();

    @DataBoundConstructor
    public SimplifyQAPipelineExecutor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener)
            throws InterruptedException, IOException {
        String apiUrl = env.get("API URL");
        String apiKey = env.get("API Key");
        String pipelineId = env.get("Pipeline ID");
        double threshold = Double.parseDouble(env.getOrDefault("Threshold", "100")); // Default to 100 if not provided

        listener.getLogger().println("********** SIMPLIFYQA PIPELINE EXECUTOR **********");
        listener.getLogger().println("Pipeline Execution Started...");
        listener.getLogger().println("API URL: " + apiUrl);
        listener.getLogger().println("Pipeline ID: " + pipelineId);

        Execution response = pipelineService.startPipelineExecution(apiUrl, apiKey, pipelineId, listener);
        if (response == null) {
            listener.getLogger().println("Failed to start execution.");
            run.setResult(Result.FAILURE);
            return;
        }

        Execution execObj = new Execution((Execution) response, threshold);

        listener.getLogger().println("Execution started with status: " + execObj.getStatus());
        simplifyQAUtils.printStatus(execObj);

        try {
            Execution temp = null;
            int retryCount = 0; // Track retries
            final int maxRetries = 5; // Define a maximum number of retries (optional)

            while (execObj.getStatus().equalsIgnoreCase("INPROGRESS")
                    && execObj.getMetadata().getFailedPercent()
                            <= execObj.getMetadata().getThreshold()) {

                Execution statusResponse = pipelineService.fetchPipelineStatus(
                        apiUrl, apiKey, execObj.getProjectId(), execObj.getId(), listener);

                if (statusResponse == null) {
                    retryCount++;
                    listener.getLogger().println("Failed to fetch execution status. Retrying... (" + retryCount + ")");

                    // Break the loop if retries exceed the maximum allowed (optional)
                    if (retryCount >= maxRetries) {
                        listener.getLogger().println("Maximum retries reached. Marking as FAILURE.");
                        pipelineService.stopExecution(apiUrl, apiKey, execObj.getProjectId(), execObj.getId());
                        run.setResult(Result.FAILURE);
                        return;
                    }

                    Thread.sleep(5000); // Delay before retrying
                    continue; // Retry the fetch
                }

                // Reset retry count on successful fetch
                retryCount = 0;

                execObj = new Execution(statusResponse, threshold);

                if (temp == null
                        || temp.getMetadata().getExecutedPercent()
                                < execObj.getMetadata().getExecutedPercent()) {
                    simplifyQAUtils.printStatus(execObj);
                }

                temp = execObj;
                Thread.sleep(5000); // Delay for status polling
            }

            if (execObj.getMetadata().getFailedPercent()
                    >= execObj.getMetadata().getThreshold()) {
                listener.getLogger().println("Threshold reached (" + threshold + "%). Stopping execution...");
                pipelineService.stopExecution(apiUrl, apiKey, execObj.getProjectId(), execObj.getId());
                run.setResult(Result.FAILURE);
            } else if ("FAILED".equalsIgnoreCase(execObj.getStatus())) {
                listener.getLogger().println("Execution failed. Stopping pipeline...");
                pipelineService.stopExecution(apiUrl, apiKey, execObj.getProjectId(), execObj.getId());
                run.setResult(Result.FAILURE);
            } else {
                listener.getLogger().println("Execution completed successfully.");
                run.setResult(Result.SUCCESS);
            }
        } catch (Exception e) {
            listener.getLogger().println("Error occurred: " + e.getMessage());
            pipelineService.stopExecution(apiUrl, apiKey, execObj.getProjectId(), execObj.getId());
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
            return "SimplifyQA";
        }
    }
}
