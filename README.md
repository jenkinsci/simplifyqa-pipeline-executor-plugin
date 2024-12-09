# SimplifyQA Automation Hub

## Overview
The **SimplifyQA Automation Hub** is a Jenkins plugin that enables users to integrate and manage pipeline executions directly from Jenkins. By leveraging APIs, it provides real-time execution updates, status monitoring, and threshold-based control.

---

## Features
- **Trigger Pipeline Execution**: Starts a pipeline execution using the provided API details.
- **Real-Time Status Updates**: Polls the pipeline status every 5 seconds to monitor progress.
- **Threshold Monitoring**: Automatically stops the execution if the failure rate exceeds the specified threshold.
- **Customizable Parameters**: Supports user-defined API URL, API Key, Pipeline ID, and execution threshold.

---

## Parameters
This plugin requires the following parameters during the build:

| Parameter    | Description                                            | Example                     |
|--------------|--------------------------------------------------------|-----------------------------|
| `API URL`    | The base URL of the API used to trigger the pipeline.  | `https://api.example.com`   |
| `API Key`    | The authentication key for API access.                | `abc123xyz`                 |
| `Pipeline ID`| The identifier for the pipeline to be executed.        | `pipeline-001`              |
| `Threshold`  | The maximum allowed failure percentage before stopping the pipeline. | `20`                        |

---

## Workflow
1. **Start Pipeline Execution**:
    - The plugin initiates a pipeline execution by calling the API's start endpoint with the provided parameters.
    - Logs the initiation details in the Jenkins console.

2. **Fetch Status Periodically**:
    - Every 5 seconds, the plugin queries the pipeline status.
    - Logs detailed execution updates, including progress and error rates.

3. **Threshold Monitoring**:
    - Stops the pipeline if the failure percentage exceeds the specified threshold.
    - Marks the build as **FAILED** if the pipeline fails or exceeds the threshold.

4. **Completion Handling**:
    - If the pipeline completes successfully, the build is marked as **SUCCESS**.

---

## Setup and Usage
This plugin requires the following environment variables to be set in your Jenkins build job:

- **API URL**: The URL of your SimplifyQA API (e.g., `https://api.simplifyqa.com`).
- **API Key**: The API key for authenticating with the SimplifyQA API.
- **Pipeline ID**: The unique ID of the pipeline to be executed.
- **Threshold**: The threshold for failure percentage (e.g., `80.0` for 80%).

When the **Build Now** button is clicked, the plugin will:

1. Trigger the pipeline execution.
2. Poll the status every 5 seconds.
3. Check if the failure percentage exceeds the threshold.
4. Stop the execution if the threshold is reached or the execution fails.
### Install the Plugin
Install the plugin via the Jenkins Plugin Manager.

### Configure Jenkins Job
Add the required parameters as environment variables in your Jenkins job configuration.

#### Example Configuration in a Freestyle Job:
```env
API URL=https://api.example.com
API Key=registrationtoken
Pipeline ID=01
Threshold=20
