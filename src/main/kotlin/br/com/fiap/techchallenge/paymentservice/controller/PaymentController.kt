package br.com.fiap.techchallenge.paymentservice.controller

import br.com.fiap.techchallenge.paymentservice.dto.OrderPaymentDto
import br.com.fiap.techchallenge.paymentservice.dto.PaymentView
import br.com.fiap.techchallenge.paymentservice.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payments")
class PagamentoController(private val service: PaymentService) {

    @PostMapping
    fun create(@RequestBody dto: OrderPaymentDto): ResponseEntity<PaymentView> {
        val payment = service.initiatePaymentForOrder(dto.orderId, dto.value)
        return ResponseEntity.ok(payment)
    }
}