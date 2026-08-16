package com.processo.pagamento.demo.aplicacao_consumer.adapter.out

import com.processo.pagamento.demo.aplicacao_consumer.domain.port.out.PagamentoGatewayPort
import com.processo.pagamento.demo.aplicacao_consumer.domain.service.ResultadoAutorizacao
import com.processo.pagamento.demo.config.enum.MetodoPagamento
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ProcessaPagamentoAdapter(
    private val listaPagamentosGateway: List<PagamentoGateway>
): PagamentoGatewayPort{

    private val logger = LoggerFactory.getLogger(ProcessaPagamentoAdapter::class.java)

    override fun autorizar(
        idCliente: String,
        valor: Double,
        metodoPagamento: MetodoPagamento
    ): ResultadoAutorizacao {

        val resultado = when (metodoPagamento) {
            MetodoPagamento.CARTAO -> {
                listaPagamentosGateway.find { it is CartaoGatewayPagamento } // projeto de teste: resultado fixo
                ResultadoAutorizacao(aprovado = true, motivo = "Pagamento aprovado")
            }
            MetodoPagamento.PIX -> {
                listaPagamentosGateway.find { it is PixGatewayPagamento }
                ResultadoAutorizacao(aprovado = false, motivo = "PIX não foi feito")
            }
            MetodoPagamento.BOLETO -> {
                listaPagamentosGateway.find { it is BoletoGatewayPagamento }
                ResultadoAutorizacao(aprovado = true, motivo = "Gateway de boleto não encontrado")
            }
            else -> {
                logger.warn("Método de pagamento não suportado pelo gateway. idCliente={}, metodoPagamento={}", idCliente, metodoPagamento)
                ResultadoAutorizacao(aprovado = false, motivo = "Método de pagamento não suportado")
            }
        }

        logger.info(
            "Resultado da autorização no gateway. idCliente={}, metodoPagamento={}, aprovado={}, motivo={}",
            idCliente, metodoPagamento, resultado.aprovado, resultado.motivo
        )

        return resultado
    }
}