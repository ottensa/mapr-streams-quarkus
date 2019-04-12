# mapr-streams-quarkus
A demo for MapR Streams on top of quarkus.io

## Overview
This project has two modules, one that sends messages to a MapR Stream and one that consumes from that stream. It is assumed that you are running a secured MapR Cluster and that you have a valid MapR Ticket.

Further it has a Dockerfile to create a base image with a MapR Client and the Streams libraries. Built on top of the base image, there is an image for the producer and the consumer.

## Build
```
~ git clone https://github.com/ottensa/mapr-streams-quarkus.git
~ mvn clean package -f mapr-streams-quarkus/streams-consumer/pom.xml
~ mvn clean package -f mapr-streams-quarkus/streams-producer/pom.xml
~ docker build --target quarkus-streams-consumer -t mapr-streams-quarkus_consumer mapr-streams-quarkus
~ docker build --target quarkus-streams-producer -t mapr-streams-quarkus_producer mapr-streams-quarkus
```

## Run
First we need a MapR Stream, in this example it is /quarkus-stream

```
maprcli stream create -path /quarkus-stream -produceperm p -consumeperm p -topicperm p
```

Next we start the producer container, in the env.file we define:

* NAME: the name of the cluster
* CLDBS: a list of the CLDBs hostnames/IPs

```
docker run -it --env-file=/path/to/env.file -v /path/to/maprticket:/tmp/maprticket mapr-streams-quarkus_producer:latest
```

Last we start the consumer container:

```
docker run -it --env-file=/path/to/env.file -v /path/to/maprticket:/tmp/maprticket mapr-streams-quarkus_consumer:latest
```
