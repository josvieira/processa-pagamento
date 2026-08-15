package com.processo.pagamento.demo.adapter.out.persistence.pagamento

import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.port.out.PagamentoRepositoryPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

/* Caso mude a tecnologia do banco de dados basta resscrever esse adapter ou construir um novo que implemente a interface
* PagamentoRepositoryPort, sem precisar alterar a camada de domínio.
 */
@Component
class PagamentoRepositoryAdapter(
    private val repository: PagamentoRepository
): PagamentoRepositoryPort {


    @Transactional
    override fun salvarPagamento(pagamento: Pagamento): Pagamento {
        val entity = pagamento.toEntity()
        val savedEntity = repository.save(entity)
        //salvar primeiro status
        return savedEntity.toDomain()
    }
}