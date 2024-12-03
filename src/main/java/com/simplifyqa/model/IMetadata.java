package com.simplifyqa.model;

public interface IMetadata {
    double getThreshold();

    boolean isKilled();

    boolean isVerbose();

    int getTotalCount();

    int getPassedCount();

    double getPassedPercent();

    int getFailedCount();

    double getFailedPercent();

    int getSkippedCount();

    double getSkippedPercent();

    int getExecutedCount();

    double getExecutedPercent();

    String getState();

    String getResult();
}
