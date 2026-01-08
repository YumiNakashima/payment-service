provider "aws" {
  region = "us-east-1"
}

# Criação da fila
resource "aws_sqs_queue" "payment_status_queue" {
  name                      = "payment-status-queue"
  delay_seconds             = 0
  max_message_size          = 262144
  message_retention_seconds = 86400 # 1 dia
  receive_wait_time_seconds = 10
}

# Output da URL para configurar no application.yml depois
output "sqs_url" {
  value = aws_sqs_queue.payment_status_queue.id
}