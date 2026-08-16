package com.processo.pagamento.demo.aplicacao_consumer.domain.service

import com.processo.pagamento.demo.aplicacao_consumer.config.NaoReprocessavelException
import com.processo.pagamento.demo.aplicacao_consumer.domain.port.out.PagamentoDlqPublisherPort
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName
import tools.jackson.databind.ObjectMapper

@Component
class PagamentoConsumer(
    private val objectMapper: ObjectMapper,
    private val processarPagamentoUseCase: ProcessarPagamentoService,
    private val dlqPublisherPort: PagamentoDlqPublisherPort
) {

    private val logger = LoggerFactory.getLogger(PagamentoConsumer::class.java)

    // Deletion policy: ON_SUCCESS -> o container só apaga a mensagem da fila principal
    // quando o método retorna sem lançar exceção. Se lançar, a mensagem permanece na
    // fila e volta a ficar visível após o visibility timeout (30s, configurado na fila)
    // para uma nova tentativa. Depois de 3 tentativas sem sucesso (maxReceiveCount=3 na
    // redrive policy da fila), o próprio SQS move a mensagem para a DLQ automaticamente.
    @SqsListener(
        value = ["\${aws.sqs.fila-processa-pagamento-url}"],
        acknowledgementMode = SqsListenerAcknowledgementMode.ON_SUCCESS
    )
    fun consumir(mensagem: Message) {
        val messageGroupId = mensagem.attributes()[MessageSystemAttributeName.MESSAGE_GROUP_ID]
            ?: "pagamento"
        val deduplicationId = mensagem.attributes()[MessageSystemAttributeName.MESSAGE_DEDUPLICATION_ID]
            ?: mensagem.messageId()
        val receiveCount = mensagem.attributes()[MessageSystemAttributeName.APPROXIMATE_RECEIVE_COUNT]

        logger.info(
            "Mensagem recebida da fila. id={}, messageGroupId={}, tentativa={}",
            mensagem.messageId(), messageGroupId, receiveCount
        )

        try {
            val evento = objectMapper.readValue(mensagem.body(), PagamentoCriadoEvent::class.java)
            processarPagamentoUseCase.processar(evento)
            logger.info("Mensagem processada com sucesso e removida da fila. id={}", mensagem.messageId())

        } catch (ex: NaoReprocessavelException) {
            // ===== Exceção não-reprocessável =====
            // Tratada como "sucesso" do ponto de vista da fila principal: publica direto
            // na DLQ e retorna sem lançar, para que o container apague a mensagem da fila
            // principal (ON_SUCCESS), sem esperar o redrive automático.
            logger.warn(
                "Mensagem não reprocessável, enviando direto para a DLQ. id={}, motivo={}",
                mensagem.messageId(), ex.message
            )
            dlqPublisherPort.enviarParaDlq(
                mensagemOriginal = mensagem.body(),
                messageGroupId = messageGroupId,
                deduplicationId = deduplicationId
            )
        } catch (ex: Exception) {
            // Demais exceções sobem depois de logadas: com acknowledgementMode = ON_SUCCESS,
            // o container não apaga a mensagem, que permanece na fila para nova tentativa.
            logger.error(
                "Falha ao processar mensagem, permanecerá na fila para nova tentativa. id={}, tentativa={}",
                mensagem.messageId(), receiveCount, ex
            )
            throw ex
        }
    }
}
