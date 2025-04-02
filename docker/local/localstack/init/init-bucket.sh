#!/bin/bash
echo "✅ Criando bucket no LocalStack..."
awslocal s3 mb s3://meu-bucket-de-videos
aws --endpoint-url=http://localhost:4566 s3 mb s3://meu-bucket-de-videos