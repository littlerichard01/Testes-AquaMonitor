package projeto.integrador.sexto.ui.screens.aquario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import projeto.integrador.sexto.data.dto.AquarioRequestDto
import projeto.integrador.sexto.data.remote.AquaMonitorApi
import projeto.integrador.sexto.data.remote.ResultadoApi
import projeto.integrador.sexto.di.ProvedorDependencias

class AquarioFormularioViewModel(
    private val api: AquaMonitorApi = ProvedorDependencias.api
) {

    data class UiState(
        val nome: String = "",
        val descricao: String = "",
        val volumeLitros: String = "",
        val tipo: String = "",
        val localizacao: String = "",
        val status: String = "ATIVO",
        val preco: String = "",
        val carregando: Boolean = false,
        val erroNome: String? = null,
        val erroTipo: String? = null,
        val erroVolumeLitros: String? = null,
        val erroPreco: String? = null,
        val erroGeral: String? = null,
        val mensagemSucesso: String? = null
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun aoMudarNome(valor: String) {
        uiState = uiState.copy(nome = valor, erroNome = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarDescricao(valor: String) {
        uiState = uiState.copy(descricao = valor, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarVolumeLitros(valor: String) {
        uiState = uiState.copy(volumeLitros = valor, erroVolumeLitros = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarTipo(valor: String) {
        uiState = uiState.copy(tipo = valor, erroTipo = null, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarLocalizacao(valor: String) {
        uiState = uiState.copy(localizacao = valor, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarStatus(valor: String) {
        uiState = uiState.copy(status = valor, erroGeral = null, mensagemSucesso = null)
    }

    fun aoMudarPreco(valor: String) {
        uiState = uiState.copy(preco = valor, erroPreco = null, erroGeral = null, mensagemSucesso = null)
    }

    fun limparCampos() {
        uiState = UiState()
    }

    fun gravar() {
        if (uiState.carregando) return

        val erros = mutableMapOf<String, String>()

        if (uiState.nome.isBlank()) {
            erros["nome"] = "Informe o nome do aquario."
        }

        if (uiState.tipo.isBlank()) {
            erros["tipo"] = "Informe o tipo do aquario."
        }

        var volumeDouble: Double? = null
        if (uiState.volumeLitros.isNotBlank()) {
            volumeDouble = uiState.volumeLitros.replace(',', '.').toDoubleOrNull()
            if (volumeDouble == null || volumeDouble <= 0) {
                erros["volumeLitros"] = "Informe um volume valido maior que zero."
            }
        }

        var precoDouble: Double? = null
        if (uiState.preco.isNotBlank()) {
            precoDouble = uiState.preco.replace(',', '.').toDoubleOrNull()
            if (precoDouble == null || precoDouble < 0) {
                erros["preco"] = "Informe um preco valido."
            }
        }

        if (erros.isNotEmpty()) {
            uiState = uiState.copy(
                erroNome = erros["nome"],
                erroTipo = erros["tipo"],
                erroVolumeLitros = erros["volumeLitros"],
                erroPreco = erros["preco"]
            )
            return
        }

        scope.launch {
            uiState = uiState.copy(
                carregando = true,
                erroGeral = null,
                mensagemSucesso = null
            )

            val request = AquarioRequestDto(
                nome = uiState.nome.trim(),
                descricao = uiState.descricao.trim().ifBlank { null },
                volumeLitros = volumeDouble,
                tipo = uiState.tipo.trim(),
                localizacao = uiState.localizacao.trim().ifBlank { null },
                status = uiState.status.trim().ifBlank { "ATIVO" },
                preco = precoDouble
            )

            when (val resultado = api.criarAquario(request)) {
                is ResultadoApi.Sucesso -> {
                    uiState = uiState.copy(
                        carregando = false,
                        mensagemSucesso = "Aquario cadastrado com sucesso!"
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
