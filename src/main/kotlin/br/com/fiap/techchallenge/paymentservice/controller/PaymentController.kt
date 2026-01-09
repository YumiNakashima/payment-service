package br.com.fiap.techchallenge.paymentservice.controller

import br.com.fiap.techchallenge.paymentservice.dto.OrderPaymentRequest
import br.com.fiap.techchallenge.paymentservice.dto.PaymentView
import br.com.fiap.techchallenge.paymentservice.service.PaymentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payments")
class PaymentController(private val service: PaymentService) {

    @PostMapping
    fun create(@RequestBody request: OrderPaymentRequest): ResponseEntity<PaymentView> {
        val payment = service.createPayment(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentView.fromDomain(payment))
    }
}