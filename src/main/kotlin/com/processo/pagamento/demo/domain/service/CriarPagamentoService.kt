package com.processo.pagamento.demo.domain.service

import com.processo.pagamento.demo.adapter.`in`.web.PedidoPagamentoRequestDto
import com.processo.pagamento.demo.adapter.`in`.web.PedidoPagamentoResponseDto
import com.processo.pagamento.demo.domain.port.`in`.CriarPagamentoUseCase
import org.springframework.stereotype.Service

@Service
class CriarPagamentoService: CriarPagamentoUseCase {
    override fun criarPagamento(request: PedidoPagamentoRequestDto): PedidoPagamentoResponseDto {
        return PedidoPagamentoResponseDto()
    }
}