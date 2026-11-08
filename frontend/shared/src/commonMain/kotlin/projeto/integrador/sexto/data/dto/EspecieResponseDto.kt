package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable

data class EspecieResponseDto(

    val id: Long,

    val nomePopular: String,

    val nomeCientifico: String? = null,

    val tipo: String,

    val descricao: String? = null,

    val temperaturaMinima: Double? = null,

    val temperaturaMaxima: Double? = null,

    val criadoEm: String,

    val atualizadoEm: String

)