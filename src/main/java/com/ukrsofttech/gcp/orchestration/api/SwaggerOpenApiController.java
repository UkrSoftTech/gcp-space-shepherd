package com.ukrsofttech.gcp.orchestration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.swagger.web.ApiResourceController;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@RestController
@RequestMapping("/api")
@ApiIgnore
public class SwaggerOpenApiController extends ApiResourceController {

    @Autowired
    public SwaggerOpenApiController(SwaggerResourcesProvider swaggerResources) {
        super(swaggerResources);
    }

}
