package com.example.backend.mapper

import com.example.backend.dto.LojaAtualizacaoRequest
import com.example.backend.dto.LojaRequest
import com.example.backend.dto.LojaResponse
import com.example.backend.entity.Loja
import org.springframework.stereotype.Component

@Component
class LojaMapper {

    fun paraEntidade(request: LojaRequest, senhaCriptografada: String): Loja {
        return Loja(
            nome = request.nome.trim(),
            email = request.email.trim().lowercase(),
            senha = senhaCriptografada,
            ativo = request.ativo ?: true
        )
    }

    fun atualizarEntidade(entidade: Loja, request: LojaAtualizacaoRequest) {
        entidade.nome = request.nome.trim()
        entidade.email = request.email.trim().lowercase()
        entidade.ativo = request.ativo ?: true
    }

    fun paraResponse(entidade: Loja): LojaResponse {
        return LojaResponse(
            id = entidade.id!!,
            nome = entidade.nome,
            email = entidade.email,
            ativo = entidade.ativo,
            criadoEm = entidade.criadoEm,
            atualizadoEm = entidade.atualizadoEm
        )
    }
}
