package com.processo.pagamento.demo.adapter.`in`.web

import com.processo.pagamento.demo.adapter.out.persistence.idempotency.IdempotencyService
import com.processo.pagamento.demo.domain.port.`in`.CriarPagamentoUseCase
import jakarta.websocket.server.PathParam
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tools.jackson.databind.ObjectMapper
import java.util.UUID

@RestController
@RequestMapping("/pagamentos")
class PagamentoController(
    private val pagamentoUseCase: CriarPagamentoUseCase,
    private val idempotencyService: IdempotencyService,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(PagamentoController::class.java)

    @PostMapping
    fun criarPagamento(
        @RequestHeader("Idempotency-Key") idempotencyKey: String?,
        @RequestBody requestBody: PedidoPagamentoRequestDto
    ): ResponseEntity<Any> {

        if (idempotencyKey == null) {
            return ResponseEntity.badRequest().body(
                PedidoPagamentoResponseDto(
                    idPagamento = "",
                    status = "ERRO",
                    mensagem = "Idempotency-Key é obrigatório"
                )
            )
        }

        val resultado = idempotencyService.iniciarOuRecuperar(idempotencyKey)

        when (resultado) {
            is IdempotencyService.Resultado.JaProcessado -> {
                logger.info("Requisição com Idempotency-Key '$idempotencyKey' já processada. Retornando resposta armazenada.")

                return ResponseEntity.status(resultado.statusHttp).body(resultado)
            }
            is IdempotencyService.Resultado.PodeProcessar -> {
                logger.info("Idempotency-Key '$idempotencyKey' aceita para processamento.")
            }
        }
        logger.info("Recebendo requisição para criar pagamento: $requestBody")

        val pagamento =
            pagamentoUseCase.criarPagamento(
                requestBody.toPagamento()
            )

        idempotencyService.concluir(idempotencyKey)
        logger.info("Pagamento id={} criado com sucesso para Idempotency-Key '{}'.", pagamento.idPagamento, idempotencyKey)

        return ResponseEntity.status(HttpStatus.CREATED).body(pagamento.toResponseDto())

    }

    @GetMapping("/{idPagamento}")
    fun buscarPagamento(
        @PathVariable idPagamento: UUID
    ): ResponseEntity<Any> {

        logger.info("Recebendo requisição para buscar pagamento: $idPagamento")

        val response = pagamentoUseCase.buscarPagamento(idPagamento)

        if (response == null) {
            logger.warn("Pagamento não encontrado para o id: $idPagamento")
        } else {
            logger.info("Pagamento encontrado para o id: $idPagamento")
        }

        return ResponseEntity.ok().body(response)
    }

}