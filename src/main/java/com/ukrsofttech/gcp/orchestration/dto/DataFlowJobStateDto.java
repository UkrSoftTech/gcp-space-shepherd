package com.ukrsofttech.gcp.orchestration.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DataFlowJobStateDto {

    private java.lang.String id;

    private String jobName;

    private String currentState;

    private String currentStateTime;

    private String createTime;

    private String clientRequestId;

    private String type;

}
