package com.processo.pagamento.demo.domain.service

import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.port.`in`.CriarPagamentoUseCase
import com.processo.pagamento.demo.domain.port.out.PagamentoRepositoryPort
import org.springframework.stereotype.Service

@Service
class CriarPagamentoService(
    private val repository: PagamentoRepositoryPort
): CriarPagamentoUseCase {

    override fun criarPagamento(pagamento: Pagamento): Pagamento {
        val pagamentoSalvo = repository.salvarPagamento(pagamento)

        return pagamentoSalvo
    }
}