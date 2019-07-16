# Carrier - Results Analysis

### Set up Grafana Dashboards

PerfGun (Gatling) and PerfMeter (JMeter) have a custom InfluxDB Listener that is automatically added to test for reporting.

Carrier provides Grafana dashboards for both tools to monitor test results.

You can install InfluxDB and Grafana using a Carrier installer. In this case, all the necessary databases and dashboards will be set up automatically.

If you already have Grafana installed, you can import the [dashboards](https://github.com/carrier-io/carrier-io/tree/master/grafana_dashboards) and [data sources](https://github.com/carrier-io/carrier-io/tree/master/influx_datasources) from the Carrier Repository.

It is also necessary to have InfluxDB installed with the databases created (jmeter, gatling, comparison, telegraf, thresholds).

Example how to import PerfMeter dashboard using Curl:

```
curl -s https://raw.githubusercontent.com/carrier-io/carrier-io/master/grafana_dashboards/perfmeter_dashboards.json | curl -X POST "http://${FULLHOST}/grafana/api/dashboards/db" -u admin:${GRAFANA_PASSWORD} --header "Content-Type: application/json" -d @-
```

You can also import dashboards manually. But in this case you need to remove the "dashboard" key and its closing bracket from the JSON-file.

Screenshots of manual import of the dashboard are presented below.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Import_dashboard_1.png)

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Import_dashboard_2.png)

Example how to import PerfMeter data source using Curl:

```
curl -s https://raw.githubusercontent.com/carrier-io/carrier-io/master/influx_datasources/datasource_jmeter | curl -X POST "http://${FULLHOST}/grafana/api/datasources" -u admin:password --header "Content-Type: application/json" -d @-
```

You can also create a data source manually. Go to the Configuration menu "Data source", press "Add data source", select "InfluxDB" and fill in all the necessary fields.
 
Screenshots are presented below.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_1.png)

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_2.png)

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_3.png)

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Create_data_source_4.png)


### Dashboard overview

During the test execution all performance metrics are being saved to InfluxDB and displayed in Grafana.

Grafana allows us to review performance tests results using different filters which helps us to divide executed tests by parameters (e.g.: Scenario, Test type, Environment, Users count, etc.).

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/dashboard_params.png)

You can pick necessary time range in which you want to see your results. You can also set the "Refreshing every:" option. This allows you to automatically update the dashboard at specified intervals.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Dashboard_time_range.png)

After all filters set-up properly you will be able to see results of test execution.

The first block consists of 6 panels with overall information.

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
