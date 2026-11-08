package com.example.backend.dto

import com.example.backend.entity.StatusDispositivo
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Requisicao para criar ou atualizar um DispositivoIoT (ESP32).")
data class DispositivoIoTRequest(
    @field:NotBlank(message = "Nome do dispositivo e obrigatorio")
    @Schema(description = "Nome descritivo", example = "ESP32 Aquario 03")
    @Size(max = 100, message = "O nome do dispositivo deve conter no maximo 100 caractares")
    val nome: String,

    @field:NotBlank(message = "Identificacao (MAC) e obrigatoria")
    @Schema(description = "MAC address ou codigo fisico unico", example = "A4:CF:12:34:56:78")
    val identificacao: String,

    @field:NotBlank(message = "Topico MQTT e obrigatorio")
    @Schema(description = "Topico MQTT onde o dispositivo publica leituras", example = "loja1/aquario3/temperatura")
    val mqttTopic: String,

    @Schema(description = "Status: ONLINE / OFFLINE / INATIVO. Default = OFFLINE", example = "OFFLINE", nullable = true)
    val status: StatusDispositivo? = StatusDispositivo.OFFLINE,

    @Schema(description = "ID do Aquario monitorado. NULL se dispositivo nao associado", nullable = true, example = "3")
    val aquarioId: Long? = null
)
