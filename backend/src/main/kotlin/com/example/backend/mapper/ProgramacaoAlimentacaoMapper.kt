package com.example.backend.mapper

import com.example.backend.dto.AquarioResumo
import com.example.backend.dto.ProgramacaoAlimentacaoRequest
import com.example.backend.dto.ProgramacaoAlimentacaoResponse
import com.example.backend.entity.Aquario
import com.example.backend.entity.ProgramacaoAlimentacao
import org.springframework.stereotype.Component

@Component
class ProgramacaoAlimentacaoMapper {

    fun paraEntidade(
        request: ProgramacaoAlimentacaoRequest,
        aquario: Aquario
    ): ProgramacaoAlimentacao {
        return ProgramacaoAlimentacao(
            horario = request.horario.trim(),
            descricao = request.descricao?.trim()?.ifBlank { null },
            ativo = request.ativo ?: true,
            aquario = aquario
        )
    }

    fun atualizarEntidade(
        entidade: ProgramacaoAlimentacao,
        request: ProgramacaoAlimentacaoRequest,
        aquario: Aquario
    ) {
        entidade.horario = request.horario.trim()
        entidade.descricao = request.descricao?.trim()?.ifBlank { null }
        entidade.ativo = request.ativo ?: true
        entidade.aquario = aquario
    }

    fun paraResponse(
        entidade: ProgramacaoAlimentacao
    ): ProgramacaoAlimentacaoResponse {
        return ProgramacaoAlimentacaoResponse(
            id = entidade.id!!,
            horario = entidade.horario,
            descricao = entidade.descricao,
            ativo = entidade.ativo,
            aquario = AquarioResumo(
                id = entidade.aquario.id!!,
                nome = entidade.aquario.nome,
                status = entidade.aquario.status
            ),
            criadoEm = entidade.criadoEm,
            atualizadoEm = entidade.atualizadoEm
        )
    }
}