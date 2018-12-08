package com.ukrsofttech.gcp.orchestration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:google-dataflow.properties")
@Configuration
public class DataFlowConfig {
}
