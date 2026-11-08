package com.example.backend.mapper

import com.example.backend.dto.AlertaRequest
import com.example.backend.dto.AlertaResponse
import com.example.backend.dto.AquarioResumo
import com.example.backend.entity.Alerta
import com.example.backend.entity.Aquario
import org.springframework.stereotype.Component

@Component
class AlertaMapper {

    fun paraEntidade(request: AlertaRequest, aquario: Aquario): Alerta {
        return Alerta(
            tipo = request.tipo,
            severidade = request.severidade,
            ativo = request.ativo ?: true,
            aquario = aquario
        )
    }

    fun atualizarEntidade(request: AlertaRequest, entidade: Alerta, aquario: Aquario) {
        entidade.tipo = request.tipo
        entidade.severidade = request.severidade
        entidade.ativo = request.ativo ?: true
        entidade.aquario = aquario
    }

    fun paraResponse(entidade: Alerta): AlertaResponse {
        return AlertaResponse(
            id = entidade.id!!,
            tipo = entidade.tipo,
            severidade = entidade.severidade,
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