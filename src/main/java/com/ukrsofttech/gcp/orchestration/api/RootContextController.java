package com.ukrsofttech.gcp.orchestration.api;

import com.ukrsofttech.gcp.orchestration.dto.ProjectInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootContextController {

    @GetMapping
    public ProjectInfoDto projectInfo() {
        return ProjectInfoDto.INSTANCE;
    }

//    @GetMapping(value = "/api/swagger-ui", produces = MediaType.TEXT_HTML_VALUE)
//    public String greeting() {
//        return "redirect:/swagger-ui.html";
//    }

}
