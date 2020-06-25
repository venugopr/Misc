from datetime import datetime

import boto3
import json

sts_client = boto3.client('sts')
sts_response = sts_client.assume_role(
    # The below role arn will be the role created in the other account
    RoleArn='arn:aws:iam::987654321987:role/CloudWatch-CrossAccountSharingRole',
    RoleSessionName='cloudwatch-share-acct-session'
)
# Get the access_key_id, secret_access_key, session_t
access_key_id = sts_response["Credentials"]['AccessKeyId']
secret_access_key = sts_response["Credentials"]['SecretAccessKey']
session_token = sts_response["Credentials"].get('SessionToken', '')

# First get the list of EC2 instances
ec2_client = boto3.client('ec2',
                          aws_access_key_id=access_key_id,
                          aws_secret_access_key=secret_access_key,
                          aws_session_token=session_token
                          )
ec2_description = ec2_client.describe_instances()
for reservation in ec2_description["Reservations"]:
    for instance in reservation["Instances"]:
        instance_id = instance['InstanceId']

        cloudwatch = boto3.client('cloudwatch',
                                  aws_access_key_id=access_key_id,
                                  aws_secret_access_key=secret_access_key,
                                  aws_session_token=session_token
                                  )
        metrics_response = cloudwatch.get_metric_data(
            MetricDataQueries=[
                {
                    "Id": "cpu",
                    "MetricStat": {
                        "Metric": {
                            "Namespace": "AWS/EC2",
                            "MetricName": "CPUUtilization",
                            "Dimensions": [
                                {
                                    "Name": "InstanceId",
                                    "Value": instance_id
                                }
                            ]
                        },
                        "Period": 3600, # The retrieval interval is 1 hour. So aggregate of 1 hour will be output
                        "Stat": "Average",
                        "Unit": "Percent"
                    },
                    "Label": "CPUUtilizationResponse",
                    "ReturnData": True
                }
            ],
            # Date in formate datetime(2020, 6, 16)
            StartTime=datetime(2020, 6, 23), # Generate the date values dynamically
            EndTime=datetime(2020, 6, 24)
        )
        # Now you can work on this metrics response.
        print(json.dumps(metrics_response, indent=2, default=str))
