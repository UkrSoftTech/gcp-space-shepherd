package com.ukrsofttech.gcp.orchestration.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Slf4j
public class GcsAccessServiceTest {

    private final GcsAccessService service = new GcsAccessService();

    @Test
    public void testGetBucketAndFileNames() {
        log.info("Test getBucketAndFileNames(...)");

        String bucketName = "my-test-bucket";
        String fileName = "MyTemplateFile";

        Pair<String, String> pair = service.getBucketAndFileNames("gs://" + bucketName + "/templates/" + fileName);

        assertThat(pair, is(notNullValue()));
        log.info("Pair: " + pair);
        assertThat(pair.getLeft(), equalTo(bucketName));
        assertThat(pair.getRight(), equalTo(fileName));
    }
}
