package br.com.fiap.techchallenge.paymentservice.dto

import java.math.BigDecimal

data class OrderPaymentDto(
    val orderId: String,
    val value: BigDecimal
)
