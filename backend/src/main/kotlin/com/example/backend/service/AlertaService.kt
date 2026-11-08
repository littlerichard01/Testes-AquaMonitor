package com.example.backend.service

import com.example.backend.dto.AlertaRequest
import com.example.backend.dto.AlertaResponse
import com.example.backend.entity.Alerta
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.AlertaMapper
import com.example.backend.repository.AlertaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlertaService(
    private val repository: AlertaRepository,
    private val aquarioService: AquarioService,
    private val lojaService: LojaService,
    private val mapper: AlertaMapper
) {

    fun listarTodosDaLoja(): List<AlertaResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findAllByLojaId(lojaAutenticada.id!!).map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): AlertaResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    private fun buscarEntidadeDaLojaOuFalhar(id: Long): Alerta {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("Alerta com id $id nao foi encontrado na sua loja.")
    }

    @Transactional
    fun criar(request: AlertaRequest): AlertaResponse {
        val aquario = aquarioService.buscarEntidadeDaLojaOuFalhar(request.aquarioId)

        val entidade = mapper.paraEntidade(request, aquario)

        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun atualizar(id: Long, request: AlertaRequest): AlertaResponse {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        val aquario = aquarioService.buscarEntidadeDaLojaOuFalhar(request.aquarioId)

        mapper.atualizarEntidade(request, entidade, aquario)

        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun excluir(id: Long) {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        repository.delete(entidade)
    }
}