package br.com.fiap.techchallenge.paymentservice.controller

import br.com.fiap.techchallenge.paymentservice.domain.PaymentStatus
import br.com.fiap.techchallenge.paymentservice.messaging.PaymentStatusPublisher
import br.com.fiap.techchallenge.paymentservice.repository.PaymentRepository
import br.com.fiap.techchallenge.paymentservice.service.MercadoPagoService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/payments/webhook")
class WebhookController(
    private val publisher: PaymentStatusPublisher,
    private val repository: PaymentRepository,
    private val mercadoPagoService: MercadoPagoService,
) {

    @PostMapping
    fun receiveNotification(@RequestParam("id") id: String, @RequestParam("topic") topic: String) {

        if (topic == "payment") {
            logger.info {"Notification received for Mercado Pago payment ID: $id"}

            val mpPaymentInfo = mercadoPagoService.fetchPayment(id)

            if (mpPaymentInfo != null && mpPaymentInfo.status == "approved") {

                val payment = repository.findByMercadoPagoId(id)

                if (payment != null) {
                    if (payment.status != PaymentStatus.APPROVED) {

                        payment.status = PaymentStatus.APPROVED
                        payment.updatedAt = LocalDateTime.now()
                        repository.save(payment)

                        logger.info { "Payment approved for order: ${payment.orderId}" }

                        publisher.notifyOrder(payment.orderId, PaymentStatus.APPROVED.name)
                    }
                } else {
                    logger.info { "Payment $id not found " }
                }
            }
        }
    }
}