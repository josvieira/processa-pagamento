package com.processo.pagamento.demo.aplicacao_consumer.domain.service

import com.processo.pagamento.demo.aplicacao_consumer.config.NaoReprocessavelException
import com.processo.pagamento.demo.aplicacao_consumer.domain.port.out.PagamentoGatewayPort
import com.processo.pagamento.demo.domain.port.out.PagamentoRepositoryPort
import org.hibernate.internal.util.collections.CollectionHelper.setOf
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// Status finais já processados — usados para checagem de idempotência
private val STATUS_FINAIS = setOf("APROVADO", "RECUSADO")

@Service
class ProcessarPagamentoService(
    private val repositoryPort: PagamentoRepositoryPort,
    private val gatewayPort: PagamentoGatewayPort
){

    private val logger = LoggerFactory.getLogger(ProcessarPagamentoService::class.java)

    fun processar(evento: PagamentoCriadoEvent) {

        // ---------------------------------------------------------------
        // 1) Pagamento precisa existir no banco. Se não existir, é um dado
        //    inconsistente que NUNCA vai virar válido reprocessando a
        //    mesma mensagem -> não reprocessável.
        // ---------------------------------------------------------------
        val pagamento = repositoryPort.buscarComHistorico(evento.id)
            ?: throw NaoReprocessavelException(
                "Pagamento id=${evento.id} não encontrado no banco. Mensagem inconsistente."
            )

        // ---------------------------------------------------------------
        // 2) Idempotência: se esse pagamento já tem um status final
        //    (aprovado/recusado), significa que já foi processado antes
        //    — por exemplo, uma redelivery do SQS que não chegou a ser
        //    deletada a tempo. Não é erro, só não faz nada de novo.
        // ---------------------------------------------------------------
        val jaProcessado = pagamento.historicoStatus.any { it.status in STATUS_FINAIS }
        if (jaProcessado) {
            logger.info("Pagamento id={} já possui status final, ignorando reprocessamento.", evento.id)
            return
        }


        // ---------------------------------------------------------------
        // 3) Chamada ao gateway externo.
        //    Se o gateway estiver fora do ar / timeout / erro de rede,
        //    a exceção sobe SEM ser capturada aqui -> vira uma exceção
        //    "comum" lá no consumer, que mantém a mensagem na fila para
        //    nova tentativa (é um problema transitório, pode dar certo
        //    na próxima tentativa).
        // ---------------------------------------------------------------
        logger.info(
            "Chamando gateway de autorização. pagamentoId={}, metodoPagamento={}",
            evento.id, evento.metodoPagamento
        )

        val resultado = gatewayPort.autorizar(
            idCliente = evento.idUser.toString(),
            valor = evento.valor,
            metodoPagamento = evento.metodoPagamento
        )

        // ---------------------------------------------------------------
        // 4) Resultado de negócio do gateway (aprovado ou recusado) NÃO É
        //    uma exceção — é um desfecho válido do processamento. Ambos
        //    os casos salvam o status e terminam com sucesso (a mensagem
        //    é deletada da fila principal normalmente).
        // ---------------------------------------------------------------
        if (resultado.aprovado) {
            repositoryPort.adicionarStatus(evento.id, "APROVADO", resultado.motivo)
            logger.info("Pagamento id={} aprovado.", evento.id)
        } else {
            repositoryPort.adicionarStatus(evento.id, "RECUSADO", resultado.motivo)
            logger.info("Pagamento id={} recusado. motivo={}", evento.id, resultado.motivo)
        }
    }
}
