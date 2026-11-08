package com.example.backend.service

import com.example.backend.dto.AquarioRequest
import com.example.backend.dto.AquarioResponse
import com.example.backend.entity.Aquario
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.AquarioMapper
import com.example.backend.repository.AquarioRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AquarioService(
    private val repository: AquarioRepository,
    private val lojaService: LojaService,
    private val mapper: AquarioMapper
) {

    fun listarTodosDaLoja(): List<AquarioResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findAllByLojaId(lojaAutenticada.id!!).map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): AquarioResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    fun buscarEntidadeDaLojaOuFalhar(id: Long): Aquario {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("Aquario com id $id nao foi encontrado na sua loja.")
    }

    fun entidadePertenceALoja(id: Long): Boolean {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.existsByIdAndLojaId(id, lojaAutenticada.id!!)
    }

    @Transactional
    fun criar(request: AquarioRequest): AquarioResponse {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        validarRequest(request)

        val entidade = mapper.paraEntidade(request, lojaAutenticada)
        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun atualizar(id: Long, request: AquarioRequest): AquarioResponse {
        validarRequest(request)
        val entidade = buscarEntidadeDaLojaOuFalhar(id)

        mapper.atualizarEntidade(entidade, request)
        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun excluir(id: Long) {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        repository.delete(entidade)
    }

    private fun validarRequest(request: AquarioRequest) {
        val zero = BigDecimal.ZERO
        if (request.volumeLitros != null && request.volumeLitros.compareTo(zero) <= 0) {
            throw ErroValidacaoException("Volume em litros deve ser maior que zero.")
        }
        if (request.preco != null && request.preco.compareTo(zero) < 0) {
            throw ErroValidacaoException("Preco nao pode ser negativo.")
        }
    }
}