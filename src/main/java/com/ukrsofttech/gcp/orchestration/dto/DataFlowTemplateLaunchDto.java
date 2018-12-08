package com.ukrsofttech.gcp.orchestration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataFlowTemplateLaunchDto {

    public static final String PROJECT_PARAM_NAME = "project";

    @Getter
    @Setter
    @JsonProperty()
    private String project;

    /**
     * Path to Template on GCP Storage
     */
    @Getter
    @Setter
    private String gcsPath;

    @Getter
    @Setter
    private String jobName;

    @Getter
    @Setter
    private String tempLocation;

    @Getter
    @Setter
    private String zone;

    @Getter
    @Builder.Default
    private Map<String, String> parameters = new HashMap<>();

    @JsonAnySetter
    public void add(String key, String value) {
        parameters.put(key, value);
    }

}
