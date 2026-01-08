package br.com.fiap.techchallenge.paymentservice.messaging

import io.awspring.cloud.sqs.operations.SqsTemplate
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class PaymentStatusPublisher(
    private val sqsTemplate: SqsTemplate,
    @Value("\${cloud.aws.sqs.queue.url}") private val queueUrl: String
) {
    fun notifyOrder(orderId: String, status: String) {
        val message = mapOf("orderId" to orderId, "status" to status)
        sqsTemplate.send(queueUrl, message)
        logger.info { "Message sent - $message" }
    }
}