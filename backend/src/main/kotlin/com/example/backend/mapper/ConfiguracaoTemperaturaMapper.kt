package com.example.backend.mapper

import com.example.backend.dto.AquarioResumo
import com.example.backend.dto.ConfiguracaoTemperaturaRequest
import com.example.backend.dto.ConfiguracaoTemperaturaResponse
import com.example.backend.entity.Aquario
import com.example.backend.entity.ConfiguracaoTemperatura
import org.springframework.stereotype.Component

@Component
class ConfiguracaoTemperaturaMapper {

    fun paraEntidade(request: ConfiguracaoTemperaturaRequest, aquario: Aquario): ConfiguracaoTemperatura {
        return ConfiguracaoTemperatura(
            temperaturaMinima = request.temperaturaMinima,
            temperaturaMaxima = request.temperaturaMaxima,
            ativo = request.ativo ?: true,
            aquario = aquario
        )
    }

    fun atualizarEntidade(
        entidade: ConfiguracaoTemperatura,
        request: ConfiguracaoTemperaturaRequest,
        novoAquario: Aquario
    ) {
        entidade.temperaturaMinima = request.temperaturaMinima
        entidade.temperaturaMaxima = request.temperaturaMaxima
        entidade.ativo = request.ativo ?: true
        entidade.aquario = novoAquario
    }

    fun paraResponse(entidade: ConfiguracaoTemperatura): ConfiguracaoTemperaturaResponse {
        return ConfiguracaoTemperaturaResponse(
            id = entidade.id!!,
            temperaturaMinima = entidade.temperaturaMinima,
            temperaturaMaxima = entidade.temperaturaMaxima,
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