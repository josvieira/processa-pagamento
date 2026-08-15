package com.processo.pagamento.demo.adapter.out.persistence.idempotency

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "idempotency_key",
    uniqueConstraints = [UniqueConstraint(columnNames = ["chave"])]
)
class IdempotencyKeyEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "chave", nullable = false, unique = true)
    val chave: String,

    /*Seria possivel criar uma funcionalidade para descartar as chaves que estão em processamento depois de x tempo, para nào impedir uma solicitação de reprocessamento real do pagamento, ele pode nào ter sido enviado para a fila por erro */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: IdempotencyStatus,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    /*Os dois campos abaixo poderiam ser utilizados para tratar os pagamentos abandonados, a coluna idPedidoPagamento poderia ser uma foremKey, melhorias futuras*/
    @Column(name = "id_cliente")
    val idCliente: String? = null,

    @Column(name = "id_pedido_pagamento")
    val idPedidoPagamento: String? = null
)
