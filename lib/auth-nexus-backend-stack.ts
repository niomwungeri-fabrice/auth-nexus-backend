import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as rds from 'aws-cdk-lib/aws-rds';
import { repoName } from '../utils/constants';

export class AuthNexusBackendStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // Create a VPC
    const vpc = new ec2.Vpc(this, `${repoName}-vpc`, {
      maxAzs: 2, // Deploy across 2 Availability Zones
    });

    // Create an RDS PostgreSQL database
    const dbInstance = new rds.DatabaseInstance(this, `${repoName}-rds`, {
      engine: rds.DatabaseInstanceEngine.postgres({
        version: rds.PostgresEngineVersion.VER_15,
      }),
      vpc,
      vpcSubnets: {
        subnetType: ec2.SubnetType.PRIVATE_WITH_EGRESS,
      },
      instanceType: ec2.InstanceType.of(
          ec2.InstanceClass.BURSTABLE4_GRAVITON,
          ec2.InstanceSize.SMALL
      ),
      credentials: rds.Credentials.fromGeneratedSecret('authnexusadmin'),
      multiAz: false, // Single instance for now
      allocatedStorage: 20, // 20 GB storage
      removalPolicy: cdk.RemovalPolicy.DESTROY, // Ensure resources are destroyed with CDK destroy
      deletionProtection: false, // Allow deletion for testing purposes
    });

    // Output the database connection details
    new cdk.CfnOutput(this, `${repoName}-db-endpoint`, {
      value: dbInstance.dbInstanceEndpointAddress,
      description: 'Database endpoint for Auth-Nexus Backend',
    });
  }
}
