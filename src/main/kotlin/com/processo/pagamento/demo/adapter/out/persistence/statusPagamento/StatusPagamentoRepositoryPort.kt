package com.processo.pagamento.demo.adapter.out.persistence.statusPagamento

import com.processo.pagamento.demo.adapter.out.persistence.pagamento.PagamentoEntity

interface StatusPagamentoRepositoryPort {

    fun adicionarStatusPagamento(pagamento: PagamentoEntity, status: String, mensagem: String = "")

}