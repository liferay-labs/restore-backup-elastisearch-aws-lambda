# AWS Lambda for listing Elasticsearch backups:

  A parameter of type `RestoreBackupRequest` must be provided to the Lambda.

```json
{
 "host": "Endpoint of the aws elasticsearch domain",
 "bucket": "Bucket repository name",
 "backup": "Backup Id"
}
```

Actions performed in Elasticsearch:

- [Snapshot And Restore](https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-snapshots.html#modules-snapshots)

