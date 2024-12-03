package com.simplifyqa.service;

import com.simplifyqa.model.Execution;
import com.simplifyqa.model.IExecution;
import com.simplifyqa.utils.SimplifyQAUtils;
import hudson.ProxyConfiguration;
import hudson.model.TaskListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import jenkins.model.Jenkins;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class SimplifyQAService {
    private final SimplifyQAUtils simplifyQAUtils = new SimplifyQAUtils();

    public Execution startPipelineExecution(String apiUrl, String apiKey, String pipelineId, TaskListener listener) {
        String urlStr = apiUrl + "/pl/exec/start/" + pipelineId;
        try {

            HttpURLConnection connection = createConnection(urlStr, "POST", apiKey, listener);
            int responseCode = connection.getResponseCode();
            listener.getLogger().println("Response Code (POST): " + responseCode);

            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseBody = response.toString();

                if (responseBody != null && !responseBody.isEmpty()) {
                    JSONObject jsonResponse = JSONObject.fromObject(responseBody);

                    // Handle projectId (dynamic type handling)
                    Object projectIdObj = jsonResponse.get("projectId");
                    String projectId;
                    if (projectIdObj instanceof String) {
                        projectId = (String) projectIdObj;
                    } else if (projectIdObj instanceof Number) {
                        projectId = String.valueOf(projectIdObj);
                    } else {
                        throw new JSONException("Unexpected type for projectId");
                    }

                    Object execIdObj = jsonResponse.get("id");
                    String execId;
                    if (execIdObj instanceof String) {
                        execId = (String) execIdObj;
                    } else if (execIdObj instanceof Number) {
                        execId = String.valueOf(execIdObj);
                    } else {
                        throw new JSONException("Unexpected type for id");
                    }
                    listener.getLogger()
                            .println("API call successful. Project ID: " + projectId + ", Execution ID: " + execId);

                    IExecution executionData = simplifyQAUtils.createExecutionFromApiResponse(responseBody);

                    // Return ExecutionResponse with Execution object as the first parameter
                    return new Execution(executionData) {};

                } else {
                    listener.getLogger().println("Response body is empty or null.");
                    return null;
                }
            } else {
                listener.getLogger().println("API call failed with response code: " + responseCode);
                return null;
            }
        } catch (IOException | JSONException e) {
            listener.getLogger().println("Error during API call (POST): " + e.getMessage());
            return null;
        }
    }

    public Execution fetchPipelineStatus(
            String apiUrl, String apiKey, int projectId, int execId, TaskListener listener) {
        String urlStr = apiUrl + "/pl/exec/status/" + projectId + "/" + execId;
        listener.getLogger().println("Fetching status from: " + urlStr);
        try {

            HttpURLConnection connection = createConnection(urlStr, "GET", apiKey, listener);

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject jsonResponse = JSONObject.fromObject(response.toString());
                String status = jsonResponse.getString("status");

                listener.getLogger().println("Status: " + status);
                IExecution executionData = simplifyQAUtils.createExecutionFromApiResponse(response.toString());

                // Return ExecutionResponse with Execution object as the first parameter
                return new Execution(executionData) {};
            } else {
                listener.getLogger().println("Failed to fetch status, response code: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            listener.getLogger().println("Error fetching status: " + e.getMessage());
            return null;
        }
    }

    public void stopPipelineExecution(String apiUrl, String apiKey, int projectId, int execId, TaskListener listener) {
        String urlStr = apiUrl + "/pl/exec/stop/" + projectId + "/" + execId;
        listener.getLogger().println("Stopping execution at: " + urlStr);
        makeApiCall("POST", urlStr, apiKey, listener);
    }

    private HttpURLConnection createConnection(String urlStr, String method, String apiKey, TaskListener listener)
            throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection;

        ProxyConfiguration proxyConfig = Jenkins.get().proxy;
        Proxy proxy = Proxy.NO_PROXY;
        if (proxyConfig != null) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.name, proxyConfig.port));
            listener.getLogger().println("Using proxy: " + proxyConfig.name + ":" + proxyConfig.port);
        }

        connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }

    public boolean makeApiCall(String method, String urlStr, String apiKey, TaskListener listener) {
        try {

            HttpURLConnection connection = createConnection(urlStr, "POST", apiKey, listener);

            int responseCode = connection.getResponseCode();
            listener.getLogger().println("Response Code (" + method + "): " + responseCode);
            if (responseCode >= 400) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = in.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                in.close();
                listener.getLogger().println("Error Response Body: " + errorResponse.toString());
            }
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseBody = response.toString();
                listener.getLogger().println("Response Body: " + responseBody);

                return responseBody != null && !responseBody.isEmpty();
            } else {
                listener.getLogger().println("API call failed with response code: " + responseCode);
                return false;
            }

        } catch (IOException e) {
            listener.getLogger().println("Error during API call (" + method + "): " + e.getMessage());
            return false;
        }
    }
}
