package com.processo.pagamento.demo.aplicacao_consumer.domain.port.out

import com.processo.pagamento.demo.aplicacao_consumer.domain.service.ResultadoAutorizacao
import com.processo.pagamento.demo.config.enum.MetodoPagamento

interface PagamentoGatewayPort {

    fun autorizar(idCliente: String, valor: Double, metodoPagamento: MetodoPagamento): ResultadoAutorizacao
}