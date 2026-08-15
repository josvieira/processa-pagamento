package com.processo.pagamento.demo.adapter.out.persistence.pagamento

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "status_pagamento")
class StatusPagamentoEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pagamento", nullable = false)
    val pagamento: PagamentoEntity,

    @Column(name = "status")
    val status: String,

    @Column(name = "mensagem")
    val mensagem: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)