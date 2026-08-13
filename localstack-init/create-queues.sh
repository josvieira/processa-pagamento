#!/bin/bash
set -e

echo "Criando fila DLQ: processa-pagamento-dlq.fifo"
awslocal sqs create-queue \
  --queue-name processa-pagamento-dlq.fifo \
  --attributes '{
    "FifoQueue": "true",
    "ContentBasedDeduplication": "false"
  }'

DLQ_ARN=$(awslocal sqs get-queue-attributes \
  --queue-url http://localhost:4566/000000000000/processa-pagamento-dlq.fifo \
  --attribute-names QueueArn \
  --query "Attributes.QueueArn" \
  --output text)

echo "DLQ ARN: $DLQ_ARN"

echo "Criando fila principal: processa-pagamento.fifo"
awslocal sqs create-queue \
  --queue-name processa-pagamento.fifo \
  --attributes "{
    \"FifoQueue\": \"true\",
    \"ContentBasedDeduplication\": \"false\",
    \"VisibilityTimeout\": \"30\",
    \"RedrivePolicy\": \"{\\\"deadLetterTargetArn\\\":\\\"$DLQ_ARN\\\",\\\"maxReceiveCount\\\":\\\"5\\\"}\"
  }"

echo "Filas criadas com sucesso."
awslocal sqs list-queues