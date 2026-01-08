package br.com.fiap.techchallenge.paymentservice.dto

import br.com.fiap.techchallenge.paymentservice.domain.Payment
import br.com.fiap.techchallenge.paymentservice.domain.PaymentStatus
import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentView(
    @Id val id: String?,
    val orderId: String,
    val value: BigDecimal,
    val status: PaymentStatus,
    val qrCode: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun fromDomain(payment: Payment): PaymentView {
            return PaymentView(
                id = payment.id,
                orderId = payment.orderId,
                value = payment.value,
                status = payment.status,
                qrCode = payment.qrCode,
                createdAt = payment.createdAt,
                updatedAt = payment.updatedAt
            )
        }
    }
}