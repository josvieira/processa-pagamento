package com.processo.pagamento.demo.aplicacao_consumer.config

class NaoReprocessavelException (
    mensagem: String,
    causa: Throwable? = null
    ) : RuntimeException(mensagem, causa)