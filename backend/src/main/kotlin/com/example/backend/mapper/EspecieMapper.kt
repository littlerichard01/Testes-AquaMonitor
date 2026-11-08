package com.example.backend.mapper

import com.example.backend.dto.EspecieRequest
import com.example.backend.dto.EspecieResponse
import com.example.backend.entity.Especie
import com.example.backend.entity.Loja
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class EspecieMapper {

    fun paraEntidade(request: EspecieRequest): Especie {
        return Especie(
            nomePopular = request.nomePopular.trim(),
            nomeCientifico = request.nomeCientifico?.trim()?.ifBlank { null },
            tipo = request.tipo,
            descricao = request.descricao?.trim()?.ifBlank { null },
            temperaturaMinima = request.temperaturaMinima,
            temperaturaMaxima = request.temperaturaMaxima
        )
    }

    fun atualizarEntidade(entidade: Especie, request: EspecieRequest): Especie {
        entidade.nomePopular = request.nomePopular.trim()
        entidade.nomeCientifico = request.nomeCientifico?.trim()?.ifBlank { null }
        entidade.tipo = request.tipo
        entidade.descricao = request.descricao?.trim()?.ifBlank { null }
        entidade.temperaturaMinima = request.temperaturaMinima
        entidade.temperaturaMaxima = request.temperaturaMaxima
        return entidade
    }

    fun paraResponse(entidade: Especie): EspecieResponse {
        return EspecieResponse(
            id = entidade.id!!,
            nomePopular = entidade.nomePopular,
            nomeCientifico = entidade.nomeCientifico,
            tipo = entidade.tipo,
            descricao = entidade.descricao,
            temperaturaMinima = entidade.temperaturaMinima,
            temperaturaMaxima = entidade.temperaturaMaxima,
            criadoEm = entidade.criadoEm,
            atualizadoEm = entidade.atualizadoEm
        )
    }
}
