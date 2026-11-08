package com.example.backend.service

import com.example.backend.dto.EspecieRequest
import com.example.backend.dto.EspecieResponse
import com.example.backend.entity.Especie
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.EspecieMapper
import com.example.backend.repository.EspecieRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class EspecieService(
    private val repository: EspecieRepository,
    private val mapper: EspecieMapper
) {

    fun listarTodos(): List<EspecieResponse> {
        return repository.findAll().map(mapper::paraResponse)
    }

    fun buscarPorId(id: Long): EspecieResponse {
        val entidade = buscarEntidadeOuFalhar(id)
        return mapper.paraResponse(entidade)
    }

    @Transactional
    fun criar(request: EspecieRequest): EspecieResponse {
        validarRequest(request)
        val entidade = mapper.paraEntidade(request)
        val salva = repository.save(entidade)
        return mapper.paraResponse(salva)
    }

    @Transactional
    fun atualizar(id: Long, request: EspecieRequest): EspecieResponse {
        validarRequest(request)
        val entidade = buscarEntidadeOuFalhar(id)
        val atualizada = mapper.atualizarEntidade(entidade, request)
        val salva = repository.save(atualizada)
        return mapper.paraResponse(atualizada)
    }

    @Transactional
    fun excluir(id: Long) {
        val entidade = buscarEntidadeOuFalhar(id)
        repository.delete(entidade)
    }

    private fun buscarEntidadeOuFalhar(id: Long): Especie {
        return repository.findById(id).orElseThrow {
            RegraNaoEncontradoException("Especie com id $id nao foi encontrada")
        }
    }

    private fun validarRequest(request: EspecieRequest) {
        if (request.temperaturaMinima != null && request.temperaturaMaxima != null) {
            if (request.temperaturaMinima.compareTo(request.temperaturaMaxima) > 0) {
                throw ErroValidacaoException(
                    "A temperatura minima (${request.temperaturaMinima}) " +
                        "nao pode ser maior que a temperatura maxima (${request.temperaturaMaxima})."
                )
            }
        }
    }
}
