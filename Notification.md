# Carrier - Notification and Alerting

Carrier provides automatic notification of test results.

You can receive a notification in:

* Email
* Slack channel
* MS Teams chat
* Telegram channel

To do this, you can use the [AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/welcome.html) or [Galloper](https://github.com/carrier-io/galloper) - docker container that Carrier provides.

### Create a task in AWS Lambda

In order to receive notifications using AWS Lambda, you need to create a task for this.

Go to [AWS Lambda](https://console.aws.amazon.com/lambda) and click "Create function" button.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/AWS_Create_function_1.png)

Then select "Author from scratch" and specify function name and runtime (should be python 3.7). Click "Create function" button.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/AWS_Create_function_2.png)

In the "Function code" section you need to specify a lambda handler - lambda_function.lambda_handler.

You also need to upload a zip file with the packed function and click "Save" button.

You can find lambda function that provide email notifications [here](https://github.com/carrier-io/galloper/tree/master/lambdas/email_notifications/package) 
and for chat notifications - [here](https://github.com/carrier-io/galloper/tree/master/lambdas/chat_notifications/package)


![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/AWS_Create_function_3.png)

Then in Designer menu click "Add trigger" button.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/AWS_Create_function_4.png)

Choose "API Gateway" trigger. Create a new API and configure the security mechanism for your API endpoint.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/AWS_Create_function_5.png)

Now you have the API endpoint to execute the function.

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/AWS_Create_function_6.png)

```
curl -XPOST -H "Content-Type: application/json"
    -d '{"param1": "value1", "param2": "value2", ...}' <API_endpoint>
```

A list of all valid parameters that can be passed to the function is provided in the sections "Email notifications" and "Chat notifications" below.


### Create a task in Galloper

You can use notifications with Galloper. This is a docker container that works just like the AWS Lambda.

Example of a container start command:

```
docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -e "REDIS_HOST=<redis_host>" \ 
       -e "REDIS_DB=2" -e "CPU_CORES=1" -e "APP_HOST=<Galloper_host>:5000" -p 5000:5000  getcarrier/galloper:latest
```

It require redis to be running somewhere. Simple container can be used for that

```
docker run -d -p 6379:6379 --name carrier-redis \
	   redis:5.0.2 redis-server --requirepass $REDIS_PASSWORD
```

To run the lambda function, you need to create a task in the Galloper and specify a lambda handler in it - lambda_function.lambda_handler.

The Lambda functions for notifications are written in python 3.7, so you need to specify this in the "Runtime" field when creating the task.

You also need to upload a zip file with the packed function.

You can find lambda function that provide email notifications [here](https://github.com/carrier-io/galloper/tree/master/lambdas/email_notifications/package) 
and for chat notifications - [here](https://github.com/carrier-io/galloper/tree/master/lambdas/chat_notifications/package)

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/Galloper_task_creation.png)

Example how to invoke a task using CURL:

```
curl -XPOST -H "Content-Type: application/json"
    -d '{"param1": "value1", "param2": "value2", ...}' <host>:5000/<webhook>
```

`<host>` - Galloper host DNS or IP

`<webhook>` - POST Hook to call your task

A list of all valid parameters that can be passed to the function is provided in the sections "Email notifications" and "Chat notifications" below.

### Email notifications

Example of what notification looks like for backend-performance test:

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/API_email.png)

Example of what notification looks like for UI-performance test:

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/UI_email.png)

You can use curl to invoke a task. The required parameters can be passed with the option -d.

List of available parameters:

`'test': '<test_name>'` - required for all type of notifications

`'test_suite': '<ui_suite_name>'` - required for UI email notifications

`'test_type': '<test_type>'` - required for API email notifications

`'users': '<count_of_vUsers>'` - required for all type of notifications

`'influx_host': '<influx_host_DNS_or_IP>'` - required for all type of notifications

`'smpt_user': '<smpt_user_who_will_send_email>'` - required for all type of notifications

`'smpt_password': '<password>'` - required for all type of notifications

`'user_list': '<list of recipients>'` - required for all type of notifications

`'notification_type': '<test_type>'` - should be 'ui' or 'api'


`'smpt_host': 'smtp.gmail.com'` - optional, default - 'smtp.gmail.com'
 
`'smpt_port': 465` - optional, default - 465
 
`'influx_port': 8086` - optional, default - 8086

`'influx_thresholds_database': 'thresholds'` - optional, default - 'thresholds'

`'influx_ui_tests_database': 'perfui'` - optional, default - 'perfui'

`'influx_comparison_database': 'comparison'` - optional, default - 'comparison'

`'influx_user': ''` - optional, default - ''

`'influx_password': ''` - optional, default - ''

`'test_limit': 5` - optional, default - 5

`'comparison_metric': 'pct95'` - optional, only for API notifications, default - 'pct95'


### Chat notifications

Example of what notification looks like for backend-performance test:

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/api_chat.png)

Example of what notification looks like for UI-performance test:

![alt text](https://raw.githubusercontent.com/hunkom/tests/master/images/ui_chat.png)

You can use curl to invoke a task. The required parameters can be passed with the option -d.

List of available parameters:

Required parameters

`'test': '<test_name>'` - required for all type of notifications

`'test_suite': '<ui_suite_name>'` - required for ui chat notifications

`'test_type': '<test_type>'` - required for api chat notifications

`'influx_host': '<influx_host_DNS_or_IP>'` - required for all type of notifications

`'notification_type': '<test_type>'` - should be 'ui' or 'api'


Optional parameters

`'influx_port': 8086` - default - 8086

`'influx_thresholds_database': 'thresholds'` - default - 'thresholds'

`'influx_ui_tests_database': 'perfui'` - default - 'perfui'

`'influx_comparison_database': 'comparison'` - default - 'comparison'

`'influx_user': ''` - default - ''

`'influx_password': ''` - default - ''

`'users': '<count_of_vUsers>'` - default - 1

`'comparison_metric': 'pct95'` - only for api test notifications, default - 'pct95'

 
 Specific parameters for chats
 
 Slack:
 
 `'slack_channel': '<#channel_name>'`
 
 `'slack_token': '<slack_bot_token>'`
 
 [How to create slack bot](https://get.slack.help/hc/en-us/articles/115005265703-Create-a-bot-for-your-workspace)
 
 Telegram:
 
 `'telegram_channel_id': '<channel_id>'`
 
 `'telegram_bot_token': '<bot_token>'`
 
 [How to create telegram bot](https://core.telegram.org/bots)
 
 MS Teams:
 
 `'ms_teams_web_hook': '<channel_web_hook>'`
 
 [How to create webhook for MS Teams](https://docs.microsoft.com/en-us/microsoftteams/platform/concepts/connectors/connectors-using)
