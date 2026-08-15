package com.processo.pagamento.demo.adapter.out.persistence.idempotency

import com.processo.pagamento.demo.adapter.`in`.web.PagamentoController
import com.processo.pagamento.demo.config.exceptions.IdempotencyKeyEmProcessamentoException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class IdempotencyService(
    private val repository: IdempotencyKeyJpaRepository
) {

    private val logger = LoggerFactory.getLogger(PagamentoController::class.java)

    // Resultado indica se já existe resposta pronta, ou se é a vez desta requisição processar
    sealed class Resultado {
        data class JaProcessado(val statusHttp: Int, val body: String) : Resultado()
        data object PodeProcessar : Resultado()
    }

    // Transação própria e curta: só garante o registro da chave antes de liberar o processamento
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun iniciarOuRecuperar(chave: String): Resultado {
        val existente = repository.findByChave(chave)

        if (existente != null) {
            return when (existente.status) {
                IdempotencyStatus.CONCLUIDO ->
                    Resultado.JaProcessado(
                        statusHttp = 200,
                        body =  "{}"
                    )
                IdempotencyStatus.PROCESSANDO ->
                    Resultado.JaProcessado(
                        statusHttp = 409,
                        body =  "{\"Warn\": \"Requisição com Idempotency-Key '$chave' já está em processamento.\"}"
                    )
            }
        }

        try {
            repository.save(
                IdempotencyKeyEntity(
                    chave = chave,
                    status = IdempotencyStatus.PROCESSANDO
                )
            )
        } catch (ex: DataIntegrityViolationException) {
            // Duas requisições concorrentes bateram no insert ao mesmo tempo;
            // o banco rejeitou a segunda por causa da constraint única — trata como "já em processamento"
            throw IdempotencyKeyEmProcessamentoException(chave)
        }

        return Resultado.PodeProcessar
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun concluir(chave: String) {
        val entity = repository.findByChave(chave) ?: return
        entity.status = IdempotencyStatus.CONCLUIDO
        entity.updatedAt = LocalDateTime.now()
        repository.save(entity)
    }
}