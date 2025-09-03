package com.simplifyqa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Execution implements IExecution {
    private String _id;
    private int customerId;
    private boolean deleted;
    private int id;
    private int projectId;
    private String agentId;
    //    private String authkey;
    private boolean childExecution;
    private String cloudType;
    private String code;
    private String createdAt;
    private int createdBy;
    private String environmentType;
    private String executionCategory;
    private String executionOS;
    private String executionStyle;
    private String executionTime;
    private String executionType;
    private String executionTypeCode;
    private int executionTypeId;
    private String executionTypeName;
    private boolean fromAgent;
    private Map<String, Object> globalConfiguration;
    private int iterationId;
    private List<String> iterationsSelected;
    private String mode;
    private Integer moduleId; // Nullable
    private Integer parentExecutionId; // Nullable
    private int releaseId;
    private String result;
    private Map<String, Object> extraPreferences;
    private String status;
    private List<String> tags; // Nullable
    private List<ExecutionItems> executionItems = new ArrayList<>();
    private String type;
    private Integer userstoryId; // Nullable
    private Metadata metadata;

    public Map<String, Object> getExtraPreferences() {
        return extraPreferences;
    }

    @Override
    public List<ExecutionItems> getExecutionItems() {
        return executionItems;
    }

    public void setExtraPreferences(Map<String, Object> extraPreferences) {
        this.extraPreferences = extraPreferences;
    }

    public Execution(IExecution data, Map<String, Object> options) {
        this._id = data.get_id();
        this.customerId = data.getCustomerId();
        this.deleted = data.isDeleted();
        this.id = data.getId();
        this.projectId = data.getProjectId();
        this.agentId = data.getAgentId();
        //        this.authkey = data.getAuthkey();
        this.childExecution = data.isChildExecution();
        this.cloudType = data.getCloudType();
        this.code = data.getCode();
        this.createdAt = data.getCreatedAt();
        this.createdBy = data.getCreatedBy();
        this.environmentType = data.getEnvironmentType();
        this.executionCategory = data.getExecutionCategory();
        this.executionOS = data.getExecutionOS();
        this.executionStyle = data.getExecutionStyle();
        this.executionTime = data.getExecutionTime();
        this.executionType = data.getExecutionType();
        this.executionTypeCode = data.getExecutionTypeCode();
        this.executionTypeId = data.getExecutionTypeId();
        this.executionTypeName = data.getExecutionTypeName();
        this.fromAgent = data.isFromAgent();
        this.globalConfiguration = data.getGlobalConfiguration();
        this.iterationId = data.getIterationId();
        this.iterationsSelected = data.getIterationsSelected();
        this.mode = data.getMode();

        this.moduleId = data.getModuleId();
        this.parentExecutionId = data.getParentExecutionId();
        this.releaseId = data.getReleaseId();
        this.result = data.getResult();
        this.status = data.getStatus();
        this.tags = data.getTags();
        this.executionItems =
                data.getExecutionItems().stream().map(ExecutionItems::new).collect(Collectors.toList());

        this.type = data.getType();
        this.userstoryId = data.getUserstoryId();

        double threshold = options.containsKey("threshold")
                ? (Double) options.get("threshold")
                : Double.parseDouble(System.getenv().getOrDefault("INPUT_THRESHOLD", "100"));
        boolean verbose = options.containsKey("verbose")
                ? (Boolean) options.get("verbose")
                : Boolean.parseBoolean(System.getenv().getOrDefault("INPUT_VERBOSE", "false"));
        boolean isKilled =
                options.containsKey("isKilled") ? (Boolean) options.get("isKilled") : false; // Default to false
        this.metadata = new Metadata(threshold, verbose, isKilled, this);
    }

    public Execution(Execution response, double threshold) {
        if (response != null) {
            this.status = response.getStatus();
            this.metadata = response.getMetadata();
            this.projectId = response.getProjectId();
            //            this.metadata = new Metadata(threshold, false, false, this);
            this.id = response.getId();
            this.executionItems = response.getExecutionItems();
        }

        assert response != null;
        this.metadata = new Metadata(threshold, false, false, response);
    }

    public Execution() {}

    public Execution(String projectId, String execId) {}

    public Execution(IExecution response) {
        if (response != null) {
            this.status = response.getStatus();
            this.metadata = (Metadata) response.getMetadata();
            this.projectId = response.getProjectId();
            this.id = response.getId();
            this.executionItems = response.getExecutionItems();
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    //    public String getAuthkey() {
    //        return authkey;
    //    }
    //
    //    public void setAuthkey(String authkey) {
    //        this.authkey = authkey;
    //    }

    public boolean isChildExecution() {
        return childExecution;
    }

    public void setChildExecution(boolean childExecution) {
        this.childExecution = childExecution;
    }

    public String getCloudType() {
        return cloudType;
    }

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(String environmentType) {
        this.environmentType = environmentType;
    }

    public String getExecutionCategory() {
        return executionCategory;
    }

    public void setExecutionCategory(String executionCategory) {
        this.executionCategory = executionCategory;
    }

    public String getExecutionOS() {
        return executionOS;
    }

    public void setExecutionOS(String executionOS) {
        this.executionOS = executionOS;
    }

    public String getExecutionStyle() {
        return executionStyle;
    }

    public void setExecutionStyle(String executionStyle) {
        this.executionStyle = executionStyle;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getExecutionTypeCode() {
        return executionTypeCode;
    }

    public void setExecutionTypeCode(String executionTypeCode) {
        this.executionTypeCode = executionTypeCode;
    }

    public int getExecutionTypeId() {
        return executionTypeId;
    }

    public void setExecutionTypeId(int executionTypeId) {
        this.executionTypeId = executionTypeId;
    }

    public String getExecutionTypeName() {
        return executionTypeName;
    }

    public void setExecutionTypeName(String executionTypeName) {
        this.executionTypeName = executionTypeName;
    }

    public boolean isFromAgent() {
        return fromAgent;
    }

    public void setFromAgent(boolean fromAgent) {
        this.fromAgent = fromAgent;
    }

    public Map<String, Object> getGlobalConfiguration() {
        return globalConfiguration;
    }

    public void setGlobalConfiguration(Map<String, Object> globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }

    public int getIterationId() {
        return iterationId;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    public List<String> getIterationsSelected() {
        return iterationsSelected;
    }

    public void setIterationsSelected(List<String> iterationsSelected) {
        this.iterationsSelected = iterationsSelected;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getParentExecutionId() {
        return parentExecutionId;
    }

    public void setParentExecutionId(Integer parentExecutionId) {
        this.parentExecutionId = parentExecutionId;
    }

    public int getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(int releaseId) {
        this.releaseId = releaseId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ExecutionItems> getTestCases() {
        return executionItems;
    }

    public void setExecutionItems(List<ExecutionItems> testCases) {
        this.executionItems = testCases;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserstoryId() {
        return userstoryId;
    }

    public void setUserstoryId(Integer userstoryId) {
        this.userstoryId = userstoryId;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
