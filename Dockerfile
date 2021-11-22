FROM google/cloud-sdk

MAINTAINER Dmytro Maidaniuk <dmytro.maidaniuk@gmail.com>

WORKDIR /
COPY /target/dataflow-governance-exec.jar .

ENV GOOGLE_APPLICATION_CREDENTIALS=/fake-secret

EXPOSE 8080

CMD ["java", "-jar", "/dataflow-governance-exec.jar"]