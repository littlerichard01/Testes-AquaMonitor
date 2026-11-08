package com.example.backend.service

import com.example.backend.dto.ConfiguracaoTemperaturaRequest
import com.example.backend.dto.ConfiguracaoTemperaturaResponse
import com.example.backend.entity.ConfiguracaoTemperatura
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.ConfiguracaoTemperaturaMapper
import com.example.backend.repository.ConfiguracaoTemperaturaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class ConfiguracaoTemperaturaService(
    private val repository: ConfiguracaoTemperaturaRepository,
    private val aquarioService: AquarioService,
    private val lojaService: LojaService,
    private val mapper: ConfiguracaoTemperaturaMapper
) {

    fun listarTodosDaLoja(): List<ConfiguracaoTemperaturaResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findAllByLojaId(lojaAutenticada.id!!).map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): ConfiguracaoTemperaturaResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    private fun buscarEntidadeDaLojaOuFalhar(id: Long): ConfiguracaoTemperatura {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException(
                "ConfiguracaoTemperatura com id $id nao foi encontrada na sua loja."
            )
    }

    @Transactional
    fun criar(request: ConfiguracaoTemperaturaRequest): ConfiguracaoTemperaturaResponse {
        validarLimites(request.temperaturaMinima, request.temperaturaMaxima)
        val aquario = aquarioService.buscarEntidadeDaLojaOuFalhar(request.aquarioId)

        if (repository.existsByAquarioId(aquario.id!!)) {
            throw ErroValidacaoException(
                "Ja existe uma configuracao de temperatura para o aquario ${aquario.nome} (id ${aquario.id})."
            )
        }

        val entidade = mapper.paraEntidade(request, aquario)
        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun atualizar(id: Long, request: ConfiguracaoTemperaturaRequest): ConfiguracaoTemperaturaResponse {
        validarLimites(request.temperaturaMinima, request.temperaturaMaxima)
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        val novoAquario = aquarioService.buscarEntidadeDaLojaOuFalhar(request.aquarioId)

        if (repository.existsByAquarioIdAndIdNot(novoAquario.id!!, id)) {
            throw ErroValidacaoException(
                "Ja existe outra configuracao de temperatura associada a este aquario (id ${novoAquario.id})."
            )
        }

        mapper.atualizarEntidade(entidade, request, novoAquario)
        return mapper.paraResponse(repository.save(entidade))
    }

    @Transactional
    fun excluir(id: Long) {
        val entidade = buscarEntidadeDaLojaOuFalhar(id)
        repository.delete(entidade)
    }

    private fun validarLimites(minima: BigDecimal, maxima: BigDecimal) {
        if (minima.compareTo(maxima) > 0) {
            throw ErroValidacaoException(
                "Temperatura minima ($minima) nao pode ser maior que a maxima ($maxima)."
            )
        }
    }
}