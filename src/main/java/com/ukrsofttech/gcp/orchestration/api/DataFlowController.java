package com.ukrsofttech.gcp.orchestration.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.*;
import com.ukrsofttech.gcp.orchestration.dto.DataFlowJobStateDto;
import com.ukrsofttech.gcp.orchestration.dto.DataFlowTemplateLaunchDto;
import com.ukrsofttech.gcp.orchestration.dto.DataFlowTemplateStageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/dataflow", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataFlowController {

    private static final String PIPELINE_TEMPLATE = "template";

    @Value("google.dataflow.project.id")
    private String projectId;

    @Autowired
    private TaskExecutor taskExecutor;

    @PostMapping(value = "/templates/stage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataFlowTemplateStageDto stageDataFlowTemplate(@RequestBody DataFlowTemplateStageDto stagingTemplateDto){
        try {
            log.info("Staging DataFlow pipeline template started...");
            log.info("GCP_PROJECT_ID is: " + projectId);

            log.info("Running stage process with params: " + stagingTemplateDto.getParameters());
            stagePipelineTemplate(stagingTemplateDto.getTemplateName(), stagingTemplateDto.getParameters());
            return stagingTemplateDto;
        } catch (Exception e) {
            log.error("Staging error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/templates/launch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataFlowJobStateDto launchDataFlowTemplate(@RequestBody DataFlowTemplateLaunchDto launchDto)
            throws IOException, GeneralSecurityException {

        log.info("Launching DataFlow pipeline from template started...");

        Dataflow dataflowService = getDataFlowService();

        LaunchTemplateParameters templateParameters = new LaunchTemplateParameters();
        templateParameters.setJobName(launchDto.getJobName());
        templateParameters.setParameters(launchDto.getParameters());

        RuntimeEnvironment environment = new RuntimeEnvironment();
        environment.setTempLocation(launchDto.getTempLocation());
        environment.setZone(launchDto.getZone());
        templateParameters.setEnvironment(environment);

        Dataflow.Projects.Templates.Launch launch = dataflowService.projects()
                .templates()
                .launch(projectId, templateParameters);

        LaunchTemplateResponse response = null;
        try {
            response = launch.setGcsPath(launchDto.getGcsPath()).execute();
        }
        catch (Exception e) {
            log.error("Something goes wrong", e);
        }

        log.info("Response from Template launching: " + response);

        if (response != null) {
            Job job = response.getJob();
            return DataFlowJobStateDto.builder()
                    .id(job.getId())
                    .type(job.getType())
                    .jobName(job.getName())
                    .clientRequestId(job.getClientRequestId())
                    .createTime(job.getCreateTime())
                    .currentState(job.getCurrentState())
                    .currentStateTime(job.getCurrentStateTime())
                    .build();
        }

        throw new RuntimeException("Template launching failed");
    }

    @GetMapping("/jobs/list")
    public List<DataFlowJobStateDto> getAllDataFlowJobs() throws IOException, GeneralSecurityException {

        Dataflow dataflowService = getDataFlowService();

        Dataflow.Projects.Jobs.List request = dataflowService.projects().jobs().list(projectId);
        ListJobsResponse response;
        List<DataFlowJobStateDto> jobStates = new ArrayList<>();
        do {
            response = request.execute();
             if (response.getJobs() == null) {
                 continue;
             }

            for (Job job : response.getJobs()) {
                DataFlowJobStateDto jobState = DataFlowJobStateDto.builder()
                        .id(job.getId())
                        .jobName(job.getName())
                        .currentState(job.getCurrentState())
                        .createTime(job.getCreateTime())
                        .clientRequestId(job.getClientRequestId())
                        .type(job.getType())
                        .build();
                jobStates.add(jobState);
            }

            request.setPageToken(response.getNextPageToken());
        } while (response.getNextPageToken() != null);


        return jobStates;
    }

    private String[] convertToMainArgs(Map<String, String> params) {
        return params.keySet()
                .stream()
                .filter(key -> !key.equalsIgnoreCase(PIPELINE_TEMPLATE))
                .map(key -> String.format("--%s=%s", key, params.get(key)))
                .toArray(String[]::new);
    }

    private void stagePipelineTemplate(String templateClass, Map<String, String> params)
            throws ClassNotFoundException, NoSuchMethodException {

        Class<?> aClass = Class.forName(templateClass);
        Method method = aClass.getMethod("main", String[].class);

        String[] appParams = convertToMainArgs(params);

        log.info("Staging dataflow  template" + aClass.getCanonicalName());

        taskExecutor.execute(() -> {
            try {
                log.info("DataFlow template " + templateClass + " uploading started...");
                method.invoke(null, (Object) appParams);
                log.info("DataFlow template " + templateClass + " uploading finished!");
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("DataFlow template staging failed", e);
                throw new RuntimeException(e);
            }
        });
    }

    private Dataflow getDataFlowService() throws IOException, GeneralSecurityException {
        // Authentication is provided by gcloud tool when running locally
        // and by built-in service accounts when running on GAE, GCE or GKE.
        GoogleCredential credential = GoogleCredential.getApplicationDefault();

        // The createScopedRequired method returns true when running on GAE or a local developer
        // machine. In that case, the desired scopes must be passed in manually. When the code is
        // running in GCE, GKE or a Managed VM, the scopes are pulled from the GCE metadata server.
        // See https://developers.google.com/identity/protocols/application-default-credentials for more information.
        if (credential.createScopedRequired()) {
            credential = credential.createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
        }

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Dataflow.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Google Cloud DataFlow Governance application")
                .build();
    }

}
