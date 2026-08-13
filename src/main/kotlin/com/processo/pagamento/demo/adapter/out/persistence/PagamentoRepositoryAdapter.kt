package com.processo.pagamento.demo.adapter.out.persistence

import com.processo.pagamento.demo.domain.port.out.PagamentoRepositoryPort

/* Caso mude a tecnologia do banco de dados basta resscrever esse adapter ou construir um novo que implemente a interface
* PagamentoRepositoryPort, sem precisar alterar a camada de domínio.
 */
class PagamentoRepositoryAdapter: PagamentoRepositoryPort {
}