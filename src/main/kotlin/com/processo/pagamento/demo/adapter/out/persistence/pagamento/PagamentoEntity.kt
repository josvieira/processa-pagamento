package com.processo.pagamento.demo.adapter.out.persistence.pagamento

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "pagamento")
class PagamentoEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "id_cliente")
    val idCliente: String,

    @Column(name = "valor")
    val valor: String,

    @Column(name = "moeda")
    val moeda: String,

    @Column(name = "metodo_pagamento")
    val metodoPagamento: String,

    @Column(name = "origem")
    val origem: String,

    @Column(name = "destino")
    val destino: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)