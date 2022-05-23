# Prerequiste

## Create PostgreSQL DB
  * Using Topology to add new PostgreSQL services
    * <img width="792" alt="postgresql" src="https://user-images.githubusercontent.com/58408898/169682524-2b5a81df-21ff-467d-8cb1-7de1cd375ea3.png"> 

## Create AMQ Streams cluster
  * Install AMQ Streams Operator
  
  * Using Topology to create a new kafka cluster
    * <img width="655" alt="kafka-cluster" src="https://user-images.githubusercontent.com/58408898/169682544-3b112d69-4eba-498a-86fb-13e9f87d000e.png">
  
  * Using Topology to create a new kafka topic
    * <img width="637" alt="kafka-topic" src="https://user-images.githubusercontent.com/58408898/169682550-2b1de5b6-bdc0-46c4-ac21-15faf12f3d22.png"> 

## Create Prometheus
  * Create prometheus configmap from prometheus.yml
    ```
    oc create configmap prom --from-file=prometheus.yml=$HOME/quarkus-lab/quarkus-workshop-m1m2-labs/src/main/kubernetes/prometheus.yml
    ```

  * Using Topology to add new prometheus via container image
    * <img width="454" alt="prometheus" src="https://user-images.githubusercontent.com/58408898/169682560-9d114d91-8242-48f6-8a8a-e3a34ae95dbb.png"> 
  
  * Mount configmap
    ```
    oc set volume deployment/prometheus --add -t configmap --configmap-name=prom -m /etc/prometheus/prometheus.yml --sub-path=prometheus.yml
    ```
  
  * Rollout prometheus services
    ```
    oc rollout status -w deployment/prometheus
    ```
  
  * Open prometheus UI
    * <img width="959" alt="prometheus-ui" src="https://user-images.githubusercontent.com/58408898/169682573-4327c4d6-6eaa-4a04-b4af-a8169e755952.png">

## Create Grafana
  * Using Topology to add new grafana via container image
    * <img width="451" alt="grafana" src="https://user-images.githubusercontent.com/58408898/169682590-e5afdd13-65b7-4fce-aa4f-2b2b898daf65.png"> 
  
  * Add datasource to grafana
    * <img width="757" alt="grafana-add-datasource" src="https://user-images.githubusercontent.com/58408898/169682599-41dbcfc8-a360-479a-a80c-a1c4f7768585.png">
    * <img width="388" alt="grafana-add-datasource-proemtheus" src="https://user-images.githubusercontent.com/58408898/169682604-f0652e94-5b06-4db2-9d1d-7e9437b46c3b.png">

## Create Jaeger
  * Install Red Hat Distributed Tracing Operator
  
  * Using Topology to add new Jaeger service
## Create Keycloak
  * Install Red Hat SSO Operator
  
  * Create Keycloak using CR
    * <img width="454" alt="SSO-keycloak" src="https://user-images.githubusercontent.com/58408898/169682639-851a3d41-3585-4446-a146-a1ff66afee9f.png"> 
  
  * Login and create new realm (username/password is in the secret)
    * <img width="158" alt="SSO-realm" src="https://user-images.githubusercontent.com/58408898/169682643-d80ae1ec-f6cc-4160-b8af-64fdbf1bb5a2.png"> 
  
  * Create client
    * <img width="691" alt="SSO-client" src="https://user-images.githubusercontent.com/58408898/169682655-ff62f699-bd7a-4908-9283-e07fdd5d5c14.png">
  
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
