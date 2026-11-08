package com.example.backend.service

import com.example.backend.config.TokenUtil
import com.example.backend.dto.LojaAtualizacaoRequest
import com.example.backend.dto.LojaLoginRequest
import com.example.backend.dto.LojaRequest
import com.example.backend.dto.LojaResponse
import com.example.backend.dto.LoginResponse
import com.example.backend.entity.Loja
import com.example.backend.exception.CredenciaisIncorretasException
import com.example.backend.exception.EmailJaCadastradoException
import com.example.backend.exception.ErroValidacaoException
import com.example.backend.exception.RegraNaoEncontradoException
import com.example.backend.mapper.LojaMapper
import com.example.backend.repository.LojaRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LojaService(
    private val lojaRepository: LojaRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenUtil: TokenUtil,
    private val lojaMapper: LojaMapper
) {

    fun buscarPerfilAutenticado(): LojaResponse {
        return lojaMapper.paraResponse(buscarLojaAutenticada())
    }

    @Transactional
    fun cadastrar(request: LojaRequest): LojaResponse {
        val emailTratado = request.email.trim().lowercase()

        if (lojaRepository.existsByEmail(emailTratado)) {
            throw EmailJaCadastradoException("Este email ja esta cadastrado no sistema.")
        }

        val senhaLimpaNaoNula: String? = request.senha
        val senhaLimpa: String = requireNotNull(senhaLimpaNaoNula) { "Senha nao informada." }
        val senhaCriptografadaRaw: CharSequence? = passwordEncoder.encode(senhaLimpa)
        val senhaCriptografada: String = senhaCriptografadaRaw.toString()

        val novaLoja = lojaMapper.paraEntidade(request, senhaCriptografada)

        val lojaSalva = lojaRepository.save(novaLoja)
        return lojaMapper.paraResponse(lojaSalva)
    }

    fun login(request: LojaLoginRequest): LoginResponse {
        val emailTratado = request.email.trim().lowercase()

        val loja = lojaRepository.findByEmail(emailTratado)
            ?: throw CredenciaisIncorretasException("Email ou senha incorretos.")

        if (!loja.ativo) {
            throw CredenciaisIncorretasException("Conta inativa, contate o suporte.")
        }

        if (!passwordEncoder.matches(request.senha, loja.senha)) {
            throw CredenciaisIncorretasException("Email ou senha incorretos.")
        }

        val token = tokenUtil.gerarToken(loja.email)
        return LoginResponse(
            token = token,
            lojaId = loja.id!!,
            nomeLoja = loja.nome,
            emailLoja = loja.email
        )
    }

    @Transactional
    fun atualizar(id: Long, request: LojaAtualizacaoRequest): LojaResponse {
        val lojaAlvo = buscarEntidadeOuFalhar(id)
        val lojaAutenticada = buscarLojaAutenticada()

        requirePertenceALojaAutenticada(lojaAlvo, lojaAutenticada)

        val emailTratado = request.email.trim().lowercase()
        if (lojaRepository.existsByEmailAndIdNot(emailTratado, id)) {
            throw EmailJaCadastradoException("Este email ja esta cadastrado no sistema.")
        }

        lojaMapper.atualizarEntidade(lojaAlvo, request)

        val senhaInformada: String? = request.senha?.trim()?.ifBlank { null }
        if (senhaInformada != null) {
            if (senhaInformada.length < 6) {
                throw ErroValidacaoException("Senha deve ter pelo menos 6 caracteres.")
            }
            val senhaCodificadaRaw: CharSequence? = passwordEncoder.encode(senhaInformada)
            lojaAlvo.senha = senhaCodificadaRaw.toString()
        }

        val atualizada = lojaRepository.save(lojaAlvo)
        return lojaMapper.paraResponse(atualizada)
    }

    @Transactional
    fun excluir(id: Long) {
        val lojaAlvo = buscarEntidadeOuFalhar(id)
        val lojaAutenticada = buscarLojaAutenticada()

        requirePertenceALojaAutenticada(lojaAlvo, lojaAutenticada)

        lojaRepository.delete(lojaAlvo)
    }

    fun buscarLojaAutenticada(): Loja {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw CredenciaisIncorretasException("Loja nao autenticada.")

        val email = authentication.name
        return lojaRepository.findByEmail(email)
            ?: throw CredenciaisIncorretasException("Loja autenticada nao encontrada.")
    }

    fun buscarPorEmail(email: String): Loja? {
        val emailTratado = email.trim().lowercase()
        return lojaRepository.findByEmail(emailTratado)
    }

    private fun buscarEntidadeOuFalhar(id: Long): Loja {
        return lojaRepository.findById(id).orElseThrow {
            RegraNaoEncontradoException("Loja com id $id nao foi encontrada.")
        }
    }

    private fun requirePertenceALojaAutenticada(alvo: Loja, autenticada: Loja) {
        if (alvo.id != autenticada.id) {
            throw ErroValidacaoException("Voce nao tem permissao para modificar esta loja.")
        }
    }
}
