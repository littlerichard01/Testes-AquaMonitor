package com.example.backend.mapper

import com.example.backend.dto.AquarioResumo
import com.example.backend.dto.DispositivoIoTResumo
import com.example.backend.dto.LeituraTemperaturaRequest
import com.example.backend.dto.LeituraTemperaturaResponse
import com.example.backend.entity.Aquario
import com.example.backend.entity.DispositivoIoT
import com.example.backend.entity.LeituraTemperatura
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class LeituraTemperaturaMapper {

    fun paraEntidade(
        request: LeituraTemperaturaRequest,
        aquario: Aquario,
        dispositivoIoT: DispositivoIoT,
        dataHora: LocalDateTime
    ): LeituraTemperatura {
        return LeituraTemperatura(
            temperatura = request.temperatura,
            dataHora = dataHora,
            aquario = aquario,
            dispositivoIoT = dispositivoIoT
        )
    }

    fun atualizarEntidade(
        entidade: LeituraTemperatura,
        request: LeituraTemperaturaRequest,
        aquario: Aquario,
        dispositivoIoT: DispositivoIoT,
        dataHora: LocalDateTime
    ) {
        entidade.temperatura = request.temperatura
        entidade.dataHora = dataHora
        entidade.aquario = aquario
        entidade.dispositivoIoT = dispositivoIoT
    }

    fun paraResponse(entidade: LeituraTemperatura): LeituraTemperaturaResponse {
        return LeituraTemperaturaResponse(
            id = entidade.id!!,
            temperatura = entidade.temperatura,
            dataHora = entidade.dataHora,
            aquario = AquarioResumo(
                id = entidade.aquario.id!!,
                nome = entidade.aquario.nome,
                status = entidade.aquario.status
            ),
            dispositivoIoT = DispositivoIoTResumo(
                id = entidade.dispositivoIoT.id!!,
                nome = entidade.dispositivoIoT.nome,
                identificacao = entidade.dispositivoIoT.identificacao,
                status = entidade.dispositivoIoT.status
            )
        )
    }
}