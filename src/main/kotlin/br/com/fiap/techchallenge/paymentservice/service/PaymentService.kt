package br.com.fiap.techchallenge.paymentservice.service

import br.com.fiap.techchallenge.paymentservice.client.MercadoPagoClient
import br.com.fiap.techchallenge.paymentservice.domain.OrderPayment
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoWebhookPayload
import br.com.fiap.techchallenge.paymentservice.dto.OrderPaymentRequest
import br.com.fiap.techchallenge.paymentservice.messaging.PaymentStatusPublisher
import br.com.fiap.techchallenge.paymentservice.repository.OrderPaymentRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Service
class PaymentService(
    private val repository: OrderPaymentRepository,
    private val mercadoPagoClient: MercadoPagoClient,
    private val publisher: PaymentStatusPublisher
) {

    fun createPayment(request: OrderPaymentRequest): OrderPayment {
        logger.info { "Creating payment for order ${request.orderId}" }

        val pendingOrderPayment = OrderPayment(
            orderId = request.orderId,
            amount = request.amount,
            status = "created",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        var payment = repository.save(pendingOrderPayment)

        val mpResponse = mercadoPagoClient.createQrCodeOrder(request.orderId, request.amount)
            ?: throw RuntimeException("Error generating QR payment")

        logger.info { "MP Response: $mpResponse"}

        payment = payment.copy(
            mercadoPagoId = mpResponse.id.toString(),
            qrCode = mpResponse.typeResponse?.qrData ?: "PIX-ESTATICO-DO-CAIXA-SUC001POS001",
        )

        return repository.save(payment)
    }


    fun processWebhook(payload: MercadoPagoWebhookPayload) {
        if (payload.type != "order") return

        val infoMp = mercadoPagoClient.fetchOrder(payload.data?.id!!) ?: return

        val orderPayment = repository.findByMercadoPagoId(payload.data.id)

        if (orderPayment != null) {
            if (orderPayment.status != infoMp.status) {
                logger.info { "Found order status update for orderId: ${orderPayment.orderId} with mpId ${payload.data.id}" }

                orderPayment.status = infoMp.status!!
                orderPayment.updatedAt = LocalDateTime.now()
                repository.save(orderPayment)


                publisher.notifyOrder(orderPayment.orderId, infoMp.status!!)
            } else {
                logger.info {"No changes in order ${orderPayment.orderId}. Ignoring duplicate." }
            }
        } else {
            logger.info { "Order ${payload.data.orderId} not found in local database." }
        }
    }

}