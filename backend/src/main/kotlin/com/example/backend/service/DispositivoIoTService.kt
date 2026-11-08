package com.example.backend.service

import com.example.backend.dto.DispositivoIoTRequest
import com.example.backend.dto.DispositivoIoTResponse
import com.example.backend.entity.DispositivoIoT
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.DispositivoIoTMapper
import com.example.backend.repository.DispositivoIoTRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DispositivoIoTService(
    private val repository: DispositivoIoTRepository,
    private val aquarioService: AquarioService,
    private val lojaService: LojaService,
    private val mapper: DispositivoIoTMapper
) {

    fun listarTodosDaLoja(): List<DispositivoIoTResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findAllByLojaId(lojaAutenticada.id!!).map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): DispositivoIoTResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    private fun buscarEntidadeDaLojaOuFalhar(id: Long): DispositivoIoT {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException(
                "DispositivoIoT com id $id nao foi encontrado (ou nao esta alocado a nenhum aquario da sua loja)."
            )
    }

    fun entidadePertenceALoja(id: Long): Boolean {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.existsByIdAndLojaId(id, lojaAutenticada.id!!)
    }

    @Transactional
    fun criar(request: DispositivoIoTRequest): DispositivoIoTResponse {
        val identificacaoNova = request.identificacao.trim()
        if (repository.existsByIdentificacao(identificacaoNova)) {
            throw ErroValidacaoException("Ja existe um dispositivo com esta identificacao (MAC).")
        }

        val aquario = request.aquarioId?.let(aquarioService::buscarEntidadeDaLojaOuFalhar)
        if (request.aquarioId != null && aquario == null) {
            throw ErroValidacaoException("Aquario com id ${request.aquarioId} nao pertence a sua loja.")
        }

        val entidade = mapper.paraEntidade(request, aquario)
        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun atualizar(id: Long, request: DispositivoIoTRequest): DispositivoIoTResponse {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)

        val identificacaoNova = request.identificacao.trim()
        if (repository.existsByIdentificacaoAndIdNot(identificacaoNova, id)) {
            throw ErroValidacaoException("Ja existe outro dispositivo com esta identificacao (MAC).")
        }

        val aquario = request.aquarioId?.let(aquarioService::buscarEntidadeDaLojaOuFalhar)
        if (request.aquarioId != null && aquario == null) {
            throw ErroValidacaoException("Aquario com id ${request.aquarioId} nao pertence a sua loja.")
        }

        mapper.atualizarEntidade(entidade, request, aquario)

        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun excluir(id: Long) {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        repository.delete(entidade)
    }
}
