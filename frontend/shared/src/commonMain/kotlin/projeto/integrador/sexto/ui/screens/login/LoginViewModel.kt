package projeto.integrador.sexto.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.local.AuthStore
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class LoginViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api,
    private val authStore: AuthStore = ProvedorDependencias.authStore
) {

    data class UiState(
        val email: String = "",
        val senha: String = "",
        val carregando: Boolean = false,
        val erroEmail: String? = null,
        val erroSenha: String? = null,
        val erroGeral: String? = null,
        val loginRealizadoComSucesso: String? = null,
        val mensagemInformativa: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarEmail(novoEmail: String) {
        uiState = uiState.copy(
            email = novoEmail,
            erroEmail = null,
            erroGeral = null,
            mensagemInformativa = null
        )
    }

    fun aoMudarSenha(novaSenha: String) {
        uiState = uiState.copy(
            senha = novaSenha,
            erroSenha = null,
            erroGeral = null,
            mensagemInformativa = null
        )
    }

    fun definirMensagemInformativa(mensagem: String?) {
        uiState = uiState.copy(mensagemInformativa = mensagem)
    }

    fun tentarLogar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        if (uiState.email.isBlank()) {
            erros["email"] = "Informe seu email."
        } else if (!uiState.email.contains("@")) {
            erros["email"] = "Email invalido."
        }

        if (uiState.senha.isBlank()) {
            erros["senha"] = "Informe sua senha."
        } else if (uiState.senha.length < 6) {
            erros["senha"] = "Senha muito curta (minimo 6 caracteres)."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroEmail = erros["email"],
                erroSenha = erros["senha"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                loginRealizadoComSucesso = null
            )

            when (val resultado = api.login(uiState.email, uiState.senha)) {
                is ResultadoApi.Sucesso -> {
                    val dados = resultado.dados
                    authStore.salvarSessao(
                        token = dados.token,
                        tipoToken = dados.tipo,
                        lojaId = dados.lojaId,
                        nomeLoja = dados.nomeLoja,
                        emailLoja = dados.emailLoja
                    )
                    uiState = uiState.copy(
                        carregando = false,
                        loginRealizadoComSucesso = dados.nomeLoja
                    )
                }

                is ResultadoApi.Falha -> {
                    uiState = uiState.copy(
                        carregando = false,
                        erroGeral = resultado.mensagem
                    )
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}
