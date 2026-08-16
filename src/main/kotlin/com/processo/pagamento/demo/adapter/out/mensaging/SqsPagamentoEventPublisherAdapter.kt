package com.processo.pagamento.demo.adapter.out.mensaging

import com.processo.pagamento.demo.aplicacao_consumer.domain.port.out.PagamentoDlqPublisherPort
import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.port.out.PagamentoEventPublisherPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import tools.jackson.databind.ObjectMapper

@Component
class SqsPagamentoEventPublisherAdapter(
    private val sqsClient: SqsClient,
    private val objectMapper: ObjectMapper,
    @Value("\${aws.sqs.fila-processa-pagamento-url}") private val queueUrl: String,
    @Value("\${aws.sqs.fila-processa-pagamento-dlq-url}") private val dlqUrl: String
) : PagamentoEventPublisherPort, PagamentoDlqPublisherPort {

    private val logger = LoggerFactory.getLogger(SqsPagamentoEventPublisherAdapter::class.java)

    override fun publicarEvento(pagamento: Pagamento) {
        val evento = pagamento.toPagamentoCriadoEvent()

        val body = objectMapper.writeValueAsString(evento)

        val request = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(body)
            .messageGroupId(pagamento.idUser) // agrupa por cliente; ajuste conforme sua necessidade de ordenação
            .messageDeduplicationId(pagamento.idPagamento.toString()) // dedup pelo id (PK), não por conteúdo, evita evento duplicado
            .build()

        val response = sqsClient.sendMessage(request)
        logger.info(
            "Evento de pagamento publicado na fila principal. idPagamento={}, sqsMessageId={}",
            pagamento.idPagamento, response.messageId()
        )
    }

    override fun enviarParaDlq(
        mensagemOriginal: String,
        messageGroupId: String,
        deduplicationId: String
    ) {
        val request = SendMessageRequest.builder()
            .queueUrl(dlqUrl)
            .messageBody(mensagemOriginal)
            .messageGroupId(messageGroupId)
            .messageDeduplicationId(deduplicationId)
            .build()

        val response = sqsClient.sendMessage(request)
        logger.info(
            "Mensagem publicada na DLQ. deduplicationId={}, sqsMessageId={}",
            deduplicationId, response.messageId()
        )
    }
}