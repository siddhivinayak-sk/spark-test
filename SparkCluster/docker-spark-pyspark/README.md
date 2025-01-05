# Spark Cluster with Docker

A simple spark (PySpark) standalone cluster for your testing environment. 

The Docker compose will create the following containers:

| Container      | Exposed ports |
|----------------|---------------|
| spark-master   | 9090 7077     |
| spark-worker-1 | 9091          |
| spark-worker-2 | 9092          |
| demo-database  | 5432          |

# Installation

The following steps will make you run your spark cluster's containers.

## Pre requisites

- Docker installed
- Docker compose  installed

## Build the image

```sh
docker build -t cluster-apache-spark:3.0.2 .
```

## Run the docker-compose

The final step to create your test cluster will be to run the compose file:

```sh
docker-compose up -d
```

## Validate your cluster

Just validate your cluster accesing the spark UI on each worker & master URL.

### Spark Master

http://localhost:9090/

### Spark Worker 1

http://localhost:9091/

### Spark Worker 2

http://localhost:9092/

# Resource Allocation 

This cluster is shipped with three workers and one spark master, each of these has a particular set of resource
allocation(basically RAM & CPU cores allocation).

- The default CPU cores allocation for each spark worker is 1 core
- The default RAM for each spark-worker is 1024 MB
- The default RAM allocation for spark executors is 256mb
- The default RAM allocation for spark driver is 128mb
- If you wish to modify this allocations just edit the env/spark-worker.sh file

# Volumes Binding

Need to bind two volumes to the spark master and workers to make available your app's jars and data:

| Host Mount | Container Mount | Purposse                                                       |
|------------|-----------------|----------------------------------------------------------------|
| apps       | /opt/spark-apps | Used to make available your app's jars on all workers & master |
| data       | /opt/spark-data | Used to make available your app's data on all workers & master |

# Run Sample applications

## NY Bus Stops Data [Pyspark]

This programs just loads archived data from [MTA Bus Time](http://web.mta.info/developers/MTA-Bus-Time-historical-data.html) and apply basic filters using spark sql, the result are persisted into a postgresql table.

The loaded table will contain the following structure:

| latitude  | longitude  | time_received       | vehicle_id | distance_along_trip | inferred_direction_id | inferred_phase | inferred_route_id | inferred_trip_id                      | next_scheduled_stop_distance | next_scheduled_stop_id | report_hour         | report_date  |
|-----------|------------|---------------------|------------|---------------------|-----------------------|----------------|-------------------|---------------------------------------|------------------------------|------------------------|---------------------|--------------|
| 40.668602 | -73.986697 | 2014-08-01 04:00:01 | 469        | 4135.34710710144    | 1                     | IN_PROGRESS    | MTA NYCT_B63      | MTA NYCT_JG_C4-Weekday-141500_B63_123 | 2.63183804205619             | MTA_305423             | 2014-08-01 04:00:00 | 2014-08-01   |

To submit the app connect to one of the workers or the master and execute:

```sh
/opt/spark/bin/spark-submit --master spark://spark-master:7077 \
--jars /opt/spark-apps/postgresql-42.2.22.jar \
--driver-memory 1G \
--executor-memory 1G \
/opt/spark-apps/main.py
```

## MTA Bus Analytics[Scala]

This program takes the archived data from [MTA Bus Time](http://web.mta.info/developers/MTA-Bus-Time-historical-data.html) and make some aggregations on it, the calculated results are persisted on postgresql tables.

Each persisted table correspond to a particular aggregation:

| Table             | Aggregation                                                                                       |
|-------------------|---------------------------------------------------------------------------------------------------|
| day_summary       | A summary of vehicles reporting, stops visited, average speed and distance traveled(all vehicles) |
| speed_excesses    | Speed excesses calculated in a 5 minute window                                                    |
| average_speed     | Average speed by vehicle                                                                          |
| distance_traveled | Total Distance traveled by vehicle                                                                |

To submit the app connect to one of the workers or the master and execute:

```sh
/opt/spark/bin/spark-submit --deploy-mode cluster \
--master spark://spark-master:7077 \
--total-executor-cores 1 \
--class mta.processing.MTAStatisticsApp \
--driver-memory 1G \
--executor-memory 1G \
--jars /opt/spark-apps/postgresql-42.2.22.jar \
--conf spark.driver.extraJavaOptions='-Dconfig-path=/opt/spark-apps/mta.conf' \
--conf spark.executor.extraJavaOptions='-Dconfig-path=/opt/spark-apps/mta.conf' \
/opt/spark-apps/mta.jar
```

You will notice on the spark-ui a driver program and executor program running(In scala we can use deploy-mode cluster)


# Steps to connect and use a pyspark shell interactively

Follow the steps to run the docker-compose file. You can scale this down if needed to 1 worker. 

```sh
docker-compose up --scale spark-worker=1
docker exec -it docker-spark-cluster_spark-worker_1 bash
apt update
apt install python3-pip
pip3 install pyspark
pyspark
```
