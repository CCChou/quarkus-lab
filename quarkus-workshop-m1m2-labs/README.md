# Prerequiste

## Create PostgreSQL DB
  * Using Topology to add new PostgreSQL services
## Create AMQ Streams cluster
  * Install AMQ Streams Operator
  * Using Topology to create a new kafka cluster
  * Using Topology to create a new kafka topic
## Create Prometheus
  * Create prometheus configmap from prometheus.yml
    ```
    oc create configmap prom --from-file=prometheus.yml=$HOME/quarkus-lab/quarkus-workshop-m1m2-labs/src/main/kubernetes/prometheus.yml
    ```
  * Using Topology to add new prometheus via container image
  * Mount configmap
    ```
    oc set volume deployment/prometheus --add -t configmap --configmap-name=prom -m /etc/prometheus/prometheus.yml --sub-path=prometheus.yml
    ```
  * Rollout prometheus services
    ```
    oc rollout status -w deployment/prometheus
    ```
  * Open prometheus UI
## Create Grafana
  * Using Topology to add new grafana via container image
  * Add datasource to grafana
## Create Jaeger
  * Install Red Hat Distributed Tracing Operator
  * Using Topology to add new Jaeger service
## Create Keycloak
  * Install Red Hat SSO Operator
  * Create Keycloak using CR
  * Login and create new realm (username/password is in the secret)
  * Create client
  * Create users

# Create and Build a Basic Quarkus Microservices App

## Build a Java application as an Linux native binary
  * Use maven CLI to start dev mode
    ```
    mvn compile quarkus:dev
    ```
  * Test App is running
    ```
    curl http://localhost:8080/hello/lastletter/redhat ; echo
    ```
  * Open GreetingResource.java and switch hello method to demonstrate live coding
    ```
    curl http://localhost:8080/hello
    ```
  * build native binary
    ```
    quarkus build --native -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman -DskipTests
    ```  

## Package an application for deployment to OpenShift as a native image
## List and add extensions

# Create a RESTful API services

## Add an externalized configuration to a Quarkus application using MicroProfile Config
## Create entities, define RESTful endpoints, and add basic queries

## Implement paging and filtering and integrate life-cycle hooks

## Deploy a final Quarkus application to OpenShift

  * Label database service
    ```
    oc label dc/postgres-database app.openshift.io/runtime=postgresql --overwrite
    oc label dc/quarkus-person app.kubernetes.io/part-of=quarkus-person --overwrite
    oc label dc/postgres-database app.kubernetes.io/part-of=quarkus-person  --overwrite
    oc annotate dc/quarkus-person app.openshift.io/connects-to=postgres-database --overwrite
    ```
  * Test services
    ```
    curl -s $(oc get route quarkus-person -o=go-template --template='{{ .spec.host }}')/person/birth/before/2000 | jq  
    ```

# Create and Config Health Check

## Configure a project to use the Quarkus SmallRye Health extension
## Create and add custom data to a custom health check
## Develop and fix negative health checks

# Create reactive messageing microservices

## Add a Quarkus extension for messaging

## Publish messages to and consume messages from an address
  * Rebuild app
    ```
    mvn clean package -DskipTests -f /home/mola/winhome/demo/quarkus-lab/quarkus-workshop-m1m2-labs && \
    oc label dc/quarkus-person app.kubernetes.io/part-of=quarkus-person --overwrite && \
    oc label dc/postgres-database app.kubernetes.io/part-of=quarkus-person --overwrite && \
    oc annotate dc/quarkus-person app.openshift.io/connects-to=postgres-database --overwrite && \
    oc rollout status -w dc/quarkus-person
    ```    

  * Test services
    ```
    http://(oc get route quarkus-person -o=go-template --template='{{ .spec.host }}')/names.html
    ```

# Create Microservices Metrics

## Leverage Prometheus to collect application metrics

## Generate metrics in the application and query them in Prometheus

## Visualize the metrics with Grafana

# Create Microservices Tracing

## Install Jaeger for distributed tracing

## Add tracing to a Quarkus application and inspect traces in the Jaeger tracing console