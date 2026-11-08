package com.example.backend.mapper

import com.example.backend.dto.DispositivoIoTRequest
import com.example.backend.dto.DispositivoIoTResponse
import com.example.backend.dto.AquarioResumo
import com.example.backend.entity.Aquario
import com.example.backend.entity.DispositivoIoT
import com.example.backend.entity.StatusDispositivo
import org.springframework.stereotype.Component

@Component
class DispositivoIoTMapper {

    fun paraEntidade(request: DispositivoIoTRequest, aquario: Aquario?): DispositivoIoT {
        return DispositivoIoT(
            nome = request.nome.trim(),
            identificacao = request.identificacao.trim(),
            mqttTopic = request.mqttTopic.trim(),
            status = request.status ?: StatusDispositivo.OFFLINE,
            ultimaComunicacao = null,
            aquario = aquario
        )
    }

    fun atualizarEntidade(entidade: DispositivoIoT, request: DispositivoIoTRequest, aquario: Aquario?) {
        entidade.nome = request.nome.trim()
        entidade.identificacao = request.identificacao.trim()
        entidade.mqttTopic = request.mqttTopic.trim()
        entidade.status = request.status ?: StatusDispositivo.OFFLINE
        entidade.aquario = aquario
    }

    fun paraResponse(entidade: DispositivoIoT): DispositivoIoTResponse {
        return DispositivoIoTResponse(
            id = entidade.id!!,
            nome = entidade.nome,
            identificacao = entidade.identificacao,
            mqttTopic = entidade.mqttTopic,
            status = entidade.status,
            ultimaComunicacao = entidade.ultimaComunicacao,
            aquario = entidade.aquario?.let {
                AquarioResumo(
                    id = it.id!!,
                    nome = it.nome,
                    status = it.status
                )
            },
            criadoEm = entidade.criadoEm,
            atualizadoEm = entidade.atualizadoEm
        )
    }
}
