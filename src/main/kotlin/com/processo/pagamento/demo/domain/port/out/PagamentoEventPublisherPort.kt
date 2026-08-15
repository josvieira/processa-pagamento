package com.processo.pagamento.demo.domain.port.out

import com.processo.pagamento.demo.domain.model.Pagamento

interface PagamentoEventPublisherPort {

    fun publicarEvento(pagamento: Pagamento)
}