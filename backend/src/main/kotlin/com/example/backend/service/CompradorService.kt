package com.example.backend.service

import com.example.backend.dto.CompradorRequest
import com.example.backend.dto.CompradorResponse
import com.example.backend.entity.Comprador
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.repository.CompradorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.example.backend.mapper.CompradorMapper

@Service
class CompradorService(
    private val repository: CompradorRepository,
    private val lojaService: LojaService,
    private val mapper: CompradorMapper
) {

    fun listarTodosDaLoja(): List<CompradorResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()

        return repository.findAllByLojaId(lojaAutenticada.id!!)
            .map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): CompradorResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    fun buscarEntidadeDaLojaOuFalhar(id: Long): Comprador {

        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("Comprador com id $id nao foi encontrado na sua loja.")
    }

    fun entidadePertenceALoja(id: Long): Boolean {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.existsByIdAndLojaId(id, lojaAutenticada.id!!)
    }

    @Transactional
    fun criar(request: CompradorRequest): CompradorResponse {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()

        val entidade = mapper.paraEntidade(request, lojaAutenticada)
        val salvo = repository.save(entidade)

        return mapper.paraResponse(salvo)
    }

    @Transactional
    fun atualizar(
        id: Long,
        request: CompradorRequest
    ): CompradorResponse {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)

        mapper.atualizarEntidade(entidade, request)

        val salvo = repository.save(entidade)

        return mapper.paraResponse(salvo)
    }

    @Transactional
    fun excluir(id: Long) {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        val entidade = buscarEntidadeDaLojaOuFalhar(id)

        lojaAutenticada.compradores.remove(entidade)
        entidade.lojas.remove(lojaAutenticada)

        if (entidade.lojas.isEmpty()) {
            repository.delete(entidade)
        }
    }
}
