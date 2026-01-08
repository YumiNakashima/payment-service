package br.com.fiap.techchallenge.paymentservice.repository

import br.com.fiap.techchallenge.paymentservice.domain.Payment
import org.springframework.data.mongodb.repository.MongoRepository

interface PaymentRepository : MongoRepository<Payment, String> {

    fun findByMercadoPagoId(mercadoPagoId: String): Payment?

}