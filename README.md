# Build docker image

To build docker image:

```bash
mvn clean install -Dmaven.test.skip=true dockerfile:build
```

Prior to running local docker image you would need to deploy SQL server to local minikube
To run docker image:
```bash
docker run -d  --net="host" -p 8080:8080 -e DATASOURCE_URL="jdbc:sqlserver://192.168.99.101:30001;databaseName=accounts" \
           -e DATASOURCE_USERNAME=sa \
           -e DATASOURCE_PASSWORD=TVMdev2018 
```

To push Google to repository:

```bash
docker tag transactrules/api:latest us.gcr.io/transact-rules-dev/transactrules-api:latest

gcloud docker -- push us.gcr.io/transact-rules-dev/transactrules-api
```

#  Kubernetes deployment

To create SQL Server password and store in Kubernetes (mikikube or GKE):
```bash
kubectl create secret generic mssql --from-literal=SA_PASSWORD="***********"
```

##To configure Minikube:

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

##To configure GKC:

To connect to GKC:

```
gcloud container clusters get-credentials dev-cluster --zone northamerica-northeast1-a --project transact-rules-dev
```

In command prompt:
```
kubectl create -f mssql-data-claim-gke.yml
kubectl create -f mssql-deployment-gke.yml
kubectl create -f transactrules-api-deployment-gke.yml
```

To view APIs:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  
