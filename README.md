# Carrier - Results Analysis using Grafana + InfluxDB

### Overview

**PerfGun (Gatling)** and **PerfMeter (JMeter)** have a custom preinstalled InfluxDB listeners what gives the ability for reporting data in **Grafana**. 
Carrier provides customized Grafana dashboards for:
* PerfGun Gatling
* PerfMeter Jmeter
* Comparison dashboard for both tools which helps to compare two different test runs

### Set up Grafana Dashboards
##### Preconditions

You can install InfluxDB and Grafana using a Carrier installer what is easyest way to get all the functionality up in a shor term. In this case, all the necessary databases and dashboards will be set automatically.

If you already have Grafana installed, you should import the [dashboards](https://github.com/carrier-io/carrier-io/tree/master/grafana_dashboards) and [data sources](https://github.com/carrier-io/carrier-io/tree/master/influx_datasources) from the Carrier Repository. 
It is also necessary to have InfluxDB installed with the specific databases created: 
* jmeter
* gatling
* comparison
* telegraf
* thresholds

##### Manual installation
Example how to import PerfMeter dashboard using CURL:

```
curl -s https://raw.githubusercontent.com/carrier-io/carrier-io/master/grafana_dashboards/perfmeter_dashboards.json | curl -X POST "http://${FULLHOST}/grafana/api/dashboards/db" -u admin:${GRAFANA_PASSWORD} --header "Content-Type: application/json" -d @-
```

You can also import dashboards manually from Grafana user interface. But in this case you need to remove the "dashboard" key and its closing brackets from the dashboard JSON-file.

Screenshots of manual import of the dashboard are presented below.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Import_dashboard_1.png)

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Import_dashboard_2.png)

Example how to import PerfMeter data source using Curl:

```
curl -s https://raw.githubusercontent.com/carrier-io/carrier-io/master/influx_datasources/datasource_jmeter | curl -X POST "http://${FULLHOST}/grafana/api/datasources" -u admin:password --header "Content-Type: application/json" -d @-
```

As for dashboards there is ability to create data sources manually from Grafana user interface. Go to the Configuration menu "Data source", press "Add data source", select "InfluxDB" and fill in all the necessary fields.
 
Screenshots with the data source configuration are presented below.

Go to "Data Sources"
![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_1.png)

Click on "Add data source"
![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_2.png)

Select "InfluxDB"
![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_3.png)

Set up the url for InfluxDB and Database
![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_4.png)

**The same works for PerfGun Dashboard**

### Dashboard overview

During the test execution all performance metrics are being saved to InfluxDB and displayed in Grafana.

Grafana allows us to review performance tests results using different filters which helps us to divide executed tests by parameters (e.g.: Simulation name, Test type, Environment, Users count, etc.).

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/dashboard_params.png)

You can pick necessary time range in which you want to see your results. You can also set the "Refreshing every:" option. This allows you to automatically update the dashboard at specified intervals.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Dashboard_time_range.png)

After all filters set-up properly you will be able to see results of test execution.

JMeter and Gatling dashboards have the same structure.

The first block consists of panels with overall information.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Dashboard_Overall_info.png)

The second block of the dashboard named “Response Times Over Time” contains a chart where you can see Response time for all requests or only for chosen in the right side of the block.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Dashboard_response_times.png)

The third block of the dashboard, called "Throughput", contains a graph showing the change in throughput over time.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Dashboard_throughput.png)

The last block of the dashboard called "Summary table" contains a table consisting of detailed statistics for each request. In case of empty table refresh the page.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Dashboard_summary_table.png)

### Capacity test

The capacity of a system is the total workload it can handle without violating predetermined key performance acceptance criteria.

A capacity test ran to determine your server’s saturation point, saturation area and failure point.

Capacity testing ran in conjunction with capacity planning, which you use to plan for future growth, such as an increased user base or increased volume of data.
For example, to accommodate future loads, you need to know how many additional resources (such as processor capacity, memory usage, disk capacity, or network bandwidth) are necessary to support future usage levels.

Capacity testing helps you to identify a scaling strategy in order to determine whether you should scale up or scale out.

Saturation point - the point when system throughput stops to grow, while pressure (concurrent users) continue to grow.
Saturation point should be used to determine if hardware resources is planned carefully, e.g. if saturation come w/o all system resources utilized, it may be considered as ineffective resource usage.
Saturation point should be used as an input in planning of scaling strategy for a particular system.

Saturation area is time from saturation point to a failure point, while pressure on a system is growing. Saturation area is a time system have to scale up to avoid reaching failure point.

Failure point is a point when % of failed requests or % of responses exceeded maximum expected time reached the allowed limit (e.g. 1%). 
This point considered to be user attrition point - users stops using the system in this state because of unsatisfactory experience.

A screenshot of the saturation point, saturation area and failure point is presented below.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Result_analysis_saturation_point.png)


### Tests comparison

There are some cases when you need to compare performance results between different test executions for each request.

Carrier provides Grafana dashboard for this purpose. Setup is performed automatically if you are using the Carrier installer.
Or you can import it yourself if you already have a Grafana installed.

Example how to import Comparison dashboard using Curl:

```
curl -s https://raw.githubusercontent.com/carrier-io/carrier-io/master/grafana_dashboards/performance_comparison_dashboard.json | curl -X POST "http://${FULLHOST}/grafana/api/dashboards/db" -u admin:${GRAFANA_PASSWORD} --header "Content-Type: application/json" -d @-
```

Grafana allows us to review performance test results using different filters which helps us to divide executed tests by parameters (e.g.: Simulation name, Duration, User count, etc.).

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Comparison_params.png)

Once all the filters have been configured correctly, you will be able to see a comparison of the results for the selected tests.

There are 3 main sections on the dashboard.

The first one shows a chart of the response time with a comparison of the selected tests for each request.
To the right of this chart you can select one of the tests and mark it as baseline. 

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Comparison_response_times_over_time.png)

The second section contains tables with the distribution of all requests by response code.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Comparison_response_codes.png)

The last section contains tables comparing the response time for each request.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Comparison_response_time_table.png)


### Load generator monitoring

Information about resource utilization on the load generator side is collected in InfluxDB. 
Carrier provides a Grafana dashboard to monitor these resources. Setup is performed automatically if you are using the Carrier installer.
Or you can import it yourself if you already have a Grafana installed.

Example how to import Comparison dashboard using Curl:

```
curl -s https://raw.githubusercontent.com/carrier-io/carrier-io/master/grafana_dashboards/telegraph_dashboard.json | curl -X POST "http://${FULLHOST}/grafana/api/dashboards/db" -u admin:${GRAFANA_PASSWORD} --header "Content-Type: application/json" -d @-
```

This dashboard contains summary information about the system and resource utilization charts.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Telegraf_summary.png)

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/telegraf_system.png)