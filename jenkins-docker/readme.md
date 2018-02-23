#Jenkins docker setup

To build new Jenkins docker image run command in this directory:

```bash
docker build -t trules_jenkins .
```

To publish latest docker image to DockerHub:
```bash
docker tag trules_jenkins  igormusic/jenkins-with-maven:latest
docker push igormusic/jenkins-with-maven:latest
```

To run local instance of Jenkins docker:

```bash
docker run --name build-server  -p 8081:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock trules_jenkins
```

Docker image will be mapped to local volume jenkins_home.

You can access Jenkins from: [http://localhost:8081/](http://localhost:8081/)

When you run first time, you will need to setup Jenkins user name and password. 

To configure access from Jenkins to Google cloud run in docker shell:
```bash
 gcloud auth login
 gcloud config set project transact-rules-dev
```

