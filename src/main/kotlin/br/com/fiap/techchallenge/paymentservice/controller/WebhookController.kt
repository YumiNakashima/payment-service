package br.com.fiap.techchallenge.paymentservice.controller

import br.com.fiap.techchallenge.paymentservice.dto.MercadoPagoWebhookPayload
import br.com.fiap.techchallenge.paymentservice.service.PaymentService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/payments/webhook")
class WebhookController(
    private val paymentService: PaymentService
) {

    @PostMapping
    fun receiveNotification(
        @RequestBody payload: MercadoPagoWebhookPayload
    ): ResponseEntity<String> {

        val id = payload.data?.id
        val type = payload.type

        if (id == null || type == null) {
            logger.warn { "Invalid webhook payload for order" }
            return ResponseEntity.ok().build()
        }

        logger.info { "Notification about order for ID: $id, type: $type" }
        paymentService.processWebhook(payload)
        return ResponseEntity.ok().build()
    }

}