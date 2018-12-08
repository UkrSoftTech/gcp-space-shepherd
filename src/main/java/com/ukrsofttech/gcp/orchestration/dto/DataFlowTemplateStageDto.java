package com.ukrsofttech.gcp.orchestration.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataFlowTemplateStageDto {

    public static final String RUNNER_PARAM_NAME = "runner";
    public static final String RUNNER_VALUE = "DataflowRunner";

    public static final String PROJECT_PARAM_NAME = "project";

    public static final String STAGING_LOCATION_PARAM_NAME = "stagingLocation";

    public static final String TEMPLATE_LOCATION_PARAM_NAME = "templateLocation";

    @Getter
    @Setter
    private String templateName;

    @Getter
    @Setter
    private String pipelineTemplateClass;

    @Getter
    @Setter
    private String projectId;

    @Getter
    @Setter
    private String stagingLocation;

    @Getter
    @Setter
    private String templateLocation;

    @JsonIgnore
    public Map<String, String> getParameters() {
        Map<String, String> params = new HashMap<>();
        params.put(RUNNER_PARAM_NAME, RUNNER_VALUE);
        params.put(PROJECT_PARAM_NAME, projectId);
        params.put(STAGING_LOCATION_PARAM_NAME, stagingLocation);
        params.put(TEMPLATE_LOCATION_PARAM_NAME, templateLocation);
        return params;
    }

}
