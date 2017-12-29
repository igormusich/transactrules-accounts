# transactrules-accounts

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

To view APIs:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  