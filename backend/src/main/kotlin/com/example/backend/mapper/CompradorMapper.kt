package com.example.backend.mapper

import com.example.backend.dto.CompradorRequest
import com.example.backend.dto.CompradorResponse
import com.example.backend.entity.Comprador
import com.example.backend.entity.Loja
import org.springframework.stereotype.Component

@Component
class CompradorMapper {

    fun paraEntidade(
        request: CompradorRequest,
        loja: Loja
    ): Comprador {
        val entidade = Comprador(
            nome = request.nome.trim(),
            cpf = request.cpf
                ?.trim()
                ?.replace("[^0-9]".toRegex(), "")
                ?.ifBlank { null },
            email = request.email
                ?.trim()
                ?.lowercase()
                ?.ifBlank { null },
            telefone = request.telefone
                ?.trim()
                ?.replace("[^0-9+]".toRegex(), "")
                ?.ifBlank { null },
            observacoes = request.observacoes
                ?.trim()
                ?.ifBlank { null }
        )

        entidade.lojas.add(loja)
        loja.compradores.add(entidade)

        return entidade
    }

    fun atualizarEntidade(
        entidade: Comprador,
        request: CompradorRequest
    ) {
        entidade.nome = request.nome.trim()
        entidade.cpf = request.cpf
            ?.trim()
            ?.replace("[^0-9]".toRegex(), "")
            ?.ifBlank { null }
        entidade.email = request.email
            ?.trim()
            ?.lowercase()
            ?.ifBlank { null }
        entidade.telefone = request.telefone
            ?.trim()
            ?.replace("[^0-9+]".toRegex(), "")
            ?.ifBlank { null }
        entidade.observacoes = request.observacoes
            ?.trim()
            ?.ifBlank { null }
    }

    fun paraResponse(entidade: Comprador): CompradorResponse {
        return CompradorResponse(
            id = entidade.id!!,
            nome = entidade.nome,
            cpf = entidade.cpf,
            email = entidade.email,
            telefone = entidade.telefone,
            observacoes = entidade.observacoes,
            criadoEm = entidade.criadoEm,
            atualizadoEm = entidade.atualizadoEm
        )
    }
}