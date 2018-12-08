package com.ukrsofttech.gcp.orchestration.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoDto {

    public static final ProjectInfoDto INSTANCE = ProjectInfoDto.builder()
            .name("dataflow-governance")
            .description("Application for monitoring Google DataFlow executions")
            .version("1.0.0-SNAPSHOT")
            .revision("draft-revision")
            .apiDefinitionPath("/api/docs")
            .apiUserInterfacePath("/swagger-ui.html")
            .build();

    private String name;

    private String description;

    private String version;

    private String revision;

    private String apiDefinitionPath;

    private String apiUserInterfacePath;

}
