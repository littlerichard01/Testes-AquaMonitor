package projeto.integrador.sexto.ui.navigation

import kotlinx.serialization.Serializable

sealed interface AquarioRotas : RotaApp {

    @Serializable
    object formulario : AquarioRotas
}
