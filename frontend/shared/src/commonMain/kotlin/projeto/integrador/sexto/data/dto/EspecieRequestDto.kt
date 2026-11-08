package projeto.integrador.sexto.data.dto

import kotlinx.serialization.Serializable

@Serializable

data class EspecieRequestDto(

    val nomePopular: String,

    val nomeCientifico: String? = null,

    val tipo: String,

    val descricao: String? = null,

    val temperaturaMinima: Double? = null,

    val temperaturaMaxima: Double? = null

)