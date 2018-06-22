# AWS Lambda for listing Elasticsearch backups:

  A parameter of type `RestoreBackupRequest` must be provided to the Lambda.

```json
{
 "host": "endpoint of the aws elasticsearch domain",
 "bucket": "Snapshot repository name",
 "backup": "Backup/snapshot Id"
}
```