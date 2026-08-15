package com.processo.pagamento.demo.adapter.out.persistence.idempotency

import org.springframework.data.jpa.repository.JpaRepository

interface IdempotencyKeyJpaRepository : JpaRepository<IdempotencyKeyEntity, java.util.UUID> {
    fun findByChave(chave: String): IdempotencyKeyEntity?
}