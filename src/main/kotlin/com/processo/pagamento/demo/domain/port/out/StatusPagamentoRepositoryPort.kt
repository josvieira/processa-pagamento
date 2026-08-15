package com.processo.pagamento.demo.domain.port.out

interface StatusPagamentoRepositoryPort {

    fun adicionarStatusPagamento(idPagamento: String, status: String)
}