package com.processo.pagamento.demo.adapter.out.persistence.pagamento

import com.processo.pagamento.demo.adapter.out.persistence.statusPagamento.StatusPagamentoEntity
import com.processo.pagamento.demo.adapter.out.persistence.statusPagamento.StatusPagamentoRepository
import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.model.PagamentoStatus
import com.processo.pagamento.demo.domain.model.StatusPagamento
import com.processo.pagamento.demo.domain.port.out.PagamentoRepositoryPort
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.UUID

/* Caso mude a tecnologia do banco de dados basta resscrever esse adapter ou construir um novo que implemente a interface
* PagamentoRepositoryPort, sem precisar alterar a camada de domínio.
 */
@Component
class PagamentoRepositoryAdapter(
    private val repository: PagamentoRepository,
    private val statusRepository: StatusPagamentoRepository
): PagamentoRepositoryPort {

    private val logger = LoggerFactory.getLogger(PagamentoRepositoryAdapter::class.java)

    @Transactional
    override fun salvarPagamento(pagamento: Pagamento): Pagamento {
        val entity = pagamento.toEntity()
        val savedEntity = repository.save(entity)
        logger.info("Pagamento id={} persistido no banco.", savedEntity.id)

        adicionarStatus(savedEntity.id!!, "CREATED", "Pagamento criado com sucesso")

        return savedEntity.toDomain()
    }

    override fun adicionarStatus(pagamentoId: UUID, status: String, mensagem: String) {
        // getReferenceById NÃO faz SELECT no banco — cria um proxy só com o ID,
        // usado apenas para satisfazer a FK do relacionamento @ManyToOne
        val pagamentoRef = repository.getReferenceById(pagamentoId)

        val novoStatus = StatusPagamentoEntity(
            pagamento = pagamentoRef,
            status = status,
            mensagem = mensagem
        )

        statusRepository.save(novoStatus)
        logger.info("Status do pagamento id={} atualizado para {}. motivo={}", pagamentoId, status, mensagem)
    }

    override fun buscarComHistorico(id: UUID): PagamentoStatus? {
        val pagamentoEntity = repository.findById(id).orElse(null)
        if (pagamentoEntity == null) {
            logger.warn("Pagamento id={} não encontrado no banco.", id)
            return null
        }
        val historico = statusRepository.findByPagamento_IdOrderByCreatedAtAsc(id)

        return PagamentoStatus(
            idCliente = pagamentoEntity.idCliente,
            idPagamento = pagamentoEntity.id.toString(),
            valor = pagamentoEntity.valor,
            moeda = pagamentoEntity.moeda,
            metodoPagamento = pagamentoEntity.metodoPagamento,
            origem = pagamentoEntity.origem,
            destino = pagamentoEntity.destino,
            dataCriacao = pagamentoEntity.createdAt,
            historicoStatus = historico.map {
                StatusPagamento(
                    id = it.id.toString(),
                    status = it.status,
                    mensagem = it.mensagem,
                    dataCriacao = it.createdAt
                )
            }.toMutableList()
        )
    }
}