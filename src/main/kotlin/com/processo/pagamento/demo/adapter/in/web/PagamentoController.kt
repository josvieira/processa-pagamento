package com.processo.pagamento.demo.adapter.`in`.web

import com.processo.pagamento.demo.domain.port.`in`.CriarPagamentoUseCase
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pagamentos")
class PagamentoController(
    private val pagamentoUseCase: CriarPagamentoUseCase
) {

    private val logger = LoggerFactory.getLogger(PagamentoController::class.java)

    @PostMapping
    fun criarPagamento(requestBody: PedidoPagamentoRequestDto): ResponseEntity<PedidoPagamentoResponseDto> {
        return ResponseEntity.ok(pagamentoUseCase.criarPagamento(requestBody))

    }
}