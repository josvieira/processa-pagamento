package com.processo.pagamento.demo.domain.service

import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.port.`in`.CriarPagamentoUseCase
import com.processo.pagamento.demo.domain.port.out.PagamentoRepositoryPort
import com.processo.pagamento.demo.domain.model.PagamentoStatus
import com.processo.pagamento.demo.domain.port.out.PagamentoEventPublisherPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CriarPagamentoService(
    private val repository: PagamentoRepositoryPort,
    private val messaging: PagamentoEventPublisherPort
): CriarPagamentoUseCase {

    private val logger = LoggerFactory.getLogger(CriarPagamentoService::class.java)

    override fun criarPagamento(pagamento: Pagamento): Pagamento {
        val pagamentoSalvo = repository.salvarPagamento(pagamento)
        logger.info(
            "Pagamento id={} salvo com sucesso. idUser={}, metodoPagamento={}",
            pagamentoSalvo.idPagamento, pagamentoSalvo.idUser, pagamentoSalvo.metodoPagamento
        )

        messaging.publicarEvento(pagamentoSalvo)
        logger.info("Evento de pagamento criado publicado. id={}", pagamentoSalvo.idPagamento)

        return pagamentoSalvo
    }

    override fun buscarPagamento(idPagamento: UUID): PagamentoStatus? {
        return repository.buscarComHistorico(idPagamento
        )
    }
}