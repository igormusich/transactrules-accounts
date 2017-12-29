# transactrules-accounts

to build docker image:

```bash
mvn clean install dockerfile:build
```

to run docker image:
```bash
docker run --name trules-accounts -p 8080:8080 -e ACCESS_KEY=your_key -e SECRET_KEY=your_secret transactrules/accounts
```
