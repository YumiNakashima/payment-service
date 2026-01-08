package br.com.fiap.techchallenge.paymentservice.dto

import java.math.BigDecimal

data class MpPaymentRequest(
    val transaction_amount: BigDecimal,
    val description: String,
    val payment_method_id: String = "pix",
    val payer: MpPayer,
    val notification_url: String?
) {
    data class MpPayer(val email: String)
}
