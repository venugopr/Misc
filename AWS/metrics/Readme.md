Prerequisites
AWS CLI should be installed in the system. Install from this link https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html

The CLI needs to be configured with a valid AWS account credentials - aws configure

  Then enter the access-key-id, secret-key, region and file type (json)
  
Boto3 needs to be installed - pip install boto3

aws-crossacct-cloudwatch.py - This file has to code to retrieve the AWS CloudWatch metrics from another account. The related setting to give the role has to be done on the other account.

The steps to do the settings are mentioned in the following link: https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/Cross-Account-Cross-Region.html.


This link has more explanation about this code and topic

https://dzone.com/articles/cross-account-amazon-cloudwatch-metric-sharing
