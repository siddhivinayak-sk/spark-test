#### Spark Cluster Setup on Windows
##### Below are steps to setup Spark Cluster on Windows (local mode)

1. Install Java 11
2. Download Spark with Pre-built for Apache Hadoop: https://spark.apache.org/downloads.html
3. Extract tgz file to directory like: C:\sandeep\sandbox\spark, content will be extracted in directory spark-3.5.4-bin-hadoop3
4. Set environment variable 
   ```
   SPARK_HOME=C:\sandeep\sandbox\spark\spark-3.5.4-bin-hadoop3
   PATH=%PATH%;%SPARK_HOME%\bin;%SPARK_HOME%\sbin
   ```
5. Start Master: 
   ```
   spark-class org.apache.spark.deploy.master.Master --host localhost
   ```
6. Start workers:
   ```
    spark-class org.apache.spark.deploy.worker.Worker spark://localhost:7077 --host localhost
   ```
7. Launch Spark Master Web Console: http://localhost:8080/
8. Submit a spark app
   spark-submit --class com.sk.spark.jobs.WordCountJob --master spark://localhost:7077 --executor-memory 5G --total-executor-cores 2 SparkJobs-0.0.1-SNAPSHOT.jar input.txt output.txt

##### References
- https://spark.apache.org/docs/latest/spark-standalone.html
- https://spark.apache.org/docs/latest/submitting-applications.html
- https://dzone.com/articles/the-magic-of-apache-spark-in-java-1
