package com.example.backend.service

import com.example.backend.dto.VendaRequest
import com.example.backend.dto.VendaResponse
import com.example.backend.entity.Aquario
import com.example.backend.entity.Especime
import com.example.backend.entity.StatusAquario
import com.example.backend.entity.StatusEspecime
import com.example.backend.entity.Venda
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.VendaMapper
import com.example.backend.repository.AquarioRepository
import com.example.backend.repository.EspecimeRepository
import com.example.backend.repository.VendaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class VendaService(
    private val repository: VendaRepository,
    private val aquarioRepository: AquarioRepository,
    private val especimeRepository: EspecimeRepository,
    private val compradorService: CompradorService,
    private val aquarioService: AquarioService,
    private val especimeService: EspecimeService,
    private val lojaService: LojaService,
    private val mapper: VendaMapper
) {

    fun listarTodosDaLoja(): List<VendaResponse> {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findAllByLojaId(lojaAutenticada.id!!).map(mapper::paraResponse)
    }

    fun buscarPorIdDaLoja(id: Long): VendaResponse {
        return mapper.paraResponse(buscarEntidadeDaLojaOuFalhar(id))
    }

    private fun buscarEntidadeDaLojaOuFalhar(id: Long): Venda {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        return repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("Venda com id $id nao foi encontrada na sua loja.")
    }

    @Transactional
    fun criar(request: VendaRequest): VendaResponse {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        validarRequest(request)

        val comprador = compradorService.buscarEntidadeDaLojaOuFalhar(request.compradorId)

        val especimeAlvo: Especime? = request.especimeId?.let { id ->
            val entidade = especimeService.buscarEntidadeDaLojaOuFalhar(id)
            if (entidade.status == StatusEspecime.VENDIDO) {
                throw ErroValidacaoException(
                    "Especime com id $id ja esta vendido (status VENDIDO) e nao pode ser vendido novamente."
                )
            }
            entidade
        }

        val aquarioAlvo: Aquario? = request.aquarioId?.let { id ->
            val entidade = aquarioService.buscarEntidadeDaLojaOuFalhar(id)
            if (entidade.status == StatusAquario.VENDIDO) {
                throw ErroValidacaoException(
                    "Aquario com id $id ja esta vendido (status VENDIDO) e nao pode ser vendido novamente."
                )
            }
            entidade
        }

        if (especimeAlvo != null) {
            especimeAlvo.status = StatusEspecime.VENDIDO
            especimeRepository.save(especimeAlvo)
        }
        if (aquarioAlvo != null) {
            aquarioAlvo.status = StatusAquario.VENDIDO
            aquarioRepository.save(aquarioAlvo)
        }


        val venda = mapper.paraEntidade(request, comprador, especimeAlvo, aquarioAlvo, lojaAutenticada)
        return mapper.paraResponse(repository.save(venda))
    }

    @Transactional
    fun atualizar(id: Long, request: VendaRequest): VendaResponse {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()

        val venda = repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("Venda com id $id nao foi encontrada na sua loja.")

        validarRequest(request)

        val comprador = compradorService.buscarEntidadeDaLojaOuFalhar(request.compradorId)

        venda.especime?.let {
            it.status = StatusEspecime.DISPONIVEL
            especimeRepository.save(it)
        }
        venda.aquario?.let {
            it.status = StatusAquario.ATIVO
            aquarioRepository.save(it)
        }

        val novoEspecime: Especime? = request.especimeId?.let { espId ->
            val entidade = especimeService.buscarEntidadeDaLojaOuFalhar(espId)
            if (entidade.status == StatusEspecime.VENDIDO) {
                throw ErroValidacaoException("Especime com id $espId ja esta vendido e nao pode ser vendido novamente.")
            }
            entidade
        }

        val novoAquario: Aquario? = request.aquarioId?.let { aqId ->
            val entidade = aquarioService.buscarEntidadeDaLojaOuFalhar(aqId)
            if (entidade.status == StatusAquario.VENDIDO) {
                throw ErroValidacaoException("Aquario com id $aqId ja esta vendido e nao pode ser vendido novamente.")
            }
            entidade
        }

        novoEspecime?.let {
            it.status = StatusEspecime.VENDIDO
            especimeRepository.save(it)
        }
        novoAquario?.let {
            it.status = StatusAquario.VENDIDO
            aquarioRepository.save(it)
        }

        venda.valor = request.valor
        venda.dataVenda = request.dataVenda ?: LocalDateTime.now()
        venda.observacoes = request.observacoes?.trim()?.ifBlank { null }
        venda.comprador = comprador
        venda.especime = novoEspecime
        venda.aquario = novoAquario

        val atualizada = repository.save(venda)
        return mapper.paraResponse(atualizada)
    }

    private fun validarRequest(request: VendaRequest) {
        if (request.valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw ErroValidacaoException("Valor da venda deve ser maior que zero.")
        }
        val informados = listOfNotNull(request.especimeId, request.aquarioId).size
        if (informados != 1) {
            throw ErroValidacaoException(
                "Regra XOR: informe EXATAMENTE UM entre especimeId e aquarioId (foram informados $informados)."
            )
        }
        if (request.dataVenda != null && request.dataVenda.isAfter(LocalDateTime.now())) {
            throw ErroValidacaoException("Data da venda nao pode ser no futuro.")
        }
    }

    @Transactional
    fun remover(id: Long) {
        val lojaAutenticada = lojaService.buscarLojaAutenticada()
        val venda = repository.findByIdAndLojaId(id, lojaAutenticada.id!!)
            ?: throw RegraNaoEncontradoException("Venda com id $id nao foi encontrada na sua loja.")

        venda.especime?.let { especime ->
            especime.status = StatusEspecime.DISPONIVEL
            especimeRepository.save(especime)
        }

        venda.aquario?.let { aquario ->
            aquario.status = StatusAquario.ATIVO
            aquarioRepository.save(aquario)
        }

        repository.delete(venda)
    }
}
