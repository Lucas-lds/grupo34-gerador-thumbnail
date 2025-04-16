#!/bin/bash
echo "🔥 Executando entrypoint customizado..."

sleep 5

awslocal s3 mb s3://meu-bucket-de-videos || true

exec /usr/bin/localstack-entrypoint.sh
