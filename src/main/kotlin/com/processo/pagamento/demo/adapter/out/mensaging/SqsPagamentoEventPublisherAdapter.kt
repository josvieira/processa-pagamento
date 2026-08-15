package com.processo.pagamento.demo.adapter.out.mensaging

import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.port.out.PagamentoEventPublisherPort
import org.apache.logging.log4j.util.Lazy.lazy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import tools.jackson.databind.ObjectMapper

@Component
class SqsPagamentoEventPublisherAdapter(
    private val sqsClient: SqsClient,
    private val objectMapper: ObjectMapper,
    @Value("\${aws.sqs.fila-processa-pagamento-url}") private val queueUrl: String
) : PagamentoEventPublisherPort {

    override fun publicarEvento(pagamento: Pagamento) {
        val evento = pagamento.toPedidoPagamentoEvento()

        val body = objectMapper.writeValueAsString(evento)

        val request = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(body)
            .messageGroupId(pagamento.idUser) // agrupa por cliente; ajuste conforme sua necessidade de ordenação
            .messageDeduplicationId(pagamento.idPagamento.toString()) // dedup pelo id (PK), não por conteúdo, evita evento duplicado
            .build()

        sqsClient.sendMessage(request)
    }
}