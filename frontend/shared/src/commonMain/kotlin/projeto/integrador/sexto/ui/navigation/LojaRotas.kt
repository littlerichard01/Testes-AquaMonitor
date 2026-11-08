package projeto.integrador.sexto.ui.navigation

import kotlinx.serialization.Serializable

sealed interface LojaRotas : RotaApp {

    @Serializable
    object formulario : LojaRotas
}
