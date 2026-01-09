package br.com.fiap.techchallenge.paymentservice.repository

import br.com.fiap.techchallenge.paymentservice.domain.OrderPayment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderPaymentRepository : MongoRepository<OrderPayment, String> {

    fun findByMercadoPagoId(mercadoPagoId: String): OrderPayment?

}