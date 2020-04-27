# spring-prometheus

#####**Start environment:**
  
`build clean bootJar`  
`cd ENV`  
`docker-compose up -d`  

check start application:  
`docker logs webapp`

_Controller metrics:_  
REQUESTS_TOTAL_ADD_PERSON  
REQUESTS_TOTAL_DELETE_PERSON  

_Service metrics:_  
SERVICE_PERSON_GET_BY_ID  
SERVICE_PERSON_GET_ALL  
SERVICE_PERSON_GET_ALL_TIMER  
SERVICE_PERSON_REMOVE  
SERVICE_PERSON_SAVE_UPDATE  

_Datasource metrics:_  
DATA_SOURCE_STATUS

#
**ACTUATOR**  
View metrics: _[http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)_  

#
**PROMETHEUS**  
View status: _[http://localhost:9090/targets](http://localhost:9090/targets)_  

#
**GRAFANA**  
Home page: _[http://localhost:3000](http://localhost:3000)_  
Login / Password: admin / admin

1\. add data source _[http://localhost:3000/datasources](http://localhost:3000/datasources)_  :  
 - choose Prometheus  
 - access: browser  
 
2\. import dashboard _[http://localhost:3000/dashboard/import](http://localhost:3000/dashboard/import)_ :
- for monitoring jvm (webapp) select dashboard "jvm-micrometer_rev9.json"
- for monitoring postgres select dashboard "postgresql-statistics_rev1.json"  

3\. view metrics JVM(Micrometer): 
- choose dashboard "JVM(Micrometer)" in Grafana
- load collections in postman (ENV/prometheus.postman_collection.json)
- in postman run "Runner":
    - collection: prometheus
    - iterations: 1000
    - delay: 1 ms

4\. view metrics postgres: 
- choose dashboard "PostgreSQL Statistics" in Grafana
- docker exec -it postgres /bin/bash  
- pgbench -i -s 100 -U postgres testdatabase  
- pgbench -U postgres -c 50 -j 2 -P 10 -T 180 testdatabase

#
###API
#####Get all persons:
**GET** _[http://localhost:8080/person](http://localhost:8080/person)_

#####Get person by id:
**GET** _[http://localhost:8080/person/1/](http://localhost:8080/person/1)_

#####Update person:
**POST** _[http://localhost:8080/person](http://localhost:8080/person)_

body:  
{"firstName":"AAA", "lastName":"BBB", "email":"aaa@bbb.com"}

#####Update person:
**PUT** _[http://localhost:8080/person](http://localhost:8080/person)_

body:  
{"firstName":"AAA", "lastName":"BBB", "email":"aaa@bbb.com"}  

#####Delete person:
**DELETE** _[http://localhost:8080/person](http://localhost:8080/person)_


#
**_postgres benchmark:_**  
1. create and populate a test database with several tables and dummy data  
`pgbench -i -s 100 -U postgres testdatabase`  

**  
_**-i** - initializes the benchmark database using tables and their dummy data_  
_**-s** - sets a scale factor of 100, which multiplies the size of the table by 100_

2. run test:
`pgbench -U postgres -c 50 -j 2 -P 10 -T 180 testdatabase`

**  
_**-c** - the number of simultaneous clients or database sessions to be simulated;_  
_**-j** - the number of worker threads that pgbench will use during the test;_  
_**-P** - displays progress and metrics every 10 seconds;_  
_**-T** - run the test for 180 seconds._
