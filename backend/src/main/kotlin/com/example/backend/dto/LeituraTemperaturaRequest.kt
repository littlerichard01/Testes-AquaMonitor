package com.example.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(
    description = "Requisicao para registrar uma LeituraTemperatura. Normalmente esta requisicao e feita automaticamente pelo backend apos receber uma mensagem MQTT, mas pode ser usada via API para testes."
)
data class LeituraTemperaturaRequest(
    @field:NotNull(message = "Temperatura medida e obrigatoria")
    @field:PositiveOrZero(message = "Temperatura invalida")
    @Schema(description = "Valor medido pelo sensor em graus Celsius", example = "26.50")
    val temperatura: BigDecimal,

    @Schema(description = "Data/hora da leitura. Default = agora", nullable = true)
    val dataHora: LocalDateTime? = LocalDateTime.now(),

    @field:NotNull(message = "ID do aquario e obrigatorio")
    @Schema(description = "ID do Aquario de onde veio a leitura", example = "3")
    val aquarioId: Long,

    @field:NotNull(message = "ID do dispositivo IoT e obrigatorio")
    @Schema(description = "ID do DispositivoIoT que mediu a temperatura", example = "2")
    val dispositivoIoTId: Long
)
