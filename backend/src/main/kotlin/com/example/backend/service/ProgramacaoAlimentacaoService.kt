package com.example.backend.service

import com.example.backend.dto.ProgramacaoAlimentacaoRequest
import com.example.backend.dto.ProgramacaoAlimentacaoResponse
import com.example.backend.entity.ProgramacaoAlimentacao
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.ProgramacaoAlimentacaoMapper
import com.example.backend.repository.ProgramacaoAlimentacaoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProgramacaoAlimentacaoService(
    private val repository: ProgramacaoAlimentacaoRepository,
    private val aquarioService: AquarioService,
    private val lojaService: LojaService,
    private val mapper: ProgramacaoAlimentacaoMapper
) {

    fun listarTodosDaLoja(): List<ProgramacaoAlimentacaoResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()

        return repository.findAllByLojaId(lojaAutenticada.id!!)
            .map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): ProgramacaoAlimentacaoResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    private fun buscarEntidadeDaLojaOuFalhar(
        id: Long
    ): ProgramacaoAlimentacao {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()

        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException(
                "ProgramacaoAlimentacao com id $id nao foi encontrada na sua loja."
            )
    }

    @Transactional
    fun criar(
        request: ProgramacaoAlimentacaoRequest
    ): ProgramacaoAlimentacaoResponse {
        val aquario = aquarioService.buscarEntidadeDaLojaOuFalhar(
            request.aquarioId
        )

        val entidade = mapper.paraEntidade(request, aquario)
        val salvo = repository.save(entidade)

        return mapper.paraResponse(salvo)
    }

    @Transactional
    fun atualizar(
        id: Long,
        request: ProgramacaoAlimentacaoRequest
    ): ProgramacaoAlimentacaoResponse {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        val aquario = aquarioService.buscarEntidadeDaLojaOuFalhar(
            request.aquarioId
        )

        mapper.atualizarEntidade(entidade, request, aquario)

        val salvo = repository.save(entidade)

        return mapper.paraResponse(salvo)
    }

    @Transactional
    fun excluir(id: Long) {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        repository.delete(entidade)
    }
}