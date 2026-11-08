package com.example.backend.service

import com.example.backend.dto.LeituraTemperaturaRequest
import com.example.backend.dto.LeituraTemperaturaResponse
import com.example.backend.entity.LeituraTemperatura
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.repository.DispositivoIoTRepository
import com.example.backend.mapper.LeituraTemperaturaMapper
import com.example.backend.repository.LeituraTemperaturaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class LeituraTemperaturaService(
    private val repository: LeituraTemperaturaRepository,
    private val aquarioService: AquarioService,
    private val dispositivoIoTRepository: DispositivoIoTRepository,
    private val dispositivoIoTService: DispositivoIoTService,
    private val lojaService: LojaService,
    private val mapper: LeituraTemperaturaMapper
) {

    companion object {
        private const val TAMANHO_PAGINA_PADRAO = 500
    }

    fun listarUltimasDaLoja(limite: Int = TAMANHO_PAGINA_PADRAO): List<LeituraTemperaturaResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        val tamanho = if (limite in 1..TAMANHO_PAGINA_PADRAO) limite else TAMANHO_PAGINA_PADRAO
        val pageable = PageRequest.of(0, tamanho, Sort.by(Sort.Direction.DESC, "dataHora"))
        return repository.findAllByLojaId(lojaAutenticada.id!!, pageable)
            .map(mapper::paraResponse)
    }

    fun listarUltimasPorAquario(
        aquarioId: Long,
        limite: Int = TAMANHO_PAGINA_PADRAO
    ): List<LeituraTemperaturaResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        if (!aquarioService.entidadePertenceALoja(aquarioId)) {
            throw RegraNaoEncontradoException("Aquario com id $aquarioId nao pertence a sua loja.")
        }
        val tamanho = if (limite in 1..TAMANHO_PAGINA_PADRAO) limite else TAMANHO_PAGINA_PADRAO
        val pageable = PageRequest.of(0, tamanho, Sort.by(Sort.Direction.DESC, "dataHora"))
        return repository.findAllByAquarioDaLoja(lojaAutenticada.id!!, aquarioId, pageable)
            .map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): LeituraTemperaturaResponse {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        val entidade = repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("LeituraTemperatura com id $id nao foi encontrada na sua loja.")
        return mapper.paraResponse(entidade)
    }

    @Transactional
    fun criar(request: LeituraTemperaturaRequest): LeituraTemperaturaResponse {
        val aquario = aquarioService.buscarEntidadeDaLojaOuFalhar(request.aquarioId)
        if (!dispositivoIoTService.entidadePertenceALoja(request.dispositivoIoTId)) {
            throw ErroValidacaoException(
                "DispositivoIoT com id ${request.dispositivoIoTId} nao esta alocado a um aquario da sua loja."
            )
        }
        val dispositivo = dispositivoIoTRepository.findById(request.dispositivoIoTId).orElseThrow {
            RegraNaoEncontradoException("DispositivoIoT com id ${request.dispositivoIoTId} nao existe.")
        }

        val dataHora: LocalDateTime = request.dataHora ?: LocalDateTime.now()
        if (dataHora.isAfter(LocalDateTime.now())) {
            throw ErroValidacaoException("Data/hora da leitura nao pode ser no futuro.")
        }

        val entidade = mapper.paraEntidade(request, aquario, dispositivo, dataHora)

        dispositivo.ultimaComunicacao = dataHora
        dispositivoIoTRepository.save(dispositivo)

        repository.save(entidade)
        return mapper.paraResponse(entidade)
    }

    @Transactional
    fun atualizar(id: Long, request: LeituraTemperaturaRequest): LeituraTemperaturaResponse {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()

        val entidade = repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("LeituraTemperatura com id $id nao foi encontrada na sua loja.")

        val aquario = aquarioService.buscarEntidadeDaLojaOuFalhar(request.aquarioId)

        if (!dispositivoIoTService.entidadePertenceALoja(request.dispositivoIoTId)) {
            throw ErroValidacaoException(
                "DispositivoIoT com id ${request.dispositivoIoTId} nao esta alocado a um aquario da sua loja."
            )
        }

        val dispositivo = dispositivoIoTRepository.findById(request.dispositivoIoTId).orElseThrow {
            RegraNaoEncontradoException("DispositivoIoT com id ${request.dispositivoIoTId} nao existe.")
        }

        val dataHora: LocalDateTime = request.dataHora ?: LocalDateTime.now()
        if (dataHora.isAfter(LocalDateTime.now())) {
            throw ErroValidacaoException("Data/hora da leitura nao pode ser no futuro.")
        }

        mapper.atualizarEntidade(entidade, request, aquario, dispositivo, dataHora)
        repository.save(entidade)
        return mapper.paraResponse(entidade)
    }

    @Transactional
    fun remover(id: Long) {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        val entidade = repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("LeituraTemperatura com id $id nao foi encontrada na sua loja.")

        repository.delete(entidade)
    }

}
