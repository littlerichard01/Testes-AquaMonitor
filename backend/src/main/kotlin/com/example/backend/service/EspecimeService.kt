package com.example.backend.service

import com.example.backend.dto.EspecimeRequest
import com.example.backend.dto.EspecimeResponse
import com.example.backend.entity.Especime
import com.example.backend.entity.StatusEspecime
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.EspecimeMapper
import com.example.backend.repository.EspecieRepository
import com.example.backend.repository.EspecimeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
class EspecimeService(
    private val repository: EspecimeRepository,
    private val especieRepository: EspecieRepository,
    private val aquarioService: AquarioService,
    private val lojaService: LojaService,
    private val mapper: EspecimeMapper
) {

    fun listarTodosDaLoja(): List<EspecimeResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findAllByLojaId(lojaAutenticada.id!!).map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): EspecimeResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    fun buscarEntidadeDaLojaOuFalhar(id: Long): Especime {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("Especime com id $id nao foi encontrado na sua loja.")
    }

    fun entidadePertenceALoja(id: Long): Boolean {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.existsByIdAndLojaId(id, lojaAutenticada.id!!)
    }

    @Transactional
    fun criar(request: EspecimeRequest): EspecimeResponse {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        validarRequest(
            identificacao = request.identificacao,
            preco = request.preco,
            especieId = request.especieId,
            aquarioId = request.aquarioId,
            dataEntrada = request.dataEntrada
        )

        if (repository.existsByIdentificacao(request.identificacao)) {
            throw ErroValidacaoException("Ja existe um especime com esta identificacao.")
        }

        val especie = especieRepository.findById(request.especieId).orElseThrow {
            RegraNaoEncontradoException("Especie com id ${request.especieId} nao existe.")
        }
        val aquario = request.aquarioId?.let(aquarioService::buscarEntidadeDaLojaOuFalhar)

        val entidade = mapper.paraEntidade(request, especie, aquario, lojaAutenticada)

        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun atualizar(id: Long, request: EspecimeRequest): EspecimeResponse {
        validarRequest(
            identificacao = request.identificacao,
            preco = request.preco,
            especieId = request.especieId,
            aquarioId = request.aquarioId,
            dataEntrada = request.dataEntrada
        )
        val entidade = buscarEntidadeDaLojaOuFalhar(id)

        // Removido o .trim()
        val identificacaoNova = request.identificacao
        if (repository.existsByIdentificacaoAndIdNot(identificacaoNova, id)) {
            throw ErroValidacaoException("Ja existe outro especime com esta identificacao.")
        }

        val especie = especieRepository.findById(request.especieId).orElseThrow {
            RegraNaoEncontradoException("Especie com id ${request.especieId} nao existe.")
        }
        val aquario = request.aquarioId?.let(aquarioService::buscarEntidadeDaLojaOuFalhar)

        mapper.atualizarEntidade(request, entidade, especie, aquario)

        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun excluir(id: Long) {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        repository.delete(entidade)
    }

    private fun validarRequest(
        identificacao: String,
        preco: BigDecimal,
        especieId: Long,
        aquarioId: Long?,
        dataEntrada: LocalDate?
    ) {
        if (identificacao.isBlank()) {
            throw ErroValidacaoException("Identificacao nao pode ser vazia.")
        }
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw ErroValidacaoException("Preco do especime deve ser maior que zero.")
        }
        if (aquarioId != null && !aquarioService.entidadePertenceALoja(aquarioId)) {
            throw ErroValidacaoException("Aquario com id $aquarioId nao pertence a sua loja.")
        }
        if (dataEntrada != null && dataEntrada.isAfter(LocalDate.now())) {
            throw ErroValidacaoException("Data de entrada nao pode ser no futuro.")
        }
    }
}