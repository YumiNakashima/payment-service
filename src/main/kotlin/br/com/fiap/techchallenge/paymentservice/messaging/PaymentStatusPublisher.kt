package br.com.fiap.techchallenge.paymentservice.messaging

import io.awspring.cloud.sns.core.SnsTemplate
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class PaymentStatusPublisher(
    private val snsTemplate: SnsTemplate,
    @Value("\${cloud.aws.sns.topic.arn}") private val topicArn: String
) {
    fun notifyOrder(orderId: String, status: String) {
        val message = mapOf("orderId" to orderId, "status" to status)
        snsTemplate.convertAndSend(topicArn, message)
        logger.info { "Message sent - $message" }
    }
}