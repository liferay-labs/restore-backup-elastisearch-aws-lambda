# AWS Lambda for listing Elasticsearch backups:

  A parameter of type `RestoreBackupRequest` must be provided to the Lambda.

```json
{
 "host": "Endpoint of the aws elasticsearch domain",
 "bucket": "Bucket repository name",
 "backup": "Backup Id"
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

Actions performed in Elasticsearch:

- [Snapshot And Restore](https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-snapshots.html#modules-snapshots)

