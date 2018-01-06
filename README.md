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
mvn clean install dockerfile:build
```

To run docker image:
```bash
docker run --name trules-accounts \
           -p 8080:8080 \
           -e ACCESS_KEY=your_key \
           -e SECRET_KEY=your_secret \
           -e DROP_DB=false \
           transactrules/accounts
```

where your_key and your_secret refer to AWS account credentials with DynamoDB permissions to create and delete tables, and CRUD on table rows.

To push ACS to repository:

```bash
docker tag transactrules/accounts:latest 460415261843.dkr.ecr.ca-central-1.amazonaws.com/transactrules:latest

docker push 460415261843.dkr.ecr.ca-central-1.amazonaws.com/transactrules:latest
```

To view APIs:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  
