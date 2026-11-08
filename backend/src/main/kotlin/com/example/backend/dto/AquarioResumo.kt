package com.example.backend.dto

import com.example.backend.entity.StatusAquario
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resumo simplificado de um Aquario (para embutir em outras responses).")
data class AquarioResumo(
    val id: Long,
    val nome: String,
    val status: StatusAquario
)
