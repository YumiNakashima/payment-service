package br.com.fiap.techchallenge.paymentservice.dto

import br.com.fiap.techchallenge.paymentservice.domain.OrderPayment
import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentView(
    @Id val id: String?,
    val orderId: String,
    val amount: BigDecimal,
    val status: String,
    val qrCode: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun fromDomain(orderPayment: OrderPayment): PaymentView {
            return PaymentView(
                id = orderPayment.id,
                orderId = orderPayment.orderId,
                amount = orderPayment.amount,
                status = orderPayment.status,
                qrCode = orderPayment.qrCode,
                createdAt = orderPayment.createdAt,
                updatedAt = orderPayment.updatedAt
            )
        }
    }
}