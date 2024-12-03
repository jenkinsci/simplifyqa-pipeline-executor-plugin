package com.simplifyqa.model;

import java.util.List;
import java.util.Map;

public interface ITestcase {
    int getTestcaseId();

    String getTestcaseName();

    String getTestcaseDesc();

    String getVersionId();

    int getTestcaseSeq();

    String getStepId();

    int getStepCount();

    String getTcType();

    Map<String, Object> getConfiguration();

    List<String> getIterationsSelected();

    List<Platform> getPlatform();

    String getStatus(); // Optional (nullable in Java)

    String getAgentStartTime(); // Optional

    String getAgentEndTime(); // Optional

    String getExecutionDuration(); // Optional
}
