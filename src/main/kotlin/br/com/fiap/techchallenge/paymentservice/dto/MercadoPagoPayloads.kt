package br.com.fiap.techchallenge.paymentservice.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MercadoPagoOrderRequest(
    val type: String = "qr",
    @JsonProperty("total_amount") val totalAmount: String,
    val description: String,
    @JsonProperty("external_reference") val externalReference: String,
    val config: MercadoPagoOrderConfig,
    val transactions: MercadoPagoOrderTransactions,
    val items: List<MercadoPagoOrderItem>
)

data class MercadoPagoOrderConfig(
    val qr: MercadoPagoQrConfig
)

data class MercadoPagoQrConfig(
    @JsonProperty("external_pos_id") val externalPosId: String,
    val mode: String = "dynamic"
)

data class MercadoPagoOrderTransactions(
    val payments: List<MercadoPagoOrderPayment>
)

data class MercadoPagoOrderPayment(
    val amount: String
)

data class MercadoPagoOrderItem(
    val title: String,
    @JsonProperty("unit_price") val unitPrice: String,
    @JsonProperty("unit_measure") val unitMeasure: String = "unit",
    val quantity: Int = 1
)

data class MercadoPagoOrderResponse(
    val id: String?,
    val status: String?,
    @JsonProperty("external_reference") val externalReference: String?,
    @JsonProperty("total_amount") val totalAmount: Double?,
    @JsonProperty("type_response") val typeResponse: MercadoPagoOrderConfigResponse? = null,
    val config: MercadoPagoOrderConfigResponse? = null
)

data class MercadoPagoOrderConfigResponse(
    @JsonProperty("qr_data") val qrData: String? = null,
)

data class MercadoPagoWebhookPayload(
    val action: String?,
    val type: String?,
    val data: MercadoPagoOrderData?
)

data class MercadoPagoOrderData(
    val id: String?,
    @JsonProperty("external_reference")
    val orderId: String?,
    val status: String?
)