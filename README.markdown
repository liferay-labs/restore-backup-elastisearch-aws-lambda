# AWS Lambda for listing Elasticsearch backups:

  A parameter of type `RestoreBackupRequest` must be provided to the Lambda.

```json
{
 "host": "endpoint of the aws elasticsearch domain",
 "bucket": "Snapshot repository name",
 "backup": "Backup/snapshot Id"
}
```

Restore Backup DEV

```json
{
  "host": "https://search-pulpo-elasticsearch-dev-2xp4jucrau2hcqsowbsaf5vnfu.us-east-1.es.amazonaws.com",
  "bucket": "us-east-1-pulpo-elasticsearch-dev-backup",
  "backup": "20180622112115"
}
```