package projeto.integrador.sexto.ui.screens.cadastro

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

class CadastroViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val nome: String = "",
        val email: String = "",
        val senha: String = "",
        val confirmarSenha: String = "",
        val carregando: Boolean = false,
        val erroNome: String? = null,
        val erroEmail: String? = null,
        val erroSenha: String? = null,
        val erroConfirmarSenha: String? = null,
        val erroGeral: String? = null,
        val cadastroRealizadoComSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarNome(novoNome: String) {
        uiState = uiState.copy(nome = novoNome, erroNome = null, erroGeral = null)
    }

    fun aoMudarEmail(novoEmail: String) {
        uiState = uiState.copy(email = novoEmail, erroEmail = null, erroGeral = null)
    }

    fun aoMudarSenha(novaSenha: String) {
        uiState = uiState.copy(senha = novaSenha, erroSenha = null, erroGeral = null)
    }

    fun aoMudarConfirmarSenha(novaConfirmacao: String) {
        uiState = uiState.copy(confirmarSenha = novaConfirmacao, erroConfirmarSenha = null, erroGeral = null)
    }

    fun tentarCadastrar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        if (uiState.nome.isBlank()) {
            erros["nome"] = "Informe o nome da loja."
        }

        if (uiState.email.isBlank()) {
            erros["email"] = "Informe seu email."
        } else if (!uiState.email.contains("@")) {
            erros["email"] = "Email inválido."
        }

        if (uiState.senha.isBlank()) {
            erros["senha"] = "Defina uma senha."
        } else if (uiState.senha.length < 6) {
            erros["senha"] = "A senha deve ter pelo menos 6 caracteres."
        }

        if (uiState.confirmarSenha.isBlank()) {
            erros["confirmarSenha"] = "Confirme a sua senha."
        } else if (uiState.senha != uiState.confirmarSenha) {
            erros["confirmarSenha"] = "As senhas não coincidem."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroNome = erros["nome"],
                erroEmail = erros["email"],
                erroSenha = erros["senha"],
                erroConfirmarSenha = erros["confirmarSenha"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                cadastroRealizadoComSucesso = null
            )

            when (val resultado = api.cadastrarLoja(
                nome = uiState.nome,
                email = uiState.email,
                senha = uiState.senha
            )) {
                is ResultadoApi.Sucesso -> {
                    val nomeLoja = resultado.dados.nome
                    uiState = uiState.copy(
                        carregando = false,
                        cadastroRealizadoComSucesso = nomeLoja
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