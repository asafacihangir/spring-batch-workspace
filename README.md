# Postgres to Mysql Migration using Spring Batch


1.  
```
http://localhost:8085/migration-api/api/student/create-data
```
Rest api'sine istek atılarak Postgres veritabanına veriler kaydedilir.

2.   
```
http://localhost:8085/migration-api/api/student/run-migrate-job
```
Rest api'sine istek atılarak postges veritabanındaki öğrenci verileri mysql veri tabanına kaydedilir.
