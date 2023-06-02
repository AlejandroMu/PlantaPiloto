#!/bin/bash
export PGPASSWORD="postgres"
psql -U postgres -h xhgrid2 -c "ALTER DATABASE assets CONNECTION LIMIT 0;"
psql -U postgres -h xhgrid2 -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'assets' AND pid <> pg_backend_pid();"
psql -U postgres -h xhgrid2 -v user=ingswi40 -v password=IngSwI40PWD -f user-db.sql
export PGPASSWORD="IngSwI40PWD"
psql -U ingswi40 -h xhgrid2 -d assets -f model.sql
psql -U ingswi40 -h xhgrid2 -d assets -f model_inserts.sql