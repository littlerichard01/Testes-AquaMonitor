package com.example.backend.dto

import com.example.backend.entity.StatusDispositivo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Resposta contendo os dados de um DispositivoIoT.")
data class DispositivoIoTResponse(
    @Schema(description = "Identificador unico do dispositivo", example = "1")
    val id: Long,

    @Schema(description = "Nome descritivo", example = "ESP32 Aquario 03")
    val nome: String,

    @Schema(description = "MAC address ou codigo fisico", example = "A4:CF:12:34:56:78")
    val identificacao: String,

    @Schema(description = "Topico MQTT de publicacao", example = "loja1/aquario3/temperatura")
    val mqttTopic: String,

    @Schema(description = "ONLINE / OFFLINE / INATIVO", example = "OFFLINE")
    val status: StatusDispositivo,

    @Schema(description = "Data/hora da ultima mensagem recebida. NULL se nunca comunicou", nullable = true)
    val ultimaComunicacao: LocalDateTime?,

    @Schema(description = "Resumo do Aquario associado. NULL se nao alocado", nullable = true)
    val aquario: AquarioResumo?,

    @Schema(description = "Data e hora do cadastro")
    val criadoEm: LocalDateTime,

    @Schema(description = "Data e hora da ultima atualizacao")
    val atualizadoEm: LocalDateTime
)
