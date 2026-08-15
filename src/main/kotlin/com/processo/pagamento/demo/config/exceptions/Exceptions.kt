package com.processo.pagamento.demo.config.exceptions

class IdempotencyKeyEmProcessamentoException(chave: String) :
    RuntimeException("Requisição com chave '$chave' já está sendo processada")

class IdempotencyKeyAusenteException :
    RuntimeException("Header Idempotency-Key é obrigatório")