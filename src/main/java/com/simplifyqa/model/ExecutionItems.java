package com.simplifyqa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecutionItems implements ITestcase {
    private int id;
    private String name;
    private String description;
    private String versionId;
    private int seq;
    private String stepId;
    private int stepCount;
    private String technologyType;
    private Map<String, Object> configuration;
    private List<String> iterationsSelected;
    private List<Platform> platform;
    private String status; // Optional field
    private String agentStartTime; // Optional field
    private String agentEndTime; // Optional field
    private String executionDuration; // Optional field

    public ExecutionItems(ITestcase testcase) {
        this.id = testcase.getTestcaseId();
        this.name = testcase.getTestcaseName();
        this.description = testcase.getTestcaseDesc();
        this.versionId = testcase.getVersionId();
        this.seq = testcase.getTestcaseSeq();
        this.stepId = testcase.getStepId();
        this.stepCount = testcase.getStepCount();
        this.technologyType = testcase.getTcType();
        this.configuration = testcase.getConfiguration();
        this.iterationsSelected = testcase.getIterationsSelected();
        this.platform = testcase.getPlatform();
        this.status = testcase.getStatus();
        this.agentStartTime = testcase.getAgentStartTime();
        this.agentEndTime = testcase.getAgentEndTime();
        this.executionDuration = testcase.getExecutionDuration();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTechnologyType() {
        return technologyType;
    }

    public ExecutionItems() {}

    public void setTechnologyType(String technologyType) {
        this.technologyType = technologyType;
    }

    public ExecutionItems(
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
        this.id = testcaseId;
        this.name = testcaseName;
        this.description = testcaseDesc;
        this.versionId = versionId;
        this.seq = testcaseSeq;
        this.stepId = stepId;
        this.stepCount = stepCount;
        this.technologyType = tcType;
        this.configuration = configuration;
        this.iterationsSelected = iterationsSelected;
        this.platform = platform;
        this.status = status;
        this.agentStartTime = agentStartTime;
        this.agentEndTime = agentEndTime;
        this.executionDuration = executionDuration;
    }

    public ExecutionItems(List<ExecutionItems> testCases) {}

    public int getTestcaseId() {
        return id;
    }

    public void setTestcaseId(int testcaseId) {
        this.id = testcaseId;
    }

    public String getTestcaseName() {
        return name;
    }

    public void setTestcaseName(String testcaseName) {
        this.name = testcaseName;
    }

    public String getTestcaseDesc() {
        return description;
    }

    public void setTestcaseDesc(String testcaseDesc) {
        this.description = testcaseDesc;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public int getTestcaseSeq() {
        return seq;
    }

    public void setTestcaseSeq(int testcaseSeq) {
        this.seq = testcaseSeq;
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
        return technologyType;
    }

    public void setTcType(String tcType) {
        this.technologyType = tcType;
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
