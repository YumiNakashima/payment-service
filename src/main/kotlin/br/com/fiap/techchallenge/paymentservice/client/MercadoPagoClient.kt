package br.com.fiap.techchallenge.paymentservice.service

import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoOrderConfig
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoOrderItem
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoOrderPayment
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoOrderRequest
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoOrderResponse
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoOrderTransactions
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoPaymentResponse
import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoQrConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.math.BigDecimal
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Component
class MercadoPagoClient(
    @Value("\${mercadopago.token}") val token: String,
    @Value("\${mercadopago.notification-url}") val notificationUrl: String,
    @Value("\${mercadopago.user-id}") val userId: String,
) {

    private val restClient = RestClient.builder()
        .baseUrl("https://api.mercadopago.com/v1")
        .defaultHeader("Authorization", "Bearer $token")
        .build()

    fun createQrCodeOrder(orderId: String, amount: BigDecimal): MercadoPagoOrderResponse? {
        val request = MercadoPagoOrderRequest(
            totalAmount = amount,
            description = "Order $orderId",
            externalReference = orderId,
            config = MercadoPagoOrderConfig(
                qr = MercadoPagoQrConfig(externalPosId = "SUC001POS001")
            ),
            transactions = MercadoPagoOrderTransactions(
                payments = listOf(MercadoPagoOrderPayment(amount = amount))
            ),
            items = listOf(
                MercadoPagoOrderItem(title = "Order item $orderId", unitPrice = amount)
            )
        )

        try {
            return restClient.post()
                .uri("/orders")
                .header("X-Idempotency-Key", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(MercadoPagoOrderResponse::class.java)

        } catch (e: Exception) {
            logger.error(e) { "error creating order $orderId" }
            return null
        }
    }

    fun fetchPayment(id: String): MercadoPagoPaymentResponse? {
        logger.info { "Fetch payment status for the id: $id" }

        return try {
            restClient.get()
                .uri("/payments/{id}", id)
                .retrieve()
                .body(MercadoPagoPaymentResponse::class.java)
        } catch (e: Exception) {
            logger.error { "Error trying to fetch payment on MP: ${e.message}" }
            null
        }
    }
}