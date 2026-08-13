package com.processo.pagamento.demo.domain.port.`in`

import com.processo.pagamento.demo.adapter.`in`.web.PedidoPagamentoRequestDto
import com.processo.pagamento.demo.adapter.`in`.web.PedidoPagamentoResponseDto

interface CriarPagamentoUseCase {

    fun criarPagamento(request: PedidoPagamentoRequestDto): PedidoPagamentoResponseDto
}