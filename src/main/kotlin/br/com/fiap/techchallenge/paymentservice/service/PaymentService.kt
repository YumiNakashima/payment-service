package br.com.fiap.techchallenge.paymentservice.service

import br.com.fiap.techchallenge.paymentservice.domain.Payment
import br.com.fiap.techchallenge.paymentservice.dto.MpPaymentRequest
import br.com.fiap.techchallenge.paymentservice.dto.PaymentView
import br.com.fiap.techchallenge.paymentservice.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.math.BigDecimal

@Service
class PaymentService(
    private val repository: PaymentRepository,
    @Value("\${mercadopago.token}") private val token: String,
    @Value("\${mercadopago.notification-url}") private val notificationUrl: String
) {

    private val restClient = RestClient.builder()
        .baseUrl("https://api.mercadopago.com/v1")
        .defaultHeader("Authorization", "Bearer $token")
        .build()

    fun initiatePaymentForOrder(orderId: String, value: BigDecimal): PaymentView {
        // salva estado inicial
        val payment = repository.save(Payment(orderId = orderId, value = value))

        val request = MpPaymentRequest(
            transaction_amount = value,
            description = "Pedido $orderId",
            // Email fake para teste mas mudar depois para o que vem da request
            payer = MpPaymentRequest.MpPayer(email = "test_user_123@testuser.com"),
            notification_url = notificationUrl
        )

        val response = restClient.post()
            .uri("/payments")
            .body(request)
            .retrieve()
            .toEntity(String::class.java)
        // Dica: Crie DTOs para mapear a resposta e pegar o QR Code real

        // Aqui você atualizaria o 'pagamento' com o QR Code retornado na 'response'
        // payment.qrCode = response.body...

        val paymentResponse = repository.save(payment)
        return PaymentView.fromDomain(paymentResponse)
    }
}