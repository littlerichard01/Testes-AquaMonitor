package projeto.integrador.sexto.ui.screens.loja

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class LojaFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val nome: String = "",
        val email: String = "",
        val senha: String = "",
        val ativo: Boolean = true,
        val carregando: Boolean = false,
        val erroNome: String? = null,
        val erroEmail: String? = null,
        val erroSenha: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarNome(valor: String) {
        uiState = uiState.copy(nome = valor, erroNome = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarEmail(valor: String) {
        uiState = uiState.copy(email = valor, erroEmail = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarSenha(valor: String) {
        uiState = uiState.copy(senha = valor, erroSenha = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarAtivo(valor: Boolean) {
        uiState = uiState.copy(ativo = valor, erroGeral = null, mensagemSucesso = null)
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        if (uiState.nome.isBlank()) {
            erros["nome"] = "Informe o nome da loja."
        }

        if (uiState.email.isBlank()) {
            erros["email"] = "Informe o email."
        } else if (!uiState.email.contains("@")) {
            erros["email"] = "Email invalido."
        }

        if (uiState.senha.isBlank()) {
            erros["senha"] = "Defina uma senha."
        } else if (uiState.senha.length < 6) {
            erros["senha"] = "A senha deve ter pelo menos 6 caracteres."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroNome = erros["nome"],
                erroEmail = erros["email"],
                erroSenha = erros["senha"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            when (val resultado = api.cadastrarLoja(
                nome = uiState.nome,
                email = uiState.email,
                senha = uiState.senha
            )) {
                is ResultadoApi.Sucesso -> {
                    uiState = uiState.copy(
                        carregando = false,
                        mensagemSucesso = "Loja cadastrada com sucesso!"
                    )
                    limparCampos()
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
