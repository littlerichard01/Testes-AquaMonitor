package com.example.backend.dto

import com.example.backend.entity.LeituraTemperatura
import com.example.backend.entity.StatusDispositivo
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "Resposta contendo uma LeituraTemperatura recebida do ESP32 (append-only).")
data class LeituraTemperaturaResponse(
    @Schema(description = "Identificador unico da leitura", example = "1")
    val id: Long,

    @Schema(description = "Temperatura medida em graus Celsius", example = "26.50")
    val temperatura: BigDecimal,

    @Schema(description = "Data/hora da medicao")
    val dataHora: LocalDateTime,

    @Schema(description = "Resumo do Aquario")
    val aquario: AquarioResumo,

    @Schema(description = "Resumo do DispositivoIoT medidor")
    val dispositivoIoT: DispositivoIoTResumo
)

@Schema(description = "Resumo simplificado de um DispositivoIoT (para embutir em responses).")
data class DispositivoIoTResumo(
    val id: Long,
    val nome: String,
    val identificacao: String,
    val status: StatusDispositivo
)