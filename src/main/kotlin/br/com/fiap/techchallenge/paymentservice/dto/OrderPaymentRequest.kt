package br.com.fiap.techchallenge.paymentservice.dto

import java.math.BigDecimal

data class OrderPaymentRequest(
    val orderId: String,
    val amount: BigDecimal,
)
