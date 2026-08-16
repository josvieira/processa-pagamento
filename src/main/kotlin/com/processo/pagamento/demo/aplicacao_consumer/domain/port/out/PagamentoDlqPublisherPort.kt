package com.processo.pagamento.demo.aplicacao_consumer.domain.port.out

interface PagamentoDlqPublisherPort {

    fun enviarParaDlq(mensagemOriginal: String, messageGroupId: String, deduplicationId: String)
}