#!/bin/bash
export PGPASSWORD="postgres"
host=$1
psql -U postgres -h $host -c "ALTER DATABASE assets CONNECTION LIMIT 0;"
psql -U postgres -h $host -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'assets' AND pid <> pg_backend_pid();"
psql -U postgres -h $host -v user=ingswi40 -v password=IngSwI40PWD -f user-db.sql
export PGPASSWORD="IngSwI40PWD"
psql -U ingswi40 -h $host -d assets -f model.sql
psql -U ingswi40 -h $host -d assets -f model_inserts.sql
