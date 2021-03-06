# Build docker image

To build docker image run from backend folder:

```bash
mvn clean install -Dmaven.test.skip=true dockerfile:build
```

Prior to running local docker image you would need to deploy SQL server to local minikube (See below) or local docker:

```
docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=TVMdev2018' -p 1433:1433 -d microsoft/mssql-server-linux:latest
```


To run docker image:
```bash
docker run -d  --net="host" -p 8080:8080 -e DATASOURCE_URL="jdbc:sqlserver://192.168.99.101:30001;databaseName=accounts" \
           -e DATASOURCE_USERNAME=sa \
           -e DATASOURCE_PASSWORD=TVMdev2018 transactrules/api
```

To push Google to repository:

```bash
docker tag transactrules/api:latest us.gcr.io/transact-rules-dev/transactrules-api:latest

gcloud docker -- push us.gcr.io/transact-rules-dev/transactrules-api
```

# Kubernetes deployment


To create SQL Server password and store in Kubernetes (mikikube or GKE):
```bash
kubectl create secret generic mssql --from-literal=SA_PASSWORD="***********"
```

## To deploy to Minikube:

To install minikube on OSX:
```
brew cask install minikube
```

To start minikube:
```
minikube start
```

To switch kubectl to local minikube

```bash
kubectl config use-context minikube
```

To enable ingress on minikube:

```bash
minikube addons enable ingress
```

```bash
minikube ssh
```

Then in node shell

```
mkdir data
```

In command prompt:
```
kubectl create -f pv-minikube.yml
kubectl create -f mssql-data-claim-mini-kube.yml
kubectl create -f mssql-deployment-mini-kube.yml
kubectl create -f transactrules-api-deployment-mini-kube.yml
```

To deploy local admin dashboard:
```
kubectl create -f https://raw.githubusercontent.com/kubernetes/dashboard/master/src/deploy/recommended/kubernetes-dashboard.yaml
```

## To deploy to GKC:

To create dev-cluster

```bash
gcloud beta container --project "transact-rules-dev" clusters create "dev-cluster" --zone "northamerica-northeast1-a" --username "admin" --cluster-version "1.9.2-gke.1" --machine-type "n1-standard-1" --image-type "COS" --disk-size "40" --scopes "https://www.googleapis.com/auth/compute","https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/monitoring","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append" --num-nodes "3" --network "default" --enable-cloud-logging --enable-cloud-monitoring --subnetwork "default"
```

To connect to GKC:

```
gcloud container clusters get-credentials dev-cluster --zone northamerica-northeast1-a --project transact-rules-dev
```

or, once cluster is created and registered

```bash
kubectl config use-context gke_transact-rules-dev_northamerica-northeast1-a_dev-cluster
``` 

to create static IP:
```bash
gcloud compute addresses create transactrules-static-ip --global
```

In command prompt:
```
kubectl create -f mssql-data-claim-gke.yml
kubectl create -f mssql-deployment-gke.yml
kubectl create -f transactrules-api-deployment-gke.yml
```

## Run locally

[http://localhost:8080](http://localhost:8080)

To view APIs:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  
## Run in GKE

Find node public IP

[http://{node_ip}:8080](http://{node_ip}:8080)

To build new Jenkins docker image run command in this directory:

```bash
docker build https://github.com/igormusic/jenkins-with-maven-docker-gcloud.git -t trules_jenkins
```

To run local instance of Jenkins docker:

```bash
docker run --name build-server  -p 8081:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock trules_jenkins
```

