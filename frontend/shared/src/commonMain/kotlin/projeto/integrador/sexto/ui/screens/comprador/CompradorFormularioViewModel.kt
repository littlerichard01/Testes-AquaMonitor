package projeto.integrador.sexto.ui.screens.comprador

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.CompradorRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class CompradorFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val nome: String = "",
        val cpf: String = "",
        val email: String = "",
        val telefone: String = "",
        val observacoes: String = "",
        val carregando: Boolean = false,
        val erroNome: String? = null,
        val erroCpf: String? = null,
        val erroEmail: String? = null,
        val erroTelefone: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarNome(valor: String) {
        uiState = uiState.copy(nome = valor, erroNome = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarCpf(valor: String) {
        uiState = uiState.copy(cpf = valor, erroCpf = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarEmail(valor: String) {
        uiState = uiState.copy(email = valor, erroEmail = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarTelefone(valor: String) {
        uiState = uiState.copy(telefone = valor, erroTelefone = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarObservacoes(valor: String) {
        uiState = uiState.copy(observacoes = valor, erroGeral = null, mensagemSucesso = null)
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        if (uiState.nome.isBlank()) {
            erros["nome"] = "Informe o nome do comprador."
        } else if (uiState.nome.trim().length > 150) {
            erros["nome"] = "O nome deve ter no maximo 150 caracteres."
        }

        val cpfDigitos = uiState.cpf.filter { it.isDigit() }
        if (uiState.cpf.isNotBlank() && cpfDigitos.length != 11) {
            erros["cpf"] = "O CPF deve ter 11 digitos."
        }

        val emailLimpo = uiState.email.trim()
        if (emailLimpo.isNotEmpty() && !emailValido(emailLimpo)) {
            erros["email"] = "Informe um email valido."
        }

        val telefoneDigitos = uiState.telefone.filter { it.isDigit() }
        if (uiState.telefone.isNotBlank() && telefoneDigitos.length !in 10..11) {
            erros["telefone"] = "Informe o telefone com DDD (10 ou 11 digitos)."
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroNome = erros["nome"],
                erroCpf = erros["cpf"],
                erroEmail = erros["email"],
                erroTelefone = erros["telefone"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = CompradorRequestDto(
                nome = uiState.nome.trim(),
                cpf = cpfDigitos.ifBlank { null },
                email = emailLimpo.lowercase().ifBlank { null },
                telefone = telefoneDigitos.ifBlank { null },
                observacoes = uiState.observacoes.trim().ifBlank { null }
            )

            when (val resultado = api.criarComprador(request)) {
                is ResultadoApi.Sucesso -> {
                    limparCampos()
                    uiState = uiState.copy(mensagemSucesso = "Comprador cadastrado com sucesso!")
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

    private fun emailValido(email: String): Boolean {
        val partes = email.split("@")
        return partes.size == 2 &&
            partes[0].isNotEmpty() &&
            partes[1].contains(".") &&
            !email.contains(" ")
    }

    fun onCleared() {
        scope.cancel()
    }
}