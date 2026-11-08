package com.example.backend.mapper

import com.example.backend.dto.AquarioRequest
import com.example.backend.dto.AquarioResponse
import com.example.backend.entity.Aquario
import com.example.backend.entity.Loja
import com.example.backend.entity.StatusAquario
import org.springframework.stereotype.Component

@Component
class AquarioMapper {

    fun paraEntidade(request: AquarioRequest, loja: Loja): Aquario {
        return Aquario(
            nome = request.nome.trim(),
            descricao = request.descricao?.trim()?.ifBlank { null },
            volumeLitros = request.volumeLitros,
            tipo = request.tipo,
            localizacao = request.localizacao?.trim()?.ifBlank { null },
            status = request.status ?: StatusAquario.ATIVO,
            preco = request.preco,
            loja = loja
        )
    }

    fun atualizarEntidade(entidade: Aquario, request: AquarioRequest) {
        entidade.nome = request.nome.trim()
        entidade.descricao = request.descricao?.trim()?.ifBlank { null }
        entidade.volumeLitros = request.volumeLitros
        entidade.tipo = request.tipo
        entidade.localizacao = request.localizacao?.trim()?.ifBlank { null }
        entidade.status = request.status ?: StatusAquario.ATIVO
        entidade.preco = request.preco
    }

    fun paraResponse(entidade: Aquario): AquarioResponse {
        return AquarioResponse(
            id = entidade.id!!,
            nome = entidade.nome,
            descricao = entidade.descricao,
            volumeLitros = entidade.volumeLitros,
            tipo = entidade.tipo,
            localizacao = entidade.localizacao,
            status = entidade.status,
            preco = entidade.preco,
            lojaId = entidade.loja.id!!,
            criadoEm = entidade.criadoEm,
            atualizadoEm = entidade.atualizadoEm
        )
    }
}