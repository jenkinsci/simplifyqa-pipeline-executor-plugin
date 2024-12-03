package com.simplifyqa.model;

import java.util.List;
import java.util.Map;

public interface IExecution {
    String get_id();

    int getCustomerId();

    boolean isDeleted();

    int getId();

    int getProjectId();

    String getAgentId();

    String getAuthkey();

    boolean isChildExecution();

    String getCloudType();

    String getCode();

    String getCreatedAt();

    int getCreatedBy();

    String getEnvironmentType();

    String getExecutionCategory();

    String getExecutionOS();

    String getExecutionStyle();

    String getExecutionTime();

    String getExecutionType();

    String getExecutionTypeCode();

    int getExecutionTypeId();

    String getExecutionTypeName();

    boolean isFromAgent();

    Map<String, Object> getGlobalConfiguration();

    int getIterationId();

    List<String> getIterationsSelected();

    String getMode();

    Integer getModuleId(); // Nullable

    Integer getParentExecutionId(); // Nullable

    int getReleaseId();

    String getResult();

    String getStatus();

    List<String> getTags(); // Nullable

    List<Testcase> getTestcases();

    String getType();

    Integer getUserstoryId(); // Nullable

    IMetadata getMetadata();
}
