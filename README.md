# transactrules-accounts

This project depends on dynamodb-transactions, which does not exist on public Maven repo.

In order to build you need to do following:

```bash
git clone https://github.com/awslabs/dynamodb-transactions

cd dynamodb-transactions

mvn clean package
```


To build docker image:

```bash
mvn clean install -Dmaven.test.skip=true dockerfile:build
```

To run docker image:
```bash
docker run -d  --net="host" -p 8080:8080 -e DATASOURCE_URL="jdbc:sqlserver://localhost;databaseName=accounts" \
           -e DATASOURCE_USERNAME=sa \
           -e DATASOURCE_PASSWORD=TVMdev2018 \
           -e DATASOURCE_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver \
           -e JPA_SHOWSQL=true \
           -e HIBERNATE_DIALECT=org.hibernate.dialect.SQLServer2012Dialect \
           -e HIBERNATE_DDL_AUTO=none \
           transactrules/api
```

where your_key and your_secret refer to AWS account credentials with DynamoDB permissions to create and delete tables, and CRUD on table rows.

To push Google to repository:

```bash
docker tag transactrules/api:latest us.gcr.io/transact-rules-dev/transactrules-api:latest

gcloud docker -- push us.gcr.io/transact-rules-dev/transactrules-api
```

To view APIs:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  
