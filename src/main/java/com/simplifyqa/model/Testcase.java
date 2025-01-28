package com.simplifyqa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Testcase implements ITestcase {
    private int testcaseId;
    private String testcaseName;
    private String testcaseDesc;
    private String versionId;
    private int testcaseSeq;
    private String stepId;
    private int stepCount;
    private String tcType;
    private Map<String, Object> configuration;
    private List<String> iterationsSelected;
    private List<Platform> platform;
    private String status; // Optional field
    private String agentStartTime; // Optional field
    private String agentEndTime; // Optional field
    private String executionDuration; // Optional field

    public Testcase() {
        // Optionally initialize fields if necessary
    }

    public Testcase(
            int testcaseId,
            String testcaseName,
            String testcaseDesc,
            String versionId,
            int testcaseSeq,
            String stepId,
            int stepCount,
            String tcType,
            Map<String, Object> configuration,
            List<String> iterationsSelected,
            List<Platform> platform,
            String status,
            String agentStartTime,
            String agentEndTime,
            String executionDuration) {
        this.testcaseId = testcaseId;
        this.testcaseName = testcaseName;
        this.testcaseDesc = testcaseDesc;
        this.versionId = versionId;
        this.testcaseSeq = testcaseSeq;
        this.stepId = stepId;
        this.stepCount = stepCount;
        this.tcType = tcType;
        this.configuration = configuration;
        this.iterationsSelected = iterationsSelected;
        this.platform = platform;
        this.status = status;
        this.agentStartTime = agentStartTime;
        this.agentEndTime = agentEndTime;
        this.executionDuration = executionDuration;
    }

    public Testcase(List<Testcase> testcases) {}

    public int getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(int testcaseId) {
        this.testcaseId = testcaseId;
    }

    public String getTestcaseName() {
        return testcaseName;
    }

    public void setTestcaseName(String testcaseName) {
        this.testcaseName = testcaseName;
    }

    public String getTestcaseDesc() {
        return testcaseDesc;
    }

    public void setTestcaseDesc(String testcaseDesc) {
        this.testcaseDesc = testcaseDesc;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public int getTestcaseSeq() {
        return testcaseSeq;
    }

    public void setTestcaseSeq(int testcaseSeq) {
        this.testcaseSeq = testcaseSeq;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getTcType() {
        return tcType;
    }

    public void setTcType(String tcType) {
        this.tcType = tcType;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }

    public List<String> getIterationsSelected() {
        return iterationsSelected;
    }

    public void setIterationsSelected(List<String> iterationsSelected) {
        this.iterationsSelected = iterationsSelected;
    }

    public List<Platform> getPlatform() {
        return platform;
    }

    public void setPlatform(List<Platform> platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgentStartTime() {
        return agentStartTime;
    }

    public void setAgentStartTime(String agentStartTime) {
        this.agentStartTime = agentStartTime;
    }

    public String getAgentEndTime() {
        return agentEndTime;
    }

    public void setAgentEndTime(String agentEndTime) {
        this.agentEndTime = agentEndTime;
    }

    public String getExecutionDuration() {
        return executionDuration;
    }

    public void setExecutionDuration(String executionDuration) {
        this.executionDuration = executionDuration;
    }
}
