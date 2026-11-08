package projeto.integrador.sexto.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface RotaApp : NavKey {

    @Serializable
    data class Login(val mensagemExito: String? = null) : RotaApp

    @Serializable
    data object Cadastro : RotaApp

    @Serializable
    data class Home(val nomeUsuario: String = "Usuario") : RotaApp

    @Serializable
    data object FormularioConfiguracaoTemperatura : RotaApp

    @Serializable
    data object FormularioLeituraTemperatura : RotaApp

    @Serializable
    data object FormularioComprador : RotaApp

    @Serializable
    data object FormularioProgramacaoAlimentacao : RotaApp

    @Serializable
    data object FormularioVenda : RotaApp

    @Serializable
    data object FormularioEspecie : RotaApp

    @Serializable
    data object FormularioDispositivoIoT : RotaApp

    @Serializable
    data object FormularioEspecime : RotaApp

    @Serializable
    data object FormularioAlerta : RotaApp
}
