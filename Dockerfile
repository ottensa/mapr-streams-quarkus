FROM centos:centos7 AS base-image

ADD docker/mapr.repo /etc/yum.repos.d
ADD docker/setup.sh /opt

RUN rpm --import http://package.mapr.com/releases/pub/maprgpg.key && \
    yum install -y which java-1.8.0-openjdk-devel && \
	yum install -y mapr-client mapr-kafka && \
    yum clean all

ENV JAVA_OPTIONS=-Dquarkus.http.host=0.0.0.0
ENV MAPR_TICKETFILE_LOCATION=/tmp/maprticket

ENTRYPOINT ["/opt/setup.sh"]


FROM base-image AS quarkus-streams-producer
COPY streams-producer/target/lib/* /deployments/lib/
COPY streams-producer/target/*-runner.jar /deployments/app.jar


FROM base-image AS quarkus-streams-consumer
COPY streams-consumer/target/lib/* /deployments/lib/
COPY streams-consumer/target/*-runner.jar /deployments/app.jar